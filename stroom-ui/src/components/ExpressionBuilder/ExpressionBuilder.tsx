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
import * as React from "react";
import {
  compose,
  withState,
  branch,
  renderComponent,
  withProps,
  lifecycle
} from "recompose";
import { connect } from "react-redux";

import Loader from "../Loader";
import ExpressionOperator from "./ExpressionOperator";
import ROExpressionOperator from "./ROExpressionOperator";
import { LineContainer } from "../LineTo";
import DeleteExpressionItem from "./DeleteExpressionItem";
import lineElementCreators from "./expressionLineCreators";
import { GlobalStoreState } from "../../startup/reducers";
import { DataSourceType } from "../../types";
import { StoreStateById } from "./redux";
import ROExpressionBuilder from "./ROExpressionBuilder";

const withSetEditableByUser = withState(
  "inEditMode",
  "setEditableByUser",
  false
);

export interface Props {
  dataSource: DataSourceType;
  expressionId: string;
  showModeToggle: boolean;
  editMode?: boolean;
}

interface ConnectState {
  expressionState: StoreStateById;
}
interface ConnectDispatch {}
interface WithState {
  inEditMode: boolean;
}
interface WithStateHandlers {
  setEditableByUser: (v: boolean) => void;
}

export interface EnhancedProps
  extends Props,
    ConnectState,
    ConnectDispatch,
    WithState,
    WithStateHandlers {}

const enhance = compose<EnhancedProps, Props>(
  connect<ConnectState, ConnectDispatch, Props, GlobalStoreState>(
    ({ expressionBuilder }, { expressionId }) => ({
      expressionState: expressionBuilder[expressionId]
    }),
    {
      // actions
    }
  ),
  withSetEditableByUser,
  lifecycle<
    Props & ConnectState & ConnectDispatch & WithState & WithStateHandlers,
    {}
  >({
    componentDidMount() {
      this.props.setEditableByUser(!!this.props.editMode);
    }
  }),
  branch(
    ({ expressionState }) => !expressionState,
    renderComponent(() => <Loader message="Loading expression state..." />)
  ),
  branch<Props & ConnectState & ConnectDispatch & WithState>(
    ({ inEditMode }) => inEditMode,
    renderComponent<Props & ConnectState & ConnectDispatch & WithState>(
      ({ expressionId, expressionState: { expression } }) => (
        <ROExpressionBuilder
          expressionId={expressionId}
          expression={expression}
        />
      )
    )
  ),
  withProps(({ showModeToggle, dataSource }) => ({
    showModeToggle: showModeToggle && !!dataSource
  }))
);

const ExpressionBuilder = ({
  expressionId,
  dataSource,
  expressionState: { expression },
  showModeToggle,
  inEditMode,
  setEditableByUser
}: EnhancedProps) => (
  <LineContainer
    className="Expression-editor__graph"
    lineContextId={`expression-lines-${expressionId}`}
    lineElementCreators={lineElementCreators}
  >
    <DeleteExpressionItem expressionId={expressionId} />
    {showModeToggle ? (
      <React.Fragment>
        <label>Edit Mode</label>
        <input
          type="checkbox"
          checked={inEditMode}
          onChange={() => setEditableByUser(!inEditMode)}
        />
      </React.Fragment>
    ) : (
      undefined
    )}
    <ExpressionOperator
      dataSource={dataSource}
      expressionId={expressionId}
      isRoot
      isEnabled
      operator={expression}
    />
  </LineContainer>
);

export default enhance(ExpressionBuilder);