package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;

import java.util.List;

/**
 * HATEOAS wrapper for SecretQuestion, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "secret-question")
@XmlAccessorType(XmlAccessType.FIELD)
public class SecretQuestionResource extends ResourceSupport {

   private SecretQuestion entity;

   public SecretQuestion getEntity() {
       return entity;
   }

   public void setEntity(SecretQuestion entity) {
       this.entity = entity;
   }

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
