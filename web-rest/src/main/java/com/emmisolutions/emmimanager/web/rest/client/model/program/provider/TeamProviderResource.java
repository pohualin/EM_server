package com.emmisolutions.emmimanager.web.rest.client.model.program.provider;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * A team provider.
 */
@XmlRootElement(name = "team-provider")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamProviderResource extends BaseResource<TeamProvider> {

    @SuppressWarnings("unused")
    private String name;

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

    @Override
    public void setEntity(TeamProvider entity) {
        super.setEntity(entity);
        // write the 'name' of the provider
        StringBuilder sb = new StringBuilder();
        Provider provider = entity.getProvider();
        if (provider != null) {
            if (StringUtils.isNotBlank(provider.getFirstName())) {
                sb.append(StringUtils.trimToEmpty(provider.getFirstName()));
                sb.append(" ");
            }
            if (StringUtils.isNotBlank(provider.getMiddleName())) {
                sb.append(StringUtils.trimToEmpty(provider.getMiddleName()));
                sb.append(" ");
            }
            if (StringUtils.isNotBlank(provider.getLastName())) {
                sb.append(StringUtils.trimToEmpty(provider.getLastName()));
            }
            this.name = StringUtils.trim(sb.toString());
        }
    }
}
