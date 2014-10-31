package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * Spring Data Repo for Tag Entities
 */
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Remove all tags for a client id where the group ids are not in the Set
     *
     * @param forThisClientId    to use
     * @param notInTheseGroupIds not these group ids
     */
    long deleteByGroupClientIdEqualsAndGroupIdNotIn(Long forThisClientId, Set<Long> notInTheseGroupIds);

    /**
     * Remove all tags for a client id
     *
     * @param forThisClientId    to use
     */
    long deleteByGroupClientIdEquals(Long forThisClientId);
}
