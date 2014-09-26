package com.emmisolutions.emmimanager.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Client Types
 *
 *     PROVIDER, PHYSICIAN_PRACTICE, PAYER, PHARMA, OTHER
 */
@Audited
@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(name = "uk_salesforce_account_id", columnNames = "salesforce_account_id"))
@XmlRootElement(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(length = 50, nullable = false)
    private String type;
}
