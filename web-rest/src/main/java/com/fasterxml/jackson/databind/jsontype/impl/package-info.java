/**
 * These are monkey patch classes necessary to allow for serialization/deserialization
 * of inheritance hierarchies using the JsonType.As.EXISTING_PROPERTY
 * <p/>
 * https://github.com/FasterXML/jackson-databind/issues/528
 *
 * @see com.emmisolutions.emmimanager.model.salesforce.CaseField
 */
package com.fasterxml.jackson.databind.jsontype.impl;