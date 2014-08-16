package com.emmisolutions.emmimanager.web.rest.model.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Group;

@XmlRootElement(name = "client")
public class ClientSaveRequest {
	
	private Client client;
	
	private List<Group> groups = new ArrayList<>();	

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
