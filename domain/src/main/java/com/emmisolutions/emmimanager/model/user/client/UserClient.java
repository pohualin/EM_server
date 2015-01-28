package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class UserClient extends User {

    @ManyToOne
    @NotNull
    @JoinColumn(name = "client_id",
            foreignKey = @ForeignKey(name = "fk_client_id"))
    private Client client;

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientRole> clientRoles;

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientTeamRole> teamRoles;

    @Size(max = 40)
    @Column(name = "activation_key", length = 40, columnDefinition = "nvarchar(40)")
    private String activationKey;

    @Column
    private Boolean activated = false;

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

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
