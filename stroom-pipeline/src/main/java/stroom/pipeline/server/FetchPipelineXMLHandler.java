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

package stroom.pipeline.server;

import stroom.pipeline.shared.FetchPipelineXMLAction;
import stroom.pipeline.shared.PipelineEntity;
import stroom.task.server.AbstractTaskHandler;
import stroom.task.server.TaskHandlerBean;
import stroom.util.shared.SharedString;

import javax.inject.Inject;

@TaskHandlerBean(task = FetchPipelineXMLAction.class)
class FetchPipelineXMLHandler extends AbstractTaskHandler<FetchPipelineXMLAction, SharedString> {
    private final PipelineService pipelineService;

    @Inject
    FetchPipelineXMLHandler(final PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @Override
    public SharedString exec(final FetchPipelineXMLAction action) {
        SharedString result = null;

        final PipelineEntity pipelineEntity = pipelineService.loadByUuidWithoutUnmarshal(action.getPipeline().getUuid());

        if (pipelineEntity != null) {
            result = SharedString.wrap(pipelineEntity.getData());
        }

        return result;
    }
}