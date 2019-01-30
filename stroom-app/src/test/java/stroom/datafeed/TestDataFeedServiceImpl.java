/*
 * Copyright 2017 Crown Copyright
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
 *
 */

package stroom.datafeed;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stroom.data.store.impl.fs.MockStreamStore;
import stroom.meta.shared.StandardHeaderArguments;
import stroom.util.date.DateUtil;
import stroom.util.io.StreamUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The combination of mock and prod classes means this test needs its own
 * context.
 */
class TestDataFeedServiceImpl extends TestBase {
    @Inject
    private DataFeedServlet dataFeedService;
    @Inject
    private MockHttpServletRequest request;
    @Inject
    private MockHttpServletResponse response;
    @Inject
    private MockStreamStore streamStore;

    @BeforeEach
    void init() {
        request.resetMock();
        response.resetMock();
        streamStore.clear();
    }

    @Test
    void testErrorNoParameters() throws IOException, ServletException {
        dataFeedService.doPost(request, response);
        checkError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Feed must be specified");
    }

    private void checkError(final int code, final String msg) {
        assertThat(response.getResponseCode()).isEqualTo(code);
        assertThat(response.getSendErrorMessage().contains(msg)).as("Expecting '" + msg + "' but was '" + response.getSendErrorMessage() + "'").isTrue();
    }

    private void checkOK() {
        assertThat(response.getResponseCode()).as(response.getSendErrorMessage()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    void testErrorCompressionInvalid() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("effectiveTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "UNKNOWN");
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Unknown compression");
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);
    }

    @Test
    void testOkCompressionNone() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("effectiveTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "NONE");
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("SOME TEST DATA");
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(1);
    }

    @Test
    void testOkWithQueryString() throws IOException, ServletException {
        request.setQueryString("feed=TEST-FEED" + "&periodStartTime=" + DateUtil.createNormalDateTimeString()
                + "&periodEndTime=" + DateUtil.createNormalDateTimeString());
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("SOME TEST DATA");
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(1);
    }

    @Test
    void testOkCompressionBlank() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "");
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("SOME TEST DATA");
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(1);
    }

    @Test
    void testOkCompressionNotStated() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("SOME TEST DATA");
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(1);
    }

    @Test
    void testErrorCompressionGZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "GZIP");
        request.setInputStream("SOME TEST DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "format");
        // checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
        // "Input is not in the .gz format");
    }

    @Test
    void testErrorCompressionZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "ZIP");

        // Data needs to be big else it gets dropped
        request.setInputStream("SOME TEST DATA XXXXXXXXXXX XXXXXXXXXXXXXX XXXXXXXXXXXXXXXX".getBytes());

        dataFeedService.doPost(request, response);

        checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Compressed stream invalid");
    }

    @Test
    void testEmptyCompressionZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "ZIP");
        // Data needs to be big else it gets dropped
        request.setInputStream("SMALL DATA".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();
    }

    @Test
    void testOKCompressionGZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "GZIP");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final InputStream inputStream = new ByteArrayInputStream("SOME TEST DATA".getBytes());
             final OutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            StreamUtil.streamToStream(inputStream, gzipOutputStream);
        }
        request.setInputStream(outputStream.toByteArray());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("SOME TEST DATA");
    }

    @Test
    void testOKCompressionGZIP2() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "GZIP");
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final InputStream inputStream = new ByteArrayInputStream("LINE1\n".getBytes());
             final OutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            StreamUtil.streamToStream(inputStream, gzipOutputStream);
        }
        try (final InputStream inputStream = new ByteArrayInputStream("LINE2\n".getBytes());
             final OutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            StreamUtil.streamToStream(inputStream, gzipOutputStream);
        }
        request.setInputStream(outputStream.toByteArray());

        dataFeedService.doPost(request, response);

        checkOK();

        assertThat(StreamUtil
                .streamToString(streamStore.openStreamSource(streamStore.getLastMeta().getId()).getInputStream())).isEqualTo("LINE1\nLINE2\n");
    }

    @Test
    void testOKCompressionZeroContent() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader(StandardHeaderArguments.CONTENT_LENGTH, "0");
        request.addHeader("compression", "GZIP");
        request.setInputStream("".getBytes());

        dataFeedService.doPost(request, response);

        checkOK();
    }

    @Test
    void testOKCompressionZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "ZIP");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final InputStream inputStream = new ByteArrayInputStream("SOME TEST DATA".getBytes());
             final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.putNextEntry(new ZipEntry("TEST.txt"));
            StreamUtil.streamToStream(inputStream, zipOutputStream);
        }

        request.setInputStream(outputStream.toByteArray());

        dataFeedService.doPost(request, response);

        checkOK();
    }

    @Test
    void testIOErrorWhileWriteUncompressesd() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.setInputStream(new CorruptInputStream(new ByteArrayInputStream("SOME TEST DATA".getBytes()), 10));
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);

        dataFeedService.doPost(request, response);

        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);

        checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Expected IO Junit Error at byte ");
    }

    @Test
    void testIOErrorWhileWriteGZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "GZIP");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final InputStream inputStream = new ByteArrayInputStream("SOME TEST DATA".getBytes());
             final OutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            StreamUtil.streamToStream(inputStream, gzipOutputStream);
        }

        request.setInputStream(new CorruptInputStream(new ByteArrayInputStream(outputStream.toByteArray()), 10));

        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);

        dataFeedService.doPost(request, response);

        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);

        checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Expected IO Junit Error at byte ");
    }

    @Test
    void testIOErrorWhileWriteZIP() throws IOException, ServletException {
        request.addHeader("feed", "TEST-FEED");
        request.addHeader("periodStartTime", DateUtil.createNormalDateTimeString());
        request.addHeader("periodEndTime", DateUtil.createNormalDateTimeString());
        request.addHeader("compression", "ZIP");

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final InputStream inputStream = new ByteArrayInputStream("SOME TEST DATA".getBytes());
             final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            zipOutputStream.putNextEntry(new ZipEntry("TEST.txt"));
            StreamUtil.streamToStream(inputStream, zipOutputStream);
        }

        request.setInputStream(new CorruptInputStream(new ByteArrayInputStream(outputStream.toByteArray()), 10));

        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);
        dataFeedService.doPost(request, response);
        assertThat(streamStore.getStreamStoreCount()).isEqualTo(0);

        checkError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Expected IO Junit Error at byte ");
    }
}
