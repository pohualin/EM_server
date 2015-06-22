package com.emmisolutions.emmimanager.web.rest.admin.model.client.note;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

/**
 * HATEOAS wrapper for ClientNote, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "client-note")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientNoteResource extends BaseResource<ClientNote> {
}
