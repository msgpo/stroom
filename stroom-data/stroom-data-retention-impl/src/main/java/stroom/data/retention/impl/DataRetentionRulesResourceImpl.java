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

package stroom.data.retention.impl;

import stroom.data.retention.shared.DataRetentionRules;
import stroom.data.retention.shared.DataRetentionRulesResource;
import stroom.security.api.SecurityContext;

import javax.inject.Inject;

class DataRetentionRulesResourceImpl implements DataRetentionRulesResource {
    private final DataRetentionRulesService dataRetentionRulesService;
    private final SecurityContext securityContext;

    @Inject
    DataRetentionRulesResourceImpl(final DataRetentionRulesService dataRetentionRulesService,
                                   final SecurityContext securityContext) {
        this.dataRetentionRulesService = dataRetentionRulesService;
        this.securityContext = securityContext;
    }

    @Override
    public DataRetentionRules read() {
        return securityContext.secureResult(() -> dataRetentionRulesService.readDocument(null));
    }

    @Override
    public DataRetentionRules update(final DataRetentionRules dataRetentionRules) {
        return securityContext.secureResult(() -> dataRetentionRulesService.writeDocument(dataRetentionRules));
    }
}