package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * A user for a single client.
 */
@Entity
@Audited
@DiscriminatorValue("C")
public class UserClient extends User {
    
    public UserClient(){
	
    }
    
    public UserClient(Long id){
	super.setId(id);
    }
    
    @ManyToOne
    @NotNull
    @JoinColumn(name = "client_id",
        foreignKey = @ForeignKey(name = "fk_client_id"))
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientRole> clientRoles;

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientTeamRole> teamRoles;


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
}
