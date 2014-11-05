package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Provider at a particular Client.
 */
@Audited
@Entity
@Table(name = "client_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_client_id_provider_id",
                        columnNames = {"client_id", "provider_id"}),
                @UniqueConstraint(name = "uk_client_id_provider_external_id",
                        columnNames = {"client_id", "external_id"}),
        })
@XmlRootElement(name = "client_provider")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientProvider extends AbstractAuditingEntity {

    @Id
    @Column(name = "id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", columnDefinition = "int")
    private Integer version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_client_id"))
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_provider_id"))
    private Provider provider;

    @Length(max = 255)
    @Column(name = "external_id", length = 255, columnDefinition = "nvarchar(255)")
    private String externalId;

    public ClientProvider() {
    }

    /**
     * Create ClientProvider by id
     *
     * @param id to use
     */
    public ClientProvider(long id) {
        this.id = id;
    }

    /**
     * Create a ClientProvider from its required parts
     *
     * @param client   the client
     * @param provider the provider
     */
    public ClientProvider(Client client, Provider provider) {
        this.client = client;
        this.provider = provider;
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientProvider that = (ClientProvider) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientProvider{" +
                "id=" + id +
                ", version=" + getVersion() +
                ", client=" + client +
                ", provider=" + getProvider() +
                ", externalId='" + getExternalId() + '\'' +
                '}';
    }
}
