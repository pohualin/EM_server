package com.emmisolutions.emmimanager.web.rest.client.model.schedule;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A scheduled program.
 */
@XmlRootElement(name = "scheduled-program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScheduledProgramResource extends BaseResource<ScheduledProgram> {

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
