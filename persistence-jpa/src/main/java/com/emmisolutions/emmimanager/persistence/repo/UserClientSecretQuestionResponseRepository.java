package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * user client secret question response 
 *
 */
public interface UserClientSecretQuestionResponseRepository extends JpaRepository<UserClientSecretQuestionResponse, Long> {

	 /**
	  * 
	  * @param id
	  * @param pageable
	  * @return page of user client secret question response
	  */
    Page<UserClientSecretQuestionResponse> findByUserClientId (Long id, Pageable pageable);
    
    /**
     * 
     * @param userClientId
     * @param questionId
     * @return user client secret question response
     */
    UserClientSecretQuestionResponse findByUserClientIdAndSecretQuestionId(
            Long userClientId, Long questionId);
    
    
    
   
}
