/*
 * Copyright 2018 Crown Copyright
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

package stroom.job.impl;

import stroom.event.logging.api.ObjectInfoProviderBinder;
import stroom.job.api.JobManager;
import stroom.job.api.ScheduledJobsModule;
import stroom.job.shared.Job;
import stroom.job.shared.JobNode;
import stroom.util.guice.RestResourcesBinder;

import com.google.inject.AbstractModule;

public class JobSystemModule extends AbstractModule {
    @Override
    protected void configure() {
        // Ensure the scheduled jobs binder is present even if we don't bind actual jobs.
        install(new ScheduledJobsModule());

        bind(JobManager.class).to(JobManagerImpl.class);

        RestResourcesBinder.create(binder())
                .bindResource(JobResourceImpl.class)
                .bindResource(JobNodeResourceImpl.class)
                .bindResource(ScheduledTimeResourceImpl.class);

        // Provide object info to the logging service.
        ObjectInfoProviderBinder.create(binder())
                .bind(Job.class, JobObjectInfoProvider.class)
                .bind(JobNode.class, JobNodeObjectInfoProvider.class);
    }
}