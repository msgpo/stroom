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

package stroom.security.shared;

import stroom.docref.DocRef;
import stroom.docref.HasDisplayValue;
import stroom.task.shared.Action;
import stroom.util.shared.VoidResult;

public class ChangeDocumentPermissionsRequest {
    private DocRef docRef;
    private ChangeSet<UserPermission> changeSet;
    private Cascade cascade;

    public ChangeDocumentPermissionsRequest() {
        // Default constructor necessary for GWT serialisation.
    }

    public ChangeDocumentPermissionsRequest(final DocRef docRef, final ChangeSet<UserPermission> changeSet, final Cascade cascade) {
        this.docRef = docRef;
        this.changeSet = changeSet;
        this.cascade = cascade;
    }

    public DocRef getDocRef() {
        return docRef;
    }

    public void setDocRef(final DocRef docRef) {
        this.docRef = docRef;
    }

    public ChangeSet<UserPermission> getChangeSet() {
        return changeSet;
    }

    public void setChangeSet(final ChangeSet<UserPermission> changeSet) {
        this.changeSet = changeSet;
    }

    public Cascade getCascade() {
        return cascade;
    }

    public void setCascade(Cascade cascade) {
        this.cascade = cascade;
    }

    public enum Cascade implements HasDisplayValue {
        NO("No"), CHANGES_ONLY("Changes only"), ALL("All");

        private final String displayValue;

        Cascade(final String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String getDisplayValue() {
            return displayValue;
        }
    }
}