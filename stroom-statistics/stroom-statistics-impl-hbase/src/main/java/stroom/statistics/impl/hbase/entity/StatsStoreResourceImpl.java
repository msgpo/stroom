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
 */

package stroom.statistics.impl.hbase.entity;

import stroom.docref.DocRef;
import stroom.docstore.api.DocumentResourceHelper;
import stroom.statistics.impl.hbase.shared.StatsStoreResource;
import stroom.statistics.impl.hbase.shared.StroomStatsStoreDoc;

import javax.inject.Inject;

class StatsStoreResourceImpl implements StatsStoreResource {
    private final StroomStatsStoreStore stroomStatsStoreStore;
    private final DocumentResourceHelper documentResourceHelper;

    @Inject
    StatsStoreResourceImpl(final StroomStatsStoreStore stroomStatsStoreStore,
                           final DocumentResourceHelper documentResourceHelper) {
        this.stroomStatsStoreStore = stroomStatsStoreStore;
        this.documentResourceHelper = documentResourceHelper;
    }

    @Override
    public StroomStatsStoreDoc read(final DocRef docRef) {
        return documentResourceHelper.read(stroomStatsStoreStore, docRef);
    }

    @Override
    public StroomStatsStoreDoc update(final StroomStatsStoreDoc stroomStatsStoreDoc) {
        return documentResourceHelper.update(stroomStatsStoreStore, stroomStatsStoreDoc);
    }
}