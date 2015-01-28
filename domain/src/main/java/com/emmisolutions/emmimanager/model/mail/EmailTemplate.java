package com.emmisolutions.emmimanager.model.mail;

import com.emmisolutions.emmimanager.model.AbstractAuditingEntity;

import javax.persistence.*;

/**
 * Represents an email template
 */
@Entity
@Table(name = "email_template",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"type"}, name = "uk_email_template_type")
        }
)
public class EmailTemplate extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(length = 5120, columnDefinition = "nvarchar(5120)", nullable = false)
    private String content;

    @Column(length = 100, columnDefinition = "varchar(100)", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailTemplateType type;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmailTemplateType getType() {
        return type;
    }

    public void setType(EmailTemplateType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailTemplate that = (EmailTemplate) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EmailTemplate{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
