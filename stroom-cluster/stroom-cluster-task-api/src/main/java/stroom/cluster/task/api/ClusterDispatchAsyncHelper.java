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

package stroom.cluster.task.api;

import stroom.docref.SharedObject;

import java.util.concurrent.TimeUnit;

public interface ClusterDispatchAsyncHelper {
    <R extends SharedObject> DefaultClusterResultCollector<R> execAsync(ClusterTask<R> task, String targetNodeName);

    <R extends SharedObject> DefaultClusterResultCollector<R> execAsync(ClusterTask<R> task, long waitTime, TimeUnit timeUnit, String targetNodeName);

    <R extends SharedObject> DefaultClusterResultCollector<R> execAsync(ClusterTask<R> task, TargetType targetType);

    <R extends SharedObject> DefaultClusterResultCollector<R> execAsync(ClusterTask<R> task, long waitTime, TimeUnit timeUnit, TargetType targetType);

    boolean isClusterStateInitialised();
}