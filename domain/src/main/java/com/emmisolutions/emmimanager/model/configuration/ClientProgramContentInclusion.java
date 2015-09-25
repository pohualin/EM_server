package com.emmisolutions.emmimanager.model.configuration;

import org.hibernate.envers.Audited;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.program.Program;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * ClientProgramInclusion Domain Object
 * 
 */
@Audited
@Entity
@XmlRootElement(name = "client_program_inclusion")
@Table(name = "client_program_inclusion",
uniqueConstraints =
@UniqueConstraint(columnNames = {"client_id", "program_id"}, name = "uk_client_program_inclusion"))
public class ClientProgramContentInclusion extends AbstractAuditingEntity
        implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long id;

    @Version
    private Integer version;

   	@NotNull
   	@ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false,
    		foreignKey = @ForeignKey(name ="fk_client_program_inclusion_client"))
    private Client client;
   
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "program_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(name = "fk_client_program_inclusion_rf_emmi"))
    private Program program;

    
    /**
     * Default constructor
     */
    public ClientProgramContentInclusion() {

    }

    /**
     * id constructor
     * 
     * @param id
     *  to use
     */
    public ClientProgramContentInclusion(Long id) {
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

   	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientProgramContentInclusion that = (ClientProgramContentInclusion) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClientProgramContentInclusion{" +
                "client=" + client +
                ", program=" + program +
                ", version=" + version +
                '}';
    }

	
}
