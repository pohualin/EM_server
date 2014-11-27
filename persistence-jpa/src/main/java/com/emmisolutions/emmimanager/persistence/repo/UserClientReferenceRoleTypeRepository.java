package com.emmisolutions.emmimanager.persistence.repo;


import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for Client Reference Role Types
 */
public interface UserClientReferenceRoleTypeRepository extends JpaRepository<UserClientReferenceRoleType, Long>, JpaSpecificationExecutor<UserClientReferenceRoleType> {

}
