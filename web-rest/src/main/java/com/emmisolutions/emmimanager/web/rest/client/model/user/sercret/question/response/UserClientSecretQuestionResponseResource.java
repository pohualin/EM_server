package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;

import java.util.List;

/**
 * HATEOAS wrapper for UserClientSecretQuestion
 */
@XmlRootElement(name = "secretQuestionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSecretQuestionResponseResource extends ResourceSupport {

   private UserClientSecretQuestionResponse entity;

   public UserClientSecretQuestionResponse getEntity() {
       return entity;
   }

   public void setEntity(UserClientSecretQuestionResponse entity) {
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
