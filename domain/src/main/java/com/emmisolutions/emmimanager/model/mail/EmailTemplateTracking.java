package com.emmisolutions.emmimanager.model.mail;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Tracks when an email was sent and to whom.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "email_template_tracking", indexes = {
        @Index(name = "ix_email_template_tracking_user_client_id", columnList = "user_client_id"),
        @Index(name = "ix_email_template_tracking_email_template_id", columnList = "email_template_id"),
})
public class EmailTemplateTracking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    @Column(length = 255, nullable = false, columnDefinition = "nvarchar(255)")
    private String email;

    private boolean sent;

    private boolean viewed;

    @Column(name = "action_taken")
    private boolean actionTaken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_client_id",
            foreignKey = @ForeignKey(name = "fk_email_template_tracking_user_client"))
    private UserClient userClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id",
            foreignKey = @ForeignKey(name = "fk_email_template_tracking_client_patient"))
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_template_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_email_template_tracking_email_template"))
    private EmailTemplate emailTemplate;

    @CreatedDate
    @Column(name = "created_date", updatable = false, columnDefinition = "timestamp", nullable = false)
    private DateTime createdDate;

    private String signature;

    @Column(name = "viewed_date_utc", columnDefinition = "timestamp")
    private DateTime viewedDate;

    @Column(name = "action_taken_date_utc", columnDefinition = "timestamp")
    private DateTime actionTakenDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public DateTime getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(DateTime viewedDate) {
        this.viewedDate = viewedDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(boolean actionTaken) {
        this.actionTaken = actionTaken;
    }

    public DateTime getActionTakenDate() {
        return actionTakenDate;
    }

    public void setActionTakenDate(DateTime actionTakenDate) {
        this.actionTakenDate = actionTakenDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailTemplateTracking that = (EmailTemplateTracking) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EmailTemplateTracking{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", sent=" + sent +
                ", viewed=" + viewed +
                ", actionTaken=" + actionTaken +
                ", createdDate=" + createdDate +
                ", signature=" + signature +
                ", viewedDate=" + viewedDate +
                ", actionTakenDate=" + actionTakenDate +
                '}';
    }
}
