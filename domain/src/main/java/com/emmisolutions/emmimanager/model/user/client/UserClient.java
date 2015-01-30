package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private Collection<UserClientUserClientRole> clientRoles;

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientTeamRole> teamRoles;

    @Size(max = 40)
    @Column(name = "activation_key", length = 40, columnDefinition = "nvarchar(40)")
    private String activationKey;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    public UserClient() {

    }

    public UserClient(Long id) {
        super.setId(id);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Collection<UserClientUserClientRole> getClientRoles() {
        return clientRoles;
    }

    public void setClientRoles(Collection<UserClientUserClientRole> clientRoles) {
        this.clientRoles = clientRoles;
    }

    public Collection<UserClientUserClientTeamRole> getTeamRoles() {
        return teamRoles;
    }

    public void setTeamRoles(Collection<UserClientUserClientTeamRole> teamRoles) {
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
                                    clientRole.getUserClient().getClient().getId()));
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
}
