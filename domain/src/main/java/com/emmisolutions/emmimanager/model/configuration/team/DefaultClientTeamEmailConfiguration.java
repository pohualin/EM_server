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

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;

/**
 * The default team email configuration.
 */
@Entity
@Table(name = "default_team_email_configuration")
@XmlRootElement(name = "default_team_email_configuration")
public class DefaultClientTeamEmailConfiguration extends AbstractAuditingEntity
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "collect_email", columnDefinition = "boolean", nullable = false)
    private boolean collectEmail;

    @Column(name = "require_email", columnDefinition = "boolean", nullable = false)
    private boolean requireEmail;

    @Column(name = "reminder_two_days", columnDefinition = "boolean", nullable = false)
    private boolean reminderTwoDays;

    @Column(name = "reminder_four_days", columnDefinition = "boolean", nullable = false)
    private boolean reminderFourDays;

    @Column(name = "reminder_six_days", columnDefinition = "boolean", nullable = false)
    private boolean reminderSixDays;

    @Column(name = "reminder_eight_days", columnDefinition = "boolean", nullable = false)
    private boolean reminderEightDays;

    @Column(name = "reminder_articles", columnDefinition = "boolean", nullable = false)
    private boolean reminderArticles;

    
    /**
     * Default constructor
     */
    public DefaultClientTeamEmailConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id to use
     */
    public DefaultClientTeamEmailConfiguration(Long id) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getCollectEmail() {
        return collectEmail;
    }

    public void setCollectEmail(boolean collectEmail) {
        this.collectEmail = collectEmail;
    }

    public boolean getRequireEmail() {
        return requireEmail;
    }

    public void setRequireEmail(boolean requireEmail) {
        this.requireEmail = requireEmail;
    }

    public boolean getReminderTwoDays() {
        return reminderTwoDays;
    }

    public void setReminderTwoDays(boolean reminderTwoDays) {
        this.reminderTwoDays = reminderTwoDays;
    }

    public boolean getReminderFourDays() {
        return reminderFourDays;
    }

    public void setReminderFourDays(boolean reminderFourDays) {
        this.reminderFourDays = reminderFourDays;
    }

    public boolean getReminderSixDays() {
        return reminderSixDays;
    }

    public void setReminderSixDays(boolean reminderSixDays) {
        this.reminderSixDays = reminderSixDays;
    }

    public boolean getReminderEightDays() {
        return reminderEightDays;
    }

    public void setReminderEightDays(boolean reminderEightDays) {
        this.reminderEightDays = reminderEightDays;
    }

    public boolean getReminderArticles() {
        return reminderArticles;
    }

    public void setReminderArticles(boolean reminderArticles) {
        this.reminderArticles = reminderArticles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultClientTeamEmailConfiguration that = (DefaultClientTeamEmailConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DefaultClientTeamEmailConfiguration{" + "id=" + id
            + ", version=" + version
            + ", active=" + active
            + ", collectEmail=" + collectEmail
            + ", requireEmail=" + requireEmail
            + ", reminderTwoDays=" + reminderTwoDays
            + ", reminderFourDays=" + reminderFourDays
            + ", reminderSixDays=" + reminderSixDays
            + ", reminderEightDays=" + reminderEightDays
            + ", reminderArticles=" + reminderArticles
            + "}";
    }
}
