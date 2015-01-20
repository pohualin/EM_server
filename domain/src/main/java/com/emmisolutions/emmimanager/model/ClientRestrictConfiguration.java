package com.emmisolutions.emmimanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

/**
 * A ClientRestrictConfiguration defined for a specific Client.
 */
@Entity
@Audited
@Table(name = "client_restrict_configuration")
public class ClientRestrictConfiguration extends AbstractAuditingEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(name = "fk_client_restrict_configuration_client"))
    private Client client;

    @NotNull
    @Column(name = "ip_config_restrict", columnDefinition = "boolean", nullable = false)
    private boolean ipConfigRestrict;

    @NotNull
    @Column(name = "email_config_restrict", columnDefinition = "boolean", nullable = false)
    private boolean emailConfigRestrict;

    /**
     * Default constructor
     */
    public ClientRestrictConfiguration() {
    }

    /**
     * Create IpRestrictConfiguration by id
     *
     * @param id
     *            to use
     */
    public ClientRestrictConfiguration(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isIpConfigRestrict() {
        return ipConfigRestrict;
    }

    public void setIpConfigRestrict(boolean ipConfigRestrict) {
        this.ipConfigRestrict = ipConfigRestrict;
    }

    public boolean isEmailConfigRestrict() {
        return emailConfigRestrict;
    }

    public void setEmailConfigRestrict(boolean emailConfigRestrict) {
        this.emailConfigRestrict = emailConfigRestrict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientRestrictConfiguration clientRestrictConfiguration = (ClientRestrictConfiguration) o;
        return !(id != null ? !id.equals(clientRestrictConfiguration.id)
                : clientRestrictConfiguration.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
