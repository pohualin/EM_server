/**
 * Custom JAXB Adapter classes for non-standard types. (e.g. Joda types).
 *
 * To use these check out {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter} and
 * {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters}. Essentially
 * you'll need to annotate one of the following:
 *  <ul>
 *   <li> a JavaBean property </li>
 *   <li> field </li>
 *   <li> parameter </li>
 *   <li> package </li>
 *   <li> from within {@link javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters} </li>
 * </ul>
 *
 * An example of this is the {@link com.emmisolutions.emmimanager.model} package info.
 */
package com.emmisolutions.emmimanager.jaxb;