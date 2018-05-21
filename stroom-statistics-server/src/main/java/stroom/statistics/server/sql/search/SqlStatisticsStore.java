package stroom.statistics.server.sql.search;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.dashboard.expression.v1.FieldIndexMap;
import stroom.dashboard.expression.v1.Val;
import stroom.node.server.StroomPropertyService;
import stroom.query.api.v2.Param;
import stroom.query.api.v2.SearchRequest;
import stroom.query.common.v2.CompletionListener;
import stroom.query.common.v2.Coprocessor;
import stroom.query.common.v2.CoprocessorSettings;
import stroom.query.common.v2.CoprocessorSettingsMap;
import stroom.query.common.v2.Data;
import stroom.query.common.v2.Payload;
import stroom.query.common.v2.ResultHandler;
import stroom.query.common.v2.SearchResultHandler;
import stroom.query.common.v2.Store;
import stroom.query.common.v2.StoreSize;
import stroom.query.common.v2.TableCoprocessor;
import stroom.query.common.v2.TableCoprocessorSettings;
import stroom.query.common.v2.TablePayload;
import stroom.statistics.shared.StatisticStoreEntity;
import stroom.task.server.TaskContext;
import stroom.util.logging.LambdaLogger;
import stroom.util.logging.LambdaLoggerFactory;
import stroom.util.shared.HasTerminate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class SqlStatisticsStore implements Store {

    private static final Logger LOGGER = LoggerFactory.getLogger(SqlStatisticsStore.class);
    private static final LambdaLogger LAMBDA_LOGGER = LambdaLoggerFactory.getLogger(SqlStatisticsStore.class);

    private static final String TASK_NAME = "Sql Statistic Search";

    public static final Duration RESULT_SEND_INTERVAL = Duration.ofSeconds(1);

    private final ResultHandler resultHandler;
    private final int resultHandlerBatchSize;
    private final List<Integer> defaultMaxResultsSizes;
    private final StoreSize storeSize;
    private final AtomicBoolean isComplete;
    private final Queue<CompletionListener> completionListeners = new ConcurrentLinkedQueue<>();
    private final List<String> errors = Collections.synchronizedList(new ArrayList<>());
    private final HasTerminate terminationMonitor;
    private final TaskContext taskContext;
    private final String searchKey;
    private final CompositeDisposable compositeDisposable;

    SqlStatisticsStore(final SearchRequest searchRequest,
                       final StatisticStoreEntity statisticStoreEntity,
                       final StatisticsSearchService statisticsSearchService,
                       final List<Integer> defaultMaxResultsSizes,
                       final StoreSize storeSize,
                       final int resultHandlerBatchSize,
                       final Executor executor,
                       final TaskContext taskContext) {

        this.defaultMaxResultsSizes = defaultMaxResultsSizes;
        this.storeSize = storeSize;
        this.isComplete = new AtomicBoolean(false);
        this.searchKey = searchRequest.getKey().toString();
        this.taskContext = taskContext;
        this.resultHandlerBatchSize = resultHandlerBatchSize;

        final CoprocessorSettingsMap coprocessorSettingsMap = CoprocessorSettingsMap.create(searchRequest);
        Preconditions.checkNotNull(coprocessorSettingsMap);

        final FieldIndexMap fieldIndexMap = new FieldIndexMap(true);
        final Map<String, String> paramMap = getParamMap(searchRequest);

//        terminationMonitor = getTerminationMonitor();
        terminationMonitor = taskContext;

        final Map<CoprocessorSettingsMap.CoprocessorKey, Coprocessor> coprocessorMap = getCoprocessorMap(
                coprocessorSettingsMap, fieldIndexMap, paramMap);

        // convert the search into something stats understands
        final FindEventCriteria criteria = StatStoreCriteriaBuilder.buildCriteria(searchRequest, statisticStoreEntity);

        resultHandler = new SearchResultHandler(coprocessorSettingsMap, defaultMaxResultsSizes, storeSize);

        //get the flowable for the search results
        final Flowable<Val[]> searchResultsFlowable = statisticsSearchService.search(
                statisticStoreEntity, criteria, fieldIndexMap);

        this.compositeDisposable = startAsyncSearch(searchResultsFlowable, coprocessorMap, executor, taskContext);

        LOGGER.debug("Async search task started for key {}", searchKey);
    }


    @Override
    public void destroy() {
        LOGGER.debug("destroy called");
        //terminate the search
        // TODO this may need to change in 6.1
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete.get();
    }

    @Override
    public Data getData(String componentId) {
        LOGGER.debug("getData called for componentId {}", componentId);

        return resultHandler.getResultStore(componentId);
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public List<String> getHighlights() {
        return null;
    }

    @Override
    public List<Integer> getDefaultMaxResultsSizes() {
        return defaultMaxResultsSizes;
    }

    @Override
    public StoreSize getStoreSize() {
        return storeSize;
    }

    @Override
    public void registerCompletionListener(final CompletionListener completionListener) {
        completionListeners.add(Objects.requireNonNull(completionListener));

        if (isComplete.get()) {
            //immediate notification
            notifyListenersOfCompletion();
        }
    }

    @Override
    public String toString() {
        return "SqlStatisticsStore{" +
                "defaultMaxResultsSizes=" + defaultMaxResultsSizes +
                ", storeSize=" + storeSize +
                ", isComplete=" + isComplete +
//                ", isTerminated=" + isTerminated +
                ", searchKey='" + searchKey + '\'' +
                '}';
    }

    private Map<String, String> getParamMap(final SearchRequest searchRequest) {
        final Map<String, String> paramMap;
        if (searchRequest.getQuery().getParams() != null) {
            paramMap = searchRequest.getQuery().getParams().stream()
                    .collect(Collectors.toMap(Param::getKey, Param::getValue));
        } else {
            paramMap = Collections.emptyMap();
        }
        return paramMap;
    }

    private CompositeDisposable startAsyncSearch(
            final Flowable<Val[]> searchResultsFlowable,
            final Map<CoprocessorSettingsMap.CoprocessorKey, Coprocessor> coprocessorMap,
            final Executor executor,
            final TaskContext taskContext) {

        LOGGER.debug("Starting search with key {}", searchKey);
        taskContext.setName(TASK_NAME);
        taskContext.info("Sql Statistics search " + searchKey + " - running query");

        final LongAdder counter = new LongAdder();
        // subscribe to the flowable, mapping each resultSet to a String[]
        // After the window period has elapsed a new flowable is create for those rows received
        // in that window, which can all be processed and sent
        // If the task is canceled, the flowable produced by search() will stop emitting
        // Set up the results flowable, the search wont be executed until subscribe is called
        final Scheduler scheduler = Schedulers.from(executor);
        final AtomicLong nextProcessPayloadsTime = new AtomicLong(Instant.now().plus(RESULT_SEND_INTERVAL).toEpochMilli());
        final AtomicLong countSinceLastSend = new AtomicLong(0);
        final Instant queryStart = Instant.now();

        // TODO this may need to change in 6.1 due to differences in task termination
        // concatMapping a just() is a bit of a hack to ensure we have a single thread for task
        // monitoring and termination purposes.
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        final Disposable searchResultsDisposable = Flowable.just(0)
                .subscribeOn(scheduler)
                .concatMap(val -> searchResultsFlowable)
                .doOnSubscribe(subscription -> {
                    LOGGER.debug("doOnSubscribeCalled");
                })
                .subscribe(
                        data -> {
                            counter.increment();
                            countSinceLastSend.incrementAndGet();
                            LAMBDA_LOGGER.trace(() -> String.format("data: [%s]", Arrays.toString(data)));

                            // give the data array to each of our coprocessors
                            coprocessorMap.values().forEach(coprocessor ->
                                    coprocessor.receive(data));
                            // send what we have every 1s or when the batch reaches a set size
                            long now = System.currentTimeMillis();
                            if (now >= nextProcessPayloadsTime.get() ||
                                    countSinceLastSend.get() >= resultHandlerBatchSize) {

                                LAMBDA_LOGGER.debug(() -> LambdaLogger.buildMessage("{} vs {}, {} vs {}",
                                        now, nextProcessPayloadsTime,
                                        countSinceLastSend.get(), resultHandlerBatchSize));

                                processPayloads(resultHandler, coprocessorMap, terminationMonitor);
                                taskContext.setName(TASK_NAME);
                                taskContext.info(searchKey +
                                        " - running database query (" + counter.longValue() + " rows fetched)");
                                nextProcessPayloadsTime.set(Instant.now().plus(RESULT_SEND_INTERVAL).toEpochMilli());
                                countSinceLastSend.set(0);
                            }
                        },
                        throwable -> {
                            LOGGER.error("Error in windowed flow: {}", throwable.getMessage(), throwable);
                            errors.add(throwable.getMessage());
                        },
                        () -> {
                            LAMBDA_LOGGER.debug(() ->
                                    String.format("onComplete of flowable called, counter: %s",
                                            counter.longValue()));
                            // completed our window so create and pass on a payload for the
                            // data we have gathered so far
                            processPayloads(resultHandler, coprocessorMap, terminationMonitor);
                            taskContext.info(searchKey + " - complete");
                            completeSearch();

                            LAMBDA_LOGGER.debug(() ->
                                    LambdaLogger.buildMessage("Query finished in {}", Duration.between(queryStart, Instant.now())));
                        });

        LOGGER.debug("Out of flowable");

        compositeDisposable.add(searchResultsDisposable);

        return compositeDisposable;
    }

    private Map<CoprocessorSettingsMap.CoprocessorKey, Coprocessor> getCoprocessorMap(
            final CoprocessorSettingsMap coprocessorSettingsMap,
            final FieldIndexMap fieldIndexMap,
            final Map<String, String> paramMap) {

        return coprocessorSettingsMap.getMap()
                .entrySet()
                .stream()
                .map(entry -> Maps.immutableEntry(
                        entry.getKey(),
                        createCoprocessor(entry.getValue(), fieldIndexMap, paramMap, terminationMonitor)))
                .filter(entry -> entry.getKey() != null)
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void notifyListenersOfCompletion() {
        //Call isComplete to ensure we are complete and not terminated
        LAMBDA_LOGGER.debug(() -> LambdaLogger.buildMessage("notifyListenersOfCompletion called for {} listeners",
                completionListeners.size()));

        if (isComplete()) {
            for (CompletionListener listener; (listener = completionListeners.poll()) != null; ) {
                //when notified they will check isComplete
                LOGGER.debug("Notifying {} {} that we are complete", listener.getClass().getName(), listener);
                listener.onCompletion();
            }
        }
    }

    private void completeSearch() {
        LOGGER.debug("completeSearch called");
        isComplete.set(true);
        notifyListenersOfCompletion();
        resultHandler.setComplete(true);
    }

    /**
     * Synchronized to ensure multiple threads don't fight over the coprocessors which is unlikely to
     * happen anyway as it is mostly used in
     */
    private synchronized void processPayloads(final ResultHandler resultHandler,
                                              final Map<CoprocessorSettingsMap.CoprocessorKey, Coprocessor> coprocessorMap,
                                              final HasTerminate terminationMonitor) {

        if (!Thread.currentThread().isInterrupted()) {
            LAMBDA_LOGGER.debug(() ->
                    LambdaLogger.buildMessage("processPayloads called for {} coprocessors", coprocessorMap.size()));

            //build a payload map from whatever the coprocessors have in them, if anything
            final Map<CoprocessorSettingsMap.CoprocessorKey, Payload> payloadMap = coprocessorMap.entrySet().stream()
                    .map(entry ->
                            Maps.immutableEntry(entry.getKey(), entry.getValue().createPayload()))
                    .filter(entry ->
                            entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // log the queue sizes in the payload map
            if (LOGGER.isDebugEnabled()) {
                final String contents = payloadMap.entrySet().stream()
                        .map(entry -> {
                            String key = entry.getKey() != null ? entry.getKey().toString() : "null";
                            String size;
                            // entry checked for null in stream above
                            if (entry.getValue() instanceof TablePayload) {
                                TablePayload tablePayload = (TablePayload) entry.getValue();
                                if (tablePayload.getQueue() != null) {
                                    size = Integer.toString(tablePayload.getQueue().size());
                                } else {
                                    size = "null";
                                }
                            } else {
                                size = "?";
                            }
                            return key + ": " + size;
                        })
                        .collect(Collectors.joining(", "));
                LOGGER.debug("payloadMap: [{}]", contents);
            }

            // give the processed results to the collector, it will handle nulls
            resultHandler.handle(payloadMap, terminationMonitor);
        } else {
            LOGGER.debug("Thread is interrupted, not processing payload");
        }
    }

    private static Coprocessor createCoprocessor(final CoprocessorSettings settings,
                                                 final FieldIndexMap fieldIndexMap,
                                                 final Map<String, String> paramMap,
                                                 final HasTerminate taskMonitor) {
        if (settings instanceof TableCoprocessorSettings) {
            final TableCoprocessorSettings tableCoprocessorSettings = (TableCoprocessorSettings) settings;
            final TableCoprocessor tableCoprocessor = new TableCoprocessor(
                    tableCoprocessorSettings, fieldIndexMap, taskMonitor, paramMap);
            return tableCoprocessor;
        }
        return null;
    }


}
