package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The email configuration for the client's team.
 */
@Audited
@Entity
@Table(name = "client_team_email_configuration")
public class ClientTeamEmailConfiguration extends AbstractAuditingEntity {
    /**
     * Default constructor
     */
    public ClientTeamEmailConfiguration() {

    }

    /**
     * Constructor with id
     *
     * @param id the id
     */
    public ClientTeamEmailConfiguration(Long id) {
        this.id = id;
    }

    @Version
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private EmailReminderType type;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_team_id", referencedColumnName="id")
    private Team team;

    @Column(name ="rank", columnDefinition = "integer")
    private Integer rank;

    @Column(name = "email_config", columnDefinition = "boolean", nullable = false)
    private boolean emailConfig;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public EmailReminderType getType() {
        return type;
    }

    public void setType(EmailReminderType type) {
        this.type = type;
    }

    public boolean isEmailConfig() {
        return emailConfig;
    }

    public void setEmailConfig(boolean emailConfig) {
        this.emailConfig = emailConfig;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientTeamEmailConfiguration{"
                + "id=" + id
                + ", emailConfig=" + emailConfig
                + " ,team=" + team
                + ", type=" + type
                + ", reminderTwoDays=" + reminderTwoDays
                + ", reminderFourDays=" + reminderFourDays
                + ", reminderSixDays=" + reminderSixDays
                + ", reminderEightDays=" + reminderEightDays
                + ", reminderArticles=" + reminderArticles
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (this == o) {
            return true;
        }

        ClientTeamEmailConfiguration that = (ClientTeamEmailConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

}
