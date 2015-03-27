package com.emmisolutions.emmimanager.web.rest.client.model.program;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A scheduled program.
 */
@XmlRootElement(name = "program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramResource extends BaseResource<Program> {

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
