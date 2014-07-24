package com.emmisolutions.emmimanager.model;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Base abstract class for entities which will hold definitions for id, version, created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractAuditingEntity {

    @CreatedBy
    @XmlTransient
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @XmlTransient
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private DateTime createdDate;

    @NotNull
    @XmlTransient
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @XmlTransient
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
