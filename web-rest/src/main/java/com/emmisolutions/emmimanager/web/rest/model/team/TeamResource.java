package com.emmisolutions.emmimanager.web.rest.model.team;

import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.model.BaseResource;

/**
 * A HATEOAS wrapper for a Team entity.
 */
@XmlRootElement(name = "team")
public class TeamResource extends BaseResource<Team> {

}