package com.emmisolutions.emmimanager.web.rest.model.client;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "client")
public class ClientSaveRequest {
	
	private Client client;
	
	@XmlElement(name = "group")
	@XmlElementWrapper(name = "groups")
	@JsonProperty("group")
	private List<Group> groups;	

	
	public ClientSaveRequest(Client client, List<Group> groups){
		this.client = client;
		this.groups = groups;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
}
