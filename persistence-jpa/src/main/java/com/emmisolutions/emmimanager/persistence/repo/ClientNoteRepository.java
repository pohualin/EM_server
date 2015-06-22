package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.ClientNote;

/**
 * Spring Data Repository for ClientNote Entity
 */
public interface ClientNoteRepository extends JpaRepository<ClientNote, Long>,
        JpaSpecificationExecutor<ClientNote> {

    public ClientNote findByClientId(Long clientId);
}
