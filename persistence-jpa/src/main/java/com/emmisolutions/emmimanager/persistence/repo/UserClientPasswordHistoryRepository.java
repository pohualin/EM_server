package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;

/**
 * Spring Data Repository for UserClientPasswordHistory Entity
 */
public interface UserClientPasswordHistoryRepository extends
        JpaRepository<UserClientPasswordHistory, Long>,
        JpaSpecificationExecutor<UserClientPasswordHistory> {

    public List<UserClientPasswordHistory> findByUserClientIdOrderByPasswordSavedTimeDesc(Long id);
}
