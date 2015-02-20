package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data repo for UserClientSecretQuestionResponse objects
 */
public interface UserClientSecretQuestionResponseRepository extends JpaRepository<UserClientSecretQuestionResponse, Long> {

	 /**
     * Find UserClientSecretQuestionResponse based on user client id
     *
     * @param userClientId and pageable
     * @return List of Questions or null
     */
    Page<UserClientSecretQuestionResponse> findByUserClientId (Long id, Pageable pageable);
    
       
    /**
     * Retrieve UserClientSecretQuestionResponse based on UserClientId and question id
     *
     * @param userClientId and question id to load
     * @return UserClientSecretQuestionReponse or null
     */
    UserClientSecretQuestionResponse findByUserClientIdAndSecretQuestionId(
            Long userClientId, Long questionId);
    
    
    
   
}
