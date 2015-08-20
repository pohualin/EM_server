package com.emmisolutions.emmimanager.model.configuration;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * This model holds the global default password configuration for all clients.
 * Configurations contain default, minimum and maximum values.
 * 
 * @see ClientPasswordConfiguration
 * 
 */
@Audited
@Entity
@Table(name = "default_password_configuration")
@XmlRootElement(name = "default_password_configuration")
public class DefaultPasswordConfiguration extends AbstractAuditingEntity
        implements Serializable {

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

    @Column(name = "default_password_expiration_days", columnDefinition = "int", nullable = false)
    private int defaultPasswordExpirationDays;

    @Column(name = "password_expiration_days_min", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysMin;

    @Column(name = "password_expiration_days_max", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysMax;

    @Column(name = "default_password_repetitions", columnDefinition = "int", nullable = false)
    private int defaultPasswordRepetitions;

    @Column(name = "password_repetitions_min", columnDefinition = "int", nullable = false)
    private int passwordRepetitionsMin;

    @Column(name = "password_repetitions_max", columnDefinition = "int", nullable = false)
    private int passwordRepetitionsMax;

    @Column(name = "default_days_between_password_change", columnDefinition = "int", nullable = false)
    private int defaultDaysBetweenPasswordChange;

    @Column(name = "days_between_password_change_min", columnDefinition = "int", nullable = false)
    private int daysBetweenPasswordChangeMin;

    @Column(name = "days_between_password_change_max", columnDefinition = "int", nullable = false)
    private int daysBetweenPasswordChangeMax;

    @Column(name = "default_password_length", columnDefinition = "int", nullable = false)
    private int defaultPasswordLength;

    @Column(name = "password_length_min", columnDefinition = "int", nullable = false)
    private int passwordLengthMin;

    @Column(name = "password_length_max", columnDefinition = "int", nullable = false)
    private int passwordLengthMax;

    @Column(name = "default_uppercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean defaultUppercaseLetters;

    @Column(name = "uppercase_letters_changeable", columnDefinition = "boolean", nullable = false)
    private boolean uppercaseLettersChangeable;

    @Column(name = "default_lowercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean defaultLowercaseLetters;

    @Column(name = "lowercase_letters_changeable", columnDefinition = "boolean", nullable = false)
    private boolean lowercaseLettersChangeable;

    @Column(name = "default_numbers", columnDefinition = "boolean", nullable = false)
    private boolean defaultNumbers;

    @Column(name = "numbers_changeable", columnDefinition = "boolean", nullable = false)
    private boolean numbersChangeable;

    @Column(name = "default_special_chars", columnDefinition = "boolean", nullable = false)
    private boolean defaultSpecialChars;

    @Column(name = "special_chars_changeable", columnDefinition = "boolean", nullable = false)
    private boolean specialCharsChangeable;

    @Column(name = "default_password_reset", columnDefinition = "boolean", nullable = false)
    private boolean defaultPasswordReset;

    @Column(name = "password_reset_changeable", columnDefinition = "boolean", nullable = false)
    private boolean passwordResetChangeable;

    @Column(name = "default_lockout_attempts", columnDefinition = "int", nullable = false)
    private int defaultLockoutAttemps;

    @Column(name = "lockout_attempts_min", columnDefinition = "int", nullable = false)
    private int lockoutAttempsMin;

    @Column(name = "lockout_attempts_max", columnDefinition = "int", nullable = false)
    private int lockoutAttempsMax;

    @Column(name = "default_lockout_reset", columnDefinition = "int", nullable = false)
    private int defaultLockoutReset;

    @Column(name = "lockout_reset_min", columnDefinition = "int", nullable = false)
    private int lockoutResetMin;

    @Column(name = "lockout_reset_max", columnDefinition = "int", nullable = false)
    private int lockoutResetMax;

    @Column(name = "default_idle_time", columnDefinition = "int", nullable = false)
    private int defaultIdleTime;

    @Column(name = "idle_time_min", columnDefinition = "int", nullable = false)
    private int idleTimeMin;

    @Column(name = "idle_time_max", columnDefinition = "int", nullable = false)
    private int idleTimeMax;

    @Column(name = "default_password_expiration_days_reminder", columnDefinition = "int", nullable = false)
    private int defaultPasswordExpirationDaysReminder;

    @Column(name = "password_expiration_days_reminder_min", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysReminderMin;

    @Column(name = "password_expiration_days_reminder_max", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysReminderMax;

    /**
     * Default constructor
     */
    public DefaultPasswordConfiguration() {

    }

    /**
     * ID constructor
     *
     * @param id
     *            to use
     */
    public DefaultPasswordConfiguration(Long id) {
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

    public int getDefaultPasswordExpirationDays() {
        return defaultPasswordExpirationDays;
    }

    public void setDefaultPasswordExpirationDays(
            int defaultPasswordExpirationDays) {
        this.defaultPasswordExpirationDays = defaultPasswordExpirationDays;
    }

    public int getPasswordExpirationDaysMin() {
        return passwordExpirationDaysMin;
    }

    public void setPasswordExpirationDaysMin(int passwordExpirationDaysMin) {
        this.passwordExpirationDaysMin = passwordExpirationDaysMin;
    }

    public int getPasswordExpirationDaysMax() {
        return passwordExpirationDaysMax;
    }

    public void setPasswordExpirationDaysMax(int passwordExpirationDaysMax) {
        this.passwordExpirationDaysMax = passwordExpirationDaysMax;
    }

    public int getDefaultPasswordRepetitions() {
        return defaultPasswordRepetitions;
    }

    public void setDefaultPasswordRepetitions(int defaultPasswordRepetitions) {
        this.defaultPasswordRepetitions = defaultPasswordRepetitions;
    }

    public int getPasswordRepetitionsMin() {
        return passwordRepetitionsMin;
    }

    public void setPasswordRepetitionsMin(int passwordRepetitionsMin) {
        this.passwordRepetitionsMin = passwordRepetitionsMin;
    }

    public int getPasswordRepetitionsMax() {
        return passwordRepetitionsMax;
    }

    public void setPasswordRepetitionsMax(int passwordRepetitionsMax) {
        this.passwordRepetitionsMax = passwordRepetitionsMax;
    }

    public int getDefaultDaysBetweenPasswordChange() {
        return defaultDaysBetweenPasswordChange;
    }

    public void setDefaultDaysBetweenPasswordChange(
            int defaultDaysBetweenPasswordChange) {
        this.defaultDaysBetweenPasswordChange = defaultDaysBetweenPasswordChange;
    }

    public int getDaysBetweenPasswordChangeMin() {
        return daysBetweenPasswordChangeMin;
    }

    public void setDaysBetweenPasswordChangeMin(int daysBetweenPasswordChangeMin) {
        this.daysBetweenPasswordChangeMin = daysBetweenPasswordChangeMin;
    }

    public int getDaysBetweenPasswordChangeMax() {
        return daysBetweenPasswordChangeMax;
    }

    public void setDaysBetweenPasswordChangeMax(int daysBetweenPasswordChangeMax) {
        this.daysBetweenPasswordChangeMax = daysBetweenPasswordChangeMax;
    }

    public int getDefaultPasswordLength() {
        return defaultPasswordLength;
    }

    public void setDefaultPasswordLength(int defaultPasswordLength) {
        this.defaultPasswordLength = defaultPasswordLength;
    }

    public int getPasswordLengthMin() {
        return passwordLengthMin;
    }

    public void setPasswordLengthMin(int passwordLengthMin) {
        this.passwordLengthMin = passwordLengthMin;
    }

    public int getPasswordLengthMax() {
        return passwordLengthMax;
    }

    public void setPasswordLengthMax(int passwordLengthMax) {
        this.passwordLengthMax = passwordLengthMax;
    }

    public boolean hasDefaultUppercaseLetters() {
        return defaultUppercaseLetters;
    }

    public void setDefaultUppercaseLetters(boolean defaultUppercaseLetters) {
        this.defaultUppercaseLetters = defaultUppercaseLetters;
    }

    public boolean isUppercaseLettersChangeable() {
        return uppercaseLettersChangeable;
    }

    public void setUppercaseLettersChangeable(boolean uppercaseLettersChangeable) {
        this.uppercaseLettersChangeable = uppercaseLettersChangeable;
    }

    public boolean hasDefaultLowercaseLetters() {
        return defaultLowercaseLetters;
    }

    public void setDefaultLowercaseLetters(boolean defaultLowercaseLetters) {
        this.defaultLowercaseLetters = defaultLowercaseLetters;
    }

    public boolean isLowercaseLettersChangeable() {
        return lowercaseLettersChangeable;
    }

    public void setLowercaseLettersChangeable(boolean lowercaseLettersChangeable) {
        this.lowercaseLettersChangeable = lowercaseLettersChangeable;
    }

    public boolean hasDefaultNumbers() {
        return defaultNumbers;
    }

    public void setDefaultNumbers(boolean defaultNumbers) {
        this.defaultNumbers = defaultNumbers;
    }

    public boolean isNumbersChangeable() {
        return numbersChangeable;
    }

    public void setNumbersChangeable(boolean numbersChangeable) {
        this.numbersChangeable = numbersChangeable;
    }

    public boolean hasDefaultSpecialChars() {
        return defaultSpecialChars;
    }

    public void setDefaultSpecialChars(boolean defaultSpecialChars) {
        this.defaultSpecialChars = defaultSpecialChars;
    }

    public boolean isSpecialCharsChangeable() {
        return specialCharsChangeable;
    }

    public void setSpecialCharsChangeable(boolean specialCharsChangeable) {
        this.specialCharsChangeable = specialCharsChangeable;
    }

    public int getDefaultLockoutAttemps() {
        return defaultLockoutAttemps;
    }

    public void setDefaultLockoutAttemps(int defaultLockoutAttemps) {
        this.defaultLockoutAttemps = defaultLockoutAttemps;
    }

    public int getLockoutAttempsMin() {
        return lockoutAttempsMin;
    }

    public void setLockoutAttempsMin(int lockoutAttempsMin) {
        this.lockoutAttempsMin = lockoutAttempsMin;
    }

    public int getLockoutAttempsMax() {
        return lockoutAttempsMax;
    }

    public void setLockoutAttempsMax(int lockoutAttempsMax) {
        this.lockoutAttempsMax = lockoutAttempsMax;
    }

    public int getDefaultLockoutReset() {
        return defaultLockoutReset;
    }

    public void setDefaultLockoutReset(int defaultLockoutReset) {
        this.defaultLockoutReset = defaultLockoutReset;
    }

    public int getLockoutResetMin() {
        return lockoutResetMin;
    }

    public void setLockoutResetMin(int lockoutResetMin) {
        this.lockoutResetMin = lockoutResetMin;
    }

    public int getLockoutResetMax() {
        return lockoutResetMax;
    }

    public void setLockoutResetMax(int lockoutResetMax) {
        this.lockoutResetMax = lockoutResetMax;
    }

    public int getDefaultIdleTime() {
        return defaultIdleTime;
    }

    public void setDefaultIdleTime(int defaultIdleTime) {
        this.defaultIdleTime = defaultIdleTime;
    }

    public int getIdleTimeMin() {
        return idleTimeMin;
    }

    public void setIdleTimeMin(int idleTimeMin) {
        this.idleTimeMin = idleTimeMin;
    }

    public int getIdleTimeMax() {
        return idleTimeMax;
    }

    public void setIdleTimeMax(int idleTimeMax) {
        this.idleTimeMax = idleTimeMax;
    }

    public int getDefaultPasswordExpirationDaysReminder() {
        return defaultPasswordExpirationDaysReminder;
    }

    public void setDefaultPasswordExpirationDaysReminder(
            int defaultPasswordExpirationDaysReminder) {
        this.defaultPasswordExpirationDaysReminder = defaultPasswordExpirationDaysReminder;
    }

    public int getPasswordExpirationDaysReminderMin() {
        return passwordExpirationDaysReminderMin;
    }

    public void setPasswordExpirationDaysReminderMin(
            int passwordExpirationDaysReminderMin) {
        this.passwordExpirationDaysReminderMin = passwordExpirationDaysReminderMin;
    }

    public int getPasswordExpirationDaysReminderMax() {
        return passwordExpirationDaysReminderMax;
    }

    public void setPasswordExpirationDaysReminderMax(
            int passwordExpirationDaysReminderMax) {
        this.passwordExpirationDaysReminderMax = passwordExpirationDaysReminderMax;
    }

    public boolean isDefaultPasswordReset() {
        return defaultPasswordReset;
    }

    public void setDefaultPasswordReset(boolean defaultPasswordReset) {
        this.defaultPasswordReset = defaultPasswordReset;
    }

    public boolean isPasswordResetChangeable() {
        return passwordResetChangeable;
    }

    public void setPasswordResetChangeable(boolean passwordResetChangeable) {
        this.passwordResetChangeable = passwordResetChangeable;
    }

    @Override
    public String toString() {
        return "DefaultPasswordConfiguration{" +
                "active=" + active +
                ", id=" + id +
                ", version=" + version +
                ", name='" + name + '\'' +
                ", defaultPasswordExpirationDays=" + defaultPasswordExpirationDays +
                ", passwordExpirationDaysMin=" + passwordExpirationDaysMin +
                ", passwordExpirationDaysMax=" + passwordExpirationDaysMax +
                ", defaultPasswordRepetitions=" + defaultPasswordRepetitions +
                ", passwordRepetitionsMin=" + passwordRepetitionsMin +
                ", passwordRepetitionsMax=" + passwordRepetitionsMax +
                ", defaultDaysBetweenPasswordChange=" + defaultDaysBetweenPasswordChange +
                ", daysBetweenPasswordChangeMin=" + daysBetweenPasswordChangeMin +
                ", daysBetweenPasswordChangeMax=" + daysBetweenPasswordChangeMax +
                ", defaultPasswordLength=" + defaultPasswordLength +
                ", passwordLengthMin=" + passwordLengthMin +
                ", passwordLengthMax=" + passwordLengthMax +
                ", defaultUppercaseLetters=" + defaultUppercaseLetters +
                ", uppercaseLettersChangeable=" + uppercaseLettersChangeable +
                ", defaultLowercaseLetters=" + defaultLowercaseLetters +
                ", lowercaseLettersChangeable=" + lowercaseLettersChangeable +
                ", defaultNumbers=" + defaultNumbers +
                ", numbersChangeable=" + numbersChangeable +
                ", defaultSpecialChars=" + defaultSpecialChars +
                ", specialCharsChangeable=" + specialCharsChangeable +
                ", defaultPasswordReset=" + defaultPasswordReset +
                ", passwordResetChangeable=" + passwordResetChangeable +
                ", defaultLockoutAttemps=" + defaultLockoutAttemps +
                ", lockoutAttempsMin=" + lockoutAttempsMin +
                ", lockoutAttempsMax=" + lockoutAttempsMax +
                ", defaultLockoutReset=" + defaultLockoutReset +
                ", lockoutResetMin=" + lockoutResetMin +
                ", lockoutResetMax=" + lockoutResetMax +
                ", defaultIdleTime=" + defaultIdleTime +
                ", idleTimeMin=" + idleTimeMin +
                ", idleTimeMax=" + idleTimeMax +
                ", defaultPasswordExpirationDaysReminder=" + defaultPasswordExpirationDaysReminder +
                ", passwordExpirationDaysReminderMin=" + passwordExpirationDaysReminderMin +
                ", passwordExpirationDaysReminderMax=" + passwordExpirationDaysReminderMax +
                '}';
    }
}
