import React from 'react';
import PropTypes from 'prop-types';

import { compose } from 'recompose';
import { connect } from 'react-redux';

import { Message, Form } from 'semantic-ui-react';

import { Toggle, InputField } from 'react-semantic-redux-form';

import { Field } from 'redux-form';

import { getActualValue } from './elementDetailsUtils';

import NumericInput from 'prototypes/NumericInput';

const ElementFieldType = ({
  name, type, value, defaultValue,
}) => {
  let actualValue;
  switch (type) {
    case 'boolean':
      actualValue = getActualValue(value, defaultValue, 'boolean') === 'true';
      return (
        <Field
          label={name}
          name={name}
          component={Toggle}
          value={actualValue}
          checked={actualValue}
        />
      );
    case 'int':
      actualValue = parseInt(getActualValue(value, defaultValue, 'integer'), 10);
      return (
        <Field
          name={name}
          value={actualValue}
          component={(props) => {
            console.log({ props });
            return <NumericInput {...props.input} />;
          }}
        />
      );
    case 'String':
    case 'DocRef':
    case 'PipelineReference':
      actualValue = getActualValue(value, defaultValue, 'string');
      return <Field name={name} component={InputField} value={actualValue} />;
    default:
      actualValue = getActualValue(value, defaultValue, 'string');
      return <Field name={name} component={InputField} value={actualValue} />;
  }
};

const ElementField = ({
  name, description, type, defaultValue, value,
}) => (
  <Form.Group>
    <Form.Field className="element-details__field">
      <label>{name}</label>
      <ElementFieldType name={name} type={type} value={value} defaultValue={defaultValue} />
    </Form.Field>
    <Message className="element-details__property-message">
      <Message.Header>{description}</Message.Header>
      {defaultValue ? (
        <p>The default value is '{defaultValue}'.</p>
      ) : (
        <p>This property does not have a default value.</p>
      )}
    </Message>
  </Form.Group>
);

ElementField.propTypes = {
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  type: PropTypes.string.isRequired,
  defaultValue: PropTypes.number.isRequired,
  value: PropTypes.object,
};

export default compose(connect(
  state => ({
    // state
  }),
  {
    // actions
  },
))(ElementField);