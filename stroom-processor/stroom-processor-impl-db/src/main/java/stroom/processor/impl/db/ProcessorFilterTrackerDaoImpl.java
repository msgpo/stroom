package stroom.processor.impl.db;

import org.jooq.DSLContext;
import stroom.db.util.GenericDao;
import stroom.processor.impl.ProcessorFilterTrackerDao;
import stroom.processor.impl.db.jooq.tables.records.ProcessorFilterTrackerRecord;
import stroom.processor.shared.ProcessorFilterTracker;

import javax.inject.Inject;
import java.util.Optional;

import static stroom.processor.impl.db.jooq.tables.ProcessorFilterTracker.PROCESSOR_FILTER_TRACKER;

class ProcessorFilterTrackerDaoImpl implements ProcessorFilterTrackerDao {
    private final GenericDao<ProcessorFilterTrackerRecord, ProcessorFilterTracker, Integer> dao;

    @Inject
    ProcessorFilterTrackerDaoImpl(final ConnectionProvider connectionProvider) {
        this.dao = new GenericDao<>(PROCESSOR_FILTER_TRACKER, PROCESSOR_FILTER_TRACKER.ID, ProcessorFilterTracker.class, connectionProvider);
    }

    @Override
    public ProcessorFilterTracker create(final ProcessorFilterTracker processorFilterTracker) {
        return dao.create(processorFilterTracker);
    }

    @Override
    public Optional<ProcessorFilterTracker> fetch(final int id) {
        return dao.fetch(id);
    }

    @Override
    public ProcessorFilterTracker update(final ProcessorFilterTracker processorFilterTracker) {
        return dao.update(processorFilterTracker);
    }

    @Override
    public boolean delete(final int id) {
        return dao.delete(id);
    }

    public ProcessorFilterTracker update(final DSLContext context, final ProcessorFilterTracker processorFilterTracker) {
        return dao.update(context, processorFilterTracker);
    }
}
