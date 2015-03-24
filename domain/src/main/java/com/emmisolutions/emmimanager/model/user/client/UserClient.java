package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * A user for a single client.
 */
@Entity
@Audited
@DiscriminatorValue("C")
@Table(name = "user_client",
        indexes = {
                @Index(name = "ix_user_client_email", columnList = "email", unique = true)
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"login"}, name = "uk_user_client_login")
        }
)
public class UserClient extends User {

    @NotNull
    @Size(min = 0, max = 255)
    @Column(length = 255, nullable = false, columnDefinition = "nvarchar(255)")
    private String login;

    @Column(length = 40, columnDefinition = "varchar(40)")
    @Size(min = 0, max = 40)
    @XmlTransient
    private String password;

    @Column(length = 32, columnDefinition = "varchar(32)")
    @Size(min = 0, max = 32)
    @XmlTransient
    private String salt;

    @Email
    @Size(min = 0, max = 255)
    @Column(length = 255, columnDefinition = "nvarchar(255)")
    private String email;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "client_id",
            foreignKey = @ForeignKey(name = "fk_user_client_client"))
    private Client client;

    @OneToMany(mappedBy = "userClient")
    @XmlTransient
    private Set<UserClientUserClientRole> clientRoles;

    @OneToMany(mappedBy = "userClient")
    @XmlTransient
    private Set<UserClientUserClientTeamRole> teamRoles;

    @Size(max = 40)
    @Column(name = "activation_key", length = 40, columnDefinition = "nvarchar(40)")
    private String activationKey;

    @Size(max = 40)
    @Column(name = "password_reset_token", length = 40, columnDefinition = "nvarchar(40)")
    private String passwordResetToken;

    @Size(max = 40)
    @Column(name = "validation_token", length = 40, columnDefinition = "nvarchar(40)")
    private String validationToken;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Column(name = "never_logged_in")
    private boolean neverLoggedIn = true;

    @Column(name = "email_validated")
    private boolean emailValidated;
    
    @Column(name = "secret_question_created")
    private boolean secretQuestionCreated;

    @Column(name = "password_reset_expiration_time_utc")
    private LocalDateTime passwordResetExpirationDateTime;

    @Column(name = "activation_expiration_time_utc")
    private LocalDateTime activationExpirationDateTime;
    
    @Column(name = "password_expiration_time_utc")
    private LocalDateTime passwordExpirationDateTime;
    
    @Column(name = "password_saved_time_utc")
    private LocalDateTime passwordSavedDateTime;

    @Column(name = "validation_expiration_time_utc")
    private LocalDateTime validationExpirationDateTime;

    @Transient
    private boolean impersonated;

    @Column(name = "login_failure_count")
    private int loginFailureCount;

    @Column(name = "lock_expiration_time_utc")
    private LocalDateTime lockExpirationDateTime;

    public UserClient() {

    }

    /**
     * Creates a client by id
     *
     * @param id the id
     */
    public UserClient(Long id) {
        super.setId(id);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<UserClientUserClientRole> getClientRoles() {
        return clientRoles;
    }

    public void setClientRoles(Set<UserClientUserClientRole> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public Set<UserClientUserClientTeamRole> getTeamRoles() {
        return teamRoles;
    }

    public void setTeamRoles(Set<UserClientUserClientTeamRole> teamRoles) {
        this.teamRoles = teamRoles;
    }

    private transient volatile List<GrantedAuthority> authorities;

    /**
     * The client level authorities are in the form of:
     * PERM_CLIENT_LEVEL_PERMISSION_NAME_XX where XX is the Client Id for which the permission is valid.
     * <p/>
     * The team level authorities are in the form of:
     * PERM_TEAM_LEVEL_PERMISSION_NAME_XX where XX is the Team Id for which the permission is valid.
     *
     * @return a collection of GrantedAuthority objects
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (UserClientUserClientRole clientRole : getClientRoles()) {
                for (UserClientPermission permission : clientRole.getUserClientRole().getUserClientPermissions()) {
                    authorityList.add(
                            new SimpleGrantedAuthority(permission.getName().toString() + "_" +
                                    clientRole.getUserClientRole().getClient().getId()));
                }
            }
            // granted team authorities are in the form of PERM_NAMEOFPERMISSION_TEAMID
            for (UserClientUserClientTeamRole teamRole : getTeamRoles()) {
                for (UserClientTeamPermission permission : teamRole.getUserClientTeamRole().getUserClientTeamPermissions()) {
                    authorityList.add(new SimpleGrantedAuthority(permission.getName().toString() + "_" +
                            teamRole.getTeam().getId()));
                }
            }
            authorities = Collections.unmodifiableList(authorityList);
        }
        return authorities;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isNeverLoggedIn() {
        return neverLoggedIn;
    }

    public void setNeverLoggedIn(boolean neverLoggedIn) {
        this.neverLoggedIn = neverLoggedIn;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public boolean isEmailValidated() {
        return emailValidated;
    }

    public void setEmailValidated(boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public boolean isSecretQuestionCreated() {
		return secretQuestionCreated;
	}

	public void setSecretQuestionCreated(boolean secretQuestionCreated) {
		this.secretQuestionCreated = secretQuestionCreated;
	}
	
    public LocalDateTime getPasswordResetExpirationDateTime() {
        return passwordResetExpirationDateTime;
    }

    public void setPasswordResetExpirationDateTime(LocalDateTime passwordResetExpirationDateTime) {
        this.passwordResetExpirationDateTime = passwordResetExpirationDateTime;
    }

    public LocalDateTime getActivationExpirationDateTime() {
        return activationExpirationDateTime;
    }

    public void setActivationExpirationDateTime(LocalDateTime activationExpirationDateTime) {
        this.activationExpirationDateTime = activationExpirationDateTime;
    }

    public LocalDateTime getPasswordExpireationDateTime() {
        return passwordExpirationDateTime;
    }

    public void setPasswordExpireationDateTime(
            LocalDateTime passwordExpirationDateTime) {
        this.passwordExpirationDateTime = passwordExpirationDateTime;
    }

    public LocalDateTime getPasswordSavedDateTime() {
        return passwordSavedDateTime;
    }

    public void setPasswordSavedDateTime(LocalDateTime passwordSavedDateTime) {
        this.passwordSavedDateTime = passwordSavedDateTime;
    }

    public boolean isImpersonated() {
        return impersonated;
    }

    public void setImpersonated(boolean impersonated) {
        this.impersonated = impersonated;
    }

    public int getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(int loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public LocalDateTime getLockExpirationDateTime() {
        return lockExpirationDateTime;
    }

    public void setLockExpirationDateTime(LocalDateTime lockExpirationDateTime) {
        this.lockExpirationDateTime = lockExpirationDateTime;
    }

    public LocalDateTime getValidationExpirationDateTime() {
        return validationExpirationDateTime;
    }

    public void setValidationExpirationDateTime(LocalDateTime validationExpirationDateTime) {
        this.validationExpirationDateTime = validationExpirationDateTime;
    }

    public String getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(String validationToken) {
        this.validationToken = validationToken;
    }
}
