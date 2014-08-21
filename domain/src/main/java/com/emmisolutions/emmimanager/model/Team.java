package com.emmisolutions.emmimanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * A Team that corresponds to a Client
 */
@Audited
@Entity
@Table(name = "client_team")
@XmlRootElement(name = "client_team")
public class Team extends AbstractAuditingEntity implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    private boolean active;

    @NotNull
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;
    
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String description;
    
    @Size(max = 30)
    @Column(length = 30)
    private String phone;
    
    @Size(max = 30)
    @Column(length = 30)
    private String fax;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}    
}
