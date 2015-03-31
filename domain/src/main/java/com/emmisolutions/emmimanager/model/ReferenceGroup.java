package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Reference Group and its containing tags
 */
@Audited
@XmlRootElement(name = "reference_group")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "reference_group")
public class ReferenceGroup extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @XmlElement(name = "tag")
    @XmlElementWrapper(name = "tags")
    @JsonProperty("tag")
    @JoinColumn(name = "group_id", nullable = false)
    private Set<ReferenceTag> tags = new HashSet<>();

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "reference_group_type_id", nullable = false)
    private ReferenceGroupType type;
    
    private boolean active = true;

    /**
     * Reference Group id constructor
     *
     * @param id the id
     */
    public ReferenceGroup(Long id) {
        super();
        this.id = id;
    }

    public ReferenceGroup() {
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

    public Set<ReferenceTag> getTags() {
        return tags;
    }

    public void setTags(Set<ReferenceTag> tags) {
        this.tags = tags;
    }

    public ReferenceGroupType getType() {
        return type;
    }

    public void setType(ReferenceGroupType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ReferenceGroup group = (ReferenceGroup) o;
        return !(getId() != null ? !getId().equals(group.getId()) : group
                .getId() != null);
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result;
        return result;
    }

    @Override
    public String toString() {
        return "Reference Group{" + "id=" + getId() + ", name='" + getName() + '\'' + '}';
    }

}
