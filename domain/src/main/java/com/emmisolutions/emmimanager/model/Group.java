package com.emmisolutions.emmimanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Group and its containing tags
 */
@Audited
@Entity
@Table(name = "client_group")
@XmlRootElement(name = "client_group")
public class Group extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @NotNull
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String name;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    @JsonManagedReference
    @XmlElement(name = "tag")
    @XmlElementWrapper(name = "tags")
    @JsonProperty("tag")
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "reference_group_type_id")
    private ReferenceGroupType type;

    public Group() {
    }

    /**
     * Group constructor
     *
     * @param name   of the group
     * @param client the client
     * @return Group
     */
    public Group(String name, Client client) {
        this.name = name;
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Group group = (Group) o;
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
        return "Group{" + "id=" + getId() + ", name='" + getName() + '\'' + '}';
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public ReferenceGroupType getType() {
        return type;
    }

    public void setType(ReferenceGroupType type) {
        this.type = type;
    }
}
