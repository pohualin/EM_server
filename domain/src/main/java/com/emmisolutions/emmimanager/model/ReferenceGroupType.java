package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

/**
 * Used to allow reference groups to be renamed but still maintain their original purpose.
 */
@Audited
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_group_type")
public class ReferenceGroupType extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

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
}
