package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repo for UserClient Entities
 */
public interface UserClientRepository extends JpaRepository<UserClient, Long>,
        JpaSpecificationExecutor<UserClient> {

    /**
     * Find a user by the login
     *
     * @param login case insensitive comparison
     * @return UserClient or null
     */
    @Cacheable(value = "clientFindByLoginIgnoreCase", key = "#p0")
    @Query("select u from UserClient u " +
            "left join fetch u.teamRoles tr left join fetch tr.userClientTeamRole uctr " +
            "left join fetch uctr.userClientTeamPermissions tp " +
            "left join fetch u.clientRoles ur left join fetch ur.userClientRole r " +
            "left join fetch r.userClientPermissions p where u.login = lower(:login)")
    UserClient findByLoginIgnoreCase(@Param("login") String login);

    @Caching(evict = {
            @CacheEvict(value = "clientFindById", key = "#p0.id"),
            @CacheEvict(value = "clientFindByLoginIgnoreCase", key = "#p0.login")
    })
    @Override
    UserClient save(UserClient userClient);

    @Cacheable(value = "clientFindById", key = "#p0")
    @Override
    UserClient findOne(Long id);


    /**
     * Finds a UserClient by activation key
     *
     * @param activationKey the key
     * @return UserClient or null
     */
    UserClient findByActivationKey(String activationKey);

    /**
     * Finds a UserClient by password reset token
     *
     * @param resetToken the key
     * @return UserClient or null
     */
    UserClient findByPasswordResetToken(String resetToken);

    /**
     * Finds a UserClient by validation token
     *
     * @param validationToken the key
     * @return UserClient or null
     */
    UserClient findByValidationToken(String validationToken);

    /**
     * Find a user by email address
     *
     * @param email to search for
     * @return UserClient or null
     */
    UserClient findByEmailIgnoreCase(String email);
}
