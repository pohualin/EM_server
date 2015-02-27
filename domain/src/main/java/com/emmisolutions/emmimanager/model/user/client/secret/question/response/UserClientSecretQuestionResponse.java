package com.emmisolutions.emmimanager.model.user.client.secret.question.response;

import java.io.Serializable;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * A user client secret question response
 */
@Entity
@Audited
@Table(name = "user_client_secret_question_response")
@XmlRootElement(name = "user_client_secret_question_response")
public class UserClientSecretQuestionResponse extends AbstractAuditingEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Integer version;

	@ManyToOne(optional = false)
	@NotNull
	@JoinColumn(name = "user_client_id", referencedColumnName="id")
	private UserClient userClient;
	
	@ManyToOne(optional = false)
	@NotAudited
	@NotNull
	@JoinColumn(name = "secret_question_id", referencedColumnName="id")
	private SecretQuestion secretQuestion;
	
	@Column(length = 200, columnDefinition = "nvarchar(200)")
	@Pattern(regexp = "[0-9A-Za-z-'=_\\-+;:@#$%&?,.!() <>^~*`|{}\\[\\]\"\\/]*", message = "Can only contain letters, digits, spaces, and the following characters: - ' = _ - + ; : @ # $ % & ? , . ! |(){}[]: <>")
	@Size(max = 200)
	private String response;
	    
    public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public SecretQuestion getSecretQuestion() {
		return secretQuestion;
	}

	public void setSecretQuestion(SecretQuestion secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	public UserClientSecretQuestionResponse(){
		
	}
	
	/**
     * ID constructor
     *
     * @param id      to use
     */
    public UserClientSecretQuestionResponse(Long id) {
        this.id = id;
    }

	public Long getId() {
		return id;
	}

	public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
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
        UserClientSecretQuestionResponse other = (UserClientSecretQuestionResponse) obj;
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
		return "Response [id=" + id + ", secret question =" + secretQuestion + ", response=" + response
				+ "]";
	}

	
	
}
