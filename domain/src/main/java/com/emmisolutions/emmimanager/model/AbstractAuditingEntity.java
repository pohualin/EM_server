package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 * Base abstract class for entities which will hold definitions for id, version, created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity {

    @NotNull
    @CreatedBy
    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @JsonIgnore
    @CreatedDate
    @Column(name = "created_date")
    private DateTime createdDate = DateTime.now();

    @NotNull
    @JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @NotNull
    @JsonIgnore
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate = DateTime.now();

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
