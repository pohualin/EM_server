package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;

/**
 * Spring Data Repository for UserClientPasswordHistory Entity
 */
public interface UserClientPasswordHistoryRepository extends
        JpaRepository<UserClientPasswordHistory, Long>,
        JpaSpecificationExecutor<UserClientPasswordHistory> {

    public Page<UserClientPasswordHistory> findByUserClientIdOrderByCreatedDateDesc(
            Pageable pageable, Long id);
}
