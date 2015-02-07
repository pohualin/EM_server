package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * A Reference Tag
 */
@Audited
@XmlRootElement(name = "reference_tag")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_tag")
public class ReferenceTag extends AbstractAuditingEntity implements
        Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    @JsonBackReference
    @NotNull
    private ReferenceGroup group;

    /**
     * No arg constructor
     */
    public ReferenceTag() {
    }

    /**
     * Id constructor
     *
     * @param id to use
     */
    public ReferenceTag(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReferenceGroup getGroup() {
        return group;
    }

    public void setGroup(ReferenceGroup group) {
        this.group = group;
    }
}
