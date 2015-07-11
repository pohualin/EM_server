package com.emmisolutions.emmimanager.model.configuration.team;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;

/**
 * This model holds the global default scheduling configuration for all teams.
 * Configurations contain default, minimum and maximum values.
 * 
 * @see ClientTeamSchedulingConfiguration
 * 
 */
@Audited
@Entity
@Table(name = "default_client_team_scheduling_configuration")
@XmlRootElement(name = "default_client_team_scheduling_configuration")
public class DefaultClientTeamSchedulingConfiguration extends
        AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "default_view_by_days", columnDefinition = "int", nullable = false)
    private int defaultViewByDays;

    @Column(name = "view_by_days_min", columnDefinition = "int", nullable = false)
    private int viewByDaysMin;

    @Column(name = "view_by_days_max", columnDefinition = "int", nullable = false)
    private int viewByDaysMax;

    @Column(name = "default_use_providers", columnDefinition = "boolean", nullable = false)
    private boolean defaultUseProviders;

    @Column(name = "default_use_locations", columnDefinition = "boolean", nullable = false)
    private boolean defaultUseLocations;

    @Column(name = "default_use_view_by_days", columnDefinition = "boolean", nullable = false)
    private boolean defaultUseViewByDays;

    /**
     * Default constructor
     */
    public DefaultClientTeamSchedulingConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id
     *            to use
     */
    public DefaultClientTeamSchedulingConfiguration(Long id) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDefaultViewByDays() {
        return defaultViewByDays;
    }

    public void setDefaultViewByDays(int defaultViewByDays) {
        this.defaultViewByDays = defaultViewByDays;
    }

    public int getViewByDaysMin() {
        return viewByDaysMin;
    }

    public void setViewByDaysMin(int viewByDaysMin) {
        this.viewByDaysMin = viewByDaysMin;
    }

    public int getViewByDaysMax() {
        return viewByDaysMax;
    }

    public void setViewByDaysMax(int viewByDaysMax) {
        this.viewByDaysMax = viewByDaysMax;
    }

    public boolean isDefaultUseProviders() {
        return defaultUseProviders;
    }

    public void setDefaultUseProviders(boolean defaultUseProviders) {
        this.defaultUseProviders = defaultUseProviders;
    }

    public boolean isDefaultUseLocations() {
        return defaultUseLocations;
    }

    public void setDefaultUseLocations(boolean defaultUseLocations) {
        this.defaultUseLocations = defaultUseLocations;
    }

    public boolean isDefaultUseViewByDays() {
        return defaultUseViewByDays;
    }

    public void setDefaultUseViewByDays(boolean defaultUseViewByDays) {
        this.defaultUseViewByDays = defaultUseViewByDays;
    }

}
