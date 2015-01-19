package com.emmisolutions.emmimanager.model.configuration;

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

/**
 * Password Configuration Domain Object
 */
@Audited
@Entity
@XmlRootElement(name = "password_configuration")
@Table(name = "password_configuration")
public class PasswordConfiguration extends AbstractAuditingEntity implements
        Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "password_expiration_days", columnDefinition = "int", nullable = false)
    private int passwordExpirationDays;

    @Column(name = "password_repetitions", columnDefinition = "int", nullable = false)
    private int passwordRepetitions;

    @Column(name = "days_between_password_change", columnDefinition = "int", nullable = false)
    private int daysBetweenPasswordChange;

    @Column(name = "password_length", columnDefinition = "int", nullable = false)
    private int passwordLength;

    @Column(name = "uppercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean uppercaseLetters;

    @Column(name = "lowercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean lowercaseLetters;

    @Column(name = "numbers", columnDefinition = "boolean", nullable = false)
    private boolean numbers;

    @Column(name = "special_chars", columnDefinition = "boolean", nullable = false)
    private boolean specialChars;
    
    @Column(name = "password_reset", columnDefinition = "boolean", nullable = false)
    private boolean passwordReset;

    @Column(name = "lockout_attempts", columnDefinition = "int", nullable = false)
    private int lockoutAttemps;

    @Column(name = "lockout_reset", columnDefinition = "int", nullable = false)
    private int lockoutReset;

    @Column(name = "idle_time", columnDefinition = "int", nullable = false)
    private int idleTime;

    @Column(name = "password_expiration_days_reminder", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysReminder;

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

    public int getPasswordExpirationDays() {
        return passwordExpirationDays;
    }

    public void setPasswordExpirationDays(int passwordExpirationDays) {
        this.passwordExpirationDays = passwordExpirationDays;
    }

    public int getPasswordRepetitions() {
        return passwordRepetitions;
    }

    public void setPasswordRepetitions(int passwordRepetitions) {
        this.passwordRepetitions = passwordRepetitions;
    }

    public int getDaysBetweenPasswordChange() {
        return daysBetweenPasswordChange;
    }

    public void setDaysBetweenPasswordChange(int daysBetweenPasswordChange) {
        this.daysBetweenPasswordChange = daysBetweenPasswordChange;
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public boolean hasUppercaseLetters() {
        return uppercaseLetters;
    }

    public void setUppercaseLetters(boolean uppercaseLetters) {
        this.uppercaseLetters = uppercaseLetters;
    }

    public boolean hasLowercaseLetters() {
        return lowercaseLetters;
    }

    public void setLowercaseLetters(boolean lowercaseLetters) {
        this.lowercaseLetters = lowercaseLetters;
    }

    public boolean hasNumbers() {
        return numbers;
    }

    public void setNumbers(boolean numbers) {
        this.numbers = numbers;
    }

    public boolean hasSpecialChars() {
        return specialChars;
    }

    public void setSpecialChars(boolean specialChars) {
        this.specialChars = specialChars;
    }

    public int getLockoutAttemps() {
        return lockoutAttemps;
    }

    public void setLockoutAttemps(int lockoutAttemps) {
        this.lockoutAttemps = lockoutAttemps;
    }

    public int getLockoutReset() {
        return lockoutReset;
    }

    public void setLockoutReset(int lockoutReset) {
        this.lockoutReset = lockoutReset;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getPasswordExpirationDaysReminder() {
        return passwordExpirationDaysReminder;
    }

    public void setPasswordExpirationDaysReminder(int passwordExpirationDaysReminder) {
        this.passwordExpirationDaysReminder = passwordExpirationDaysReminder;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

}
