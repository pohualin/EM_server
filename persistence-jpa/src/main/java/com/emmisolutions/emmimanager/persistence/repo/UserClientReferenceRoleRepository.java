package com.emmisolutions.emmimanager.persistence.repo;


import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for Reference Role
 */
public interface UserClientReferenceRoleRepository extends JpaRepository<UserClientReferenceRole, Long>, JpaSpecificationExecutor<UserClientReferenceRole> {

}
