package com.emmisolutions.emmimanager.model.configuration;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;

/**
 * ClientContentSubscriptionConfiguration Domain Object
 * 
 */
@Audited
@Entity
@XmlRootElement(name = "client_content_subscription_configuration")
@Table(name = "client_content_subscription_configuration",
uniqueConstraints =
@UniqueConstraint(columnNames = {"client_id", "content_subscription_id"}, name = "uk_client_content_subscription"))
public class ClientContentSubscriptionConfiguration extends AbstractAuditingEntity
        implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

   	@NotNull
   	@ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_subscription_id", columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_content_subscription_client_content_subscription_configuration"))
    private ContentSubscription contentSubscription;
    
    @Column(name = "faith_based", columnDefinition = "boolean")
    private boolean faithBased;

    @Column(name = "source", columnDefinition = "boolean")
    private boolean source;
   
    @ManyToOne
    @JoinColumn(name = "source_content_subscription_id", columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_source_content_subscription_client_content_subscription_configuration"))
    private ContentSubscription sourceContentSubscription;

    /**
     * Default constructor
     */
    public ClientContentSubscriptionConfiguration() {

    }

    /**
     * id constructor
     * 
     * @param id
     *  to use
     */
    public ClientContentSubscriptionConfiguration(Long id) {
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
    
    public boolean isFaithBased() {
		return faithBased;
	}

	public void setFaithBased(boolean faithBased) {
		this.faithBased = faithBased;
	}

	public boolean isSource() {
		return source;
	}

	public ContentSubscription getContentSubscription() {
		return contentSubscription;
	}

	public void setContentSubscription(ContentSubscription contentSubscription) {
		this.contentSubscription = contentSubscription;
	}

	public void setSource(boolean source) {
		this.source = source;
	}
	
	public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ContentSubscription getSourceContentSubscription() {
		return sourceContentSubscription;
	}

	public void setSourceContentSubscription(
			ContentSubscription sourceContentSubscription) {
		this.sourceContentSubscription = sourceContentSubscription;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientContentSubscriptionConfiguration that = (ClientContentSubscriptionConfiguration) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientContentSubscriptionConfiguration{" +
                "client=" + client +
                ", contentSubscription=" + contentSubscription +
                ", faithBased=" + faithBased +
                ", source=" + source +
                ", sourceContentSubscription=" + sourceContentSubscription + 
                ", version=" + version +
                '}';
    }

	
}
