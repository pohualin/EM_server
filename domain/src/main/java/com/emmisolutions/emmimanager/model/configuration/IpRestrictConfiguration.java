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

/**
 * IpRestrictConfiguration defined for a specific ClientRestrictConfiguration.
 */
@Entity
@Audited
@Table(name = "ip_restrict_configuration")
public class IpRestrictConfiguration extends AbstractAuditingEntity implements
        Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;
    
    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "client_restrict_configuration_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ip_restrict_configuration_client_restrict_configuration"))
    private ClientRestrictConfiguration clientRestrictConfiguration;

    @Column(name = "description", length = 255, columnDefinition = "nvarchar(255)")
    @Size(min = 0, max = 255)
    private String description;

    @NotNull
    @Column(name = "ip_range_start", length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @Size(min = 0, max = 255)
    private String ipRangeStart;

    @NotNull
    @Column(name = "ip_range_end", length = 255, columnDefinition = "nvarchar(255)", nullable = false)
    @Size(min = 0, max = 255)
    private String ipRangeEnd;

    /**
     * Default constructor
     */
    public IpRestrictConfiguration() {
    }

    /**
     * Create IpRestrictConfiguration by id
     *
     * @param id
     *            to use
     */
    public IpRestrictConfiguration(Long id) {
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

    public ClientRestrictConfiguration getClientRestrictConfiguration() {
        return clientRestrictConfiguration;
    }

    public void setClientRestrictConfiguration(
            ClientRestrictConfiguration clientRestrictConfiguration) {
        this.clientRestrictConfiguration = clientRestrictConfiguration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpRangeStart() {
        return ipRangeStart;
    }

    public void setIpRangeStart(String ipRangeStart) {
        this.ipRangeStart = ipRangeStart;
    }

    public String getIpRangeEnd() {
        return ipRangeEnd;
    }

    public void setIpRangeEnd(String ipRangeEnd) {
        this.ipRangeEnd = ipRangeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        IpRestrictConfiguration ipRestrictConfiguration = (IpRestrictConfiguration) o;
        return !(id != null ? !id.equals(ipRestrictConfiguration.id)
                : ipRestrictConfiguration.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
