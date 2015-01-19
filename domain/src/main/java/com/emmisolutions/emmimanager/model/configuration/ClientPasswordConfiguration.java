package com.emmisolutions.emmimanager.model.configuration;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;

/**
 * Client Password Configuration Domain Object
 */
@Audited
@Entity
@XmlRootElement(name = "client_password_configuration")
@Table(name = "client_password_configuration")
public class ClientPasswordConfiguration extends AbstractAuditingEntity
        implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "default_password_configuration_id", nullable = false)
    private DefaultPasswordConfiguration defaultPasswordConfiguration;

    @OneToOne
    @JoinColumn(name = "password_configuration_id")
    private PasswordConfiguration passwordConfiguration;

    /**
     * Default constructor
     */
    public ClientPasswordConfiguration() {

    }

    /**
     * id constructor
     * 
     * @param id
     *            to use
     */
    public ClientPasswordConfiguration(Long id) {
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

    public DefaultPasswordConfiguration getDefaultPasswordConfiguration() {
        return defaultPasswordConfiguration;
    }

    public void setDefaultPasswordConfiguration(
            DefaultPasswordConfiguration defaultPasswordConfiguration) {
        this.defaultPasswordConfiguration = defaultPasswordConfiguration;
    }

    public PasswordConfiguration getPasswordConfiguration() {
        return passwordConfiguration;
    }

    public void setPasswordConfiguration(
            PasswordConfiguration passwordConfiguration) {
        this.passwordConfiguration = passwordConfiguration;
    }

}
