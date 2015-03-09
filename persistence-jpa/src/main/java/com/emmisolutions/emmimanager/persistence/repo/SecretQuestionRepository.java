package com.emmisolutions.emmimanager.persistence.repo;

import java.util.List;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * secret question repo
 *
 */
public interface SecretQuestionRepository extends JpaRepository<SecretQuestion, Long>{

  
}
