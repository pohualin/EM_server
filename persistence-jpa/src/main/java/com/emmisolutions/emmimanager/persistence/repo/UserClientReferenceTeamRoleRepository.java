package com.emmisolutions.emmimanager.persistence.repo;


import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for Reference Role
 */
public interface UserClientReferenceTeamRoleRepository extends JpaRepository<UserClientReferenceTeamRole, Long>, JpaSpecificationExecutor<UserClientReferenceTeamRole> {

}
