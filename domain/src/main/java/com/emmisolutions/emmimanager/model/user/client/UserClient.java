package com.emmisolutions.emmimanager.model.user.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /*
    The reason we do not add or use the @OneToMany side of the
    userClient --has many-> userClientUserClientRole relationship is because we
    cannot guarantee that:
            this.client === this.userClientUserClientRole.userClientRole.client

    @OneToMany(mappedBy = "userClient")
    private Collection<UserClientUserClientRole> userClientUserClientRole;

    */

}
