package com.emmisolutions.emmimanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

/**
 * Reference table to store secret questions for client users
 */
@Entity
@Table(name = "secret_question")
public class SecretQuestion extends AbstractAuditingEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;
	
	@Column(name ="rank", columnDefinition = "integer")
	private Integer rank;

	public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @NotNull
	@Size(max = 1000 )
	@Column(name = "secret_question", columnDefinition = "nvarchar(1000)", nullable = false)
	private String secretQuestion;

	public String getSecretQuestion() {
		return secretQuestion;
	}

	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	public SecretQuestion(){
		
	}
	
	/**
	 * 
	 * @param id secret question id
	 * @param secretQuestion
	 */
    public SecretQuestion(Long id, String secretQuestion) {
        this.id = id;
        this.secretQuestion = secretQuestion;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SecretQuestion other = (SecretQuestion) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
	public String toString() {
		return "Question [id=" + id + ", secret question =" + secretQuestion
				+ "]";
	}

}
