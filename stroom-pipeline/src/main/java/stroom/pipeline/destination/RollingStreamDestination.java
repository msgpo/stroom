/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.pipeline.destination;

import stroom.feed.MetaMap;
import stroom.streamstore.server.StreamStore;
import stroom.streamstore.server.StreamTarget;
import stroom.streamstore.server.fs.serializable.RASegmentOutputStream;
import stroom.streamstore.shared.StreamAttributeConstants;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class RollingStreamDestination extends RollingDestination {
    private final StreamStore streamStore;
    private final StreamTarget streamTarget;
    private final String nodeName;
    private final AtomicLong recordCount = new AtomicLong();
    private RASegmentOutputStream segmentOutputStream;

    public RollingStreamDestination(final StreamKey key,
                                    final long frequency,
                                    final long maxSize,
                                    final long creationTime,
                                    final StreamStore streamStore,
                                    final StreamTarget streamTarget,
                                    final String nodeName) throws IOException {
        super(key, frequency, maxSize, creationTime);

        this.streamStore = streamStore;
        this.streamTarget = streamTarget;
        this.nodeName = nodeName;

        if (key.isSegmentOutput()) {
            segmentOutputStream = new RASegmentOutputStream(streamTarget);
            setOutputStream(new ByteCountOutputStream(segmentOutputStream));
        } else {
            setOutputStream(new ByteCountOutputStream(streamTarget.getOutputStream()));
        }
    }

    @Override
    protected void onHeaderWritten(final ByteCountOutputStream outputStream,
                                   final Consumer<Throwable> exceptionConsumer) {
        if (outputStream != null && outputStream.getBytesWritten() > 0) {
            // Add a segment marker to the output stream if we are
            // segmenting.
            if (segmentOutputStream != null) {
                try {
                    segmentOutputStream.addSegment();
                } catch (IOException e) {
                    exceptionConsumer.accept(e);
                }
            }
        }

        recordCount.incrementAndGet();
    }

    @Override
    protected void onFooterWritten(final Consumer<Throwable> exceptionConsumer) {
        // Write meta data to stream target.
        final MetaMap metaMap = new MetaMap();
        metaMap.put(StreamAttributeConstants.REC_WRITE, recordCount.toString());
        metaMap.put(StreamAttributeConstants.NODE, nodeName);
        streamTarget.getAttributeMap().putAll(metaMap);
        streamStore.closeStreamTarget(streamTarget);
    }
}
