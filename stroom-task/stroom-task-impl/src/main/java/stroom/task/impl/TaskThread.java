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

package stroom.task.impl;

import stroom.security.api.UserIdentity;
import stroom.task.shared.Task;
import stroom.util.shared.HasTerminate;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

class TaskThread implements HasTerminate {
    private final Task<?> task;
    private final UserIdentity userIdentity;
    private final long submitTimeMs = System.currentTimeMillis();

    private final Set<TaskThread> children = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private volatile boolean terminate;
    private volatile Supplier<String> messageSupplier;
    private volatile String name;

    private volatile Thread thread;

    TaskThread(final Task<?> task, final UserIdentity userIdentity) {
        this.task = task;
        this.userIdentity = userIdentity;
    }

    Task<?> getTask() {
        return task;
    }

    String getUserId() {
        if (userIdentity != null) {
            return userIdentity.getId();
        }
        return null;
    }

    String getSessionId() {
        if (userIdentity != null) {
            return userIdentity.getSessionId();
        }
        return null;
    }

    synchronized void setThread(final Thread thread) {
        this.thread = thread;
        if (terminate) {
            interrupt();
        }
    }

    boolean isTerminated() {
        final Thread thread = this.thread;
        if (thread != null) {
            if (thread.isInterrupted()) {
                // Make sure the thread hasn't been reassigned.
                if (thread == this.thread) {
                    return true;
                }
            }
        }

        return terminate;
    }

    @Override
    public synchronized void terminate() {
        this.terminate = true;
        children.forEach(TaskThread::terminate);

        interrupt();
    }

    synchronized void interrupt() {
        final Thread thread = this.thread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @SuppressWarnings("deprecation")
    synchronized void kill() {
        final Thread thread = this.thread;
        if (thread != null) {
            thread.stop();
        }
    }

    String getThreadName() {
        final Thread thread = this.thread;
        if (thread != null) {
            return thread.getName();
        }
        return null;
    }

    long getSubmitTimeMs() {
        return submitTimeMs;
    }

    String getName() {
        String name = this.name;
        if (name == null) {
            name = task.getTaskName();
        }
        if (terminate) {
            name = "<<terminated>> " + name;
        }

        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    String getInfo() {
        final Supplier<String> messageSupplier = this.messageSupplier;
        if (messageSupplier != null) {
            return messageSupplier.get();
        }
        return "";
    }

    void info(final Supplier<String> messageSupplier) {
        this.messageSupplier = messageSupplier;
    }

    synchronized void addChild(final TaskThread taskThread) {
        children.add(taskThread);
        if (terminate) {
            taskThread.terminate();
        }
    }

    void removeChild(final TaskThread taskThread) {
        children.remove(taskThread);
    }

    Set<TaskThread> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
