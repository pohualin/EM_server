package com.emmisolutions.emmimanager.model.configuration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;

/**
 * EmailRestrictConfiguration defined for a specific
 * ClientRestrictConfiguration.
 */
@Entity
@Audited
@Table(name = "email_restrict_configuration")
public class EmailRestrictConfiguration extends AbstractAuditingEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(name = "fk_email_restrict_configuration_client"))
    private Client client;

    @Column(name = "description", length = 255, columnDefinition = "nvarchar(255)")
    @Size(min = 0, max = 255)
    private String description;

    @NotNull
    @Column(name = "email_ending", length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @Size(min = 0, max = 255)
    private String emailEnding;

    /**
     * Default constructor
     */
    public EmailRestrictConfiguration() {
    }

    /**
     * Create IpRestrictConfiguration by id
     *
     * @param id to use
     */
    public EmailRestrictConfiguration(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailEnding() {
        return emailEnding;
    }

    public void setEmailEnding(String emailEnding) {
        this.emailEnding = emailEnding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmailRestrictConfiguration ipRestrictConfiguration = (EmailRestrictConfiguration) o;
        return !(id != null ? !id.equals(ipRestrictConfiguration.id)
                : ipRestrictConfiguration.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EmailRestrictConfiguration{" +
                "id=" + id +
                ", version=" + version +
                ", client=" + client +
                ", description='" + description + '\'' +
                ", emailEnding='" + emailEnding + '\'' +
                '}';
    }
}
