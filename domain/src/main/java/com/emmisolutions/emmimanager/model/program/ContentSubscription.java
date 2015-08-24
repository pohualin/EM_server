package com.emmisolutions.emmimanager.model.program;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

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
 * Reference table to store rf content subscription for users
 */
@Entity
@Table(name = "rf_content_subscription", schema = "program")
@Audited(targetAuditMode = NOT_AUDITED)
public class ContentSubscription {

	@Id
    @Column(name = "config_cd")
    private Integer id;

    private boolean active;

    @Column(name = "name")
    private String name;
    
    @Column(name = "display_ordr")
    private Integer rank;
    
    @Column(name = "primary_sbscrptn")
    private boolean primarySbscrptn;
    
	@Column(name = "source_sbscrptn")
    private boolean sourceSbscrptn;
    
    public ContentSubscription(){
		
	}
	
	public ContentSubscription(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isPrimarySbscrptn() {
		return primarySbscrptn;
	}

	public void setPrimarySbscrptn(boolean primarySbscrptn) {
		this.primarySbscrptn = primarySbscrptn;
	}

	public boolean isSourceSbscrptn() {
		return sourceSbscrptn;
	}

	public void setSourceSbscrptn(boolean sourceSbscrptn) {
		this.sourceSbscrptn = sourceSbscrptn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
        ContentSubscription other = (ContentSubscription) obj;
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
		return "Content Subscription [id=" + id + ", "
				+ "content subscription name =" + name
				+ "primary subscription =" + primarySbscrptn
				+ "source subscription =" + sourceSbscrptn
				+ "rank =" + rank
				+ "]";
	}

}
