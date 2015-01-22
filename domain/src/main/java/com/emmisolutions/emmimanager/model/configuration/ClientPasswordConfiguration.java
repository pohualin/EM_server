package com.emmisolutions.emmimanager.model.configuration;

import java.io.Serializable;

import javax.persistence.Column;
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

    @NotNull
    @Column(columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @NotNull
    @Column(name = "password_expiration_days", columnDefinition = "int", nullable = false)
    private int passwordExpirationDays;

    @NotNull
    @Column(name = "password_repetitions", columnDefinition = "int", nullable = false)
    private int passwordRepetitions;

    @NotNull
    @Column(name = "days_between_password_change", columnDefinition = "int", nullable = false)
    private int daysBetweenPasswordChange;

    @NotNull
    @Column(name = "password_length", columnDefinition = "int", nullable = false)
    private int passwordLength;

    @NotNull
    @Column(name = "uppercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean uppercaseLetters;

    @NotNull
    @Column(name = "lowercase_letters", columnDefinition = "boolean", nullable = false)
    private boolean lowercaseLetters;

    @NotNull
    @Column(name = "numbers", columnDefinition = "boolean", nullable = false)
    private boolean numbers;

    @NotNull
    @Column(name = "special_chars", columnDefinition = "boolean", nullable = false)
    private boolean specialChars;

    @NotNull
    @Column(name = "password_reset", columnDefinition = "boolean", nullable = false)
    private boolean passwordReset;

    @NotNull
    @Column(name = "lockout_attempts", columnDefinition = "int", nullable = false)
    private int lockoutAttemps;

    @NotNull
    @Column(name = "lockout_reset", columnDefinition = "int", nullable = false)
    private int lockoutReset;

    @NotNull
    @Column(name = "idle_time", columnDefinition = "int", nullable = false)
    private int idleTime;

    @NotNull
    @Column(name = "password_expiration_days_reminder", columnDefinition = "int", nullable = false)
    private int passwordExpirationDaysReminder;

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

    public void setPasswordExpirationDaysReminder(
            int passwordExpirationDaysReminder) {
        this.passwordExpirationDaysReminder = passwordExpirationDaysReminder;
    }

    public boolean isPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

}
