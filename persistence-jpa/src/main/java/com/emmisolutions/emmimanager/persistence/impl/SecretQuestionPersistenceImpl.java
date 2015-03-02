package com.emmisolutions.emmimanager.persistence.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserClientSecretQuestionResponseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * Repo to deal with  Secret Question persistence.
 */
@Repository
public class SecretQuestionPersistenceImpl implements SecretQuestionPersistence {

    @Resource
    SecretQuestionRepository secretQuestionRepository;

	@Override
	public Page<SecretQuestion> findAll(Pageable pageable) {
		return  secretQuestionRepository.findAll(pageable);
	}

   @Override
    public SecretQuestion reload(SecretQuestion secretQuestion) {
       return secretQuestionRepository.findOne(secretQuestion.getId());

    }

}
