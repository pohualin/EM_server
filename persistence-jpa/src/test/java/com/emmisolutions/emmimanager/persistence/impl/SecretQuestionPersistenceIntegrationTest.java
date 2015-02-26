package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.repo.SecretQuestionRepository;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

/**
 * Integration test for SecretQuestionPersistence
 */
public class SecretQuestionPersistenceIntegrationTest extends
				BaseIntegrationTest {
	
	@Resource
	SecretQuestionPersistence secretQuestionPersistence;
	

	@Test
    public void loadAllSecretQuestions(){
        SecretQuestion secretQuestion = new SecretQuestion();
        secretQuestion.setSecretQuestion("What was the make and model of your first car?");
        secretQuestion.setCreatedBy("WTEST");
        secretQuestionPersistence.save(secretQuestion);
        
    	Page<SecretQuestion> set = secretQuestionPersistence.findAll(new PageRequest(0, 10));
    	secretQuestionPersistence.reload(secretQuestion);
    	assertThat("SecretQuestion has been created", set.hasContent(), is(true));
    	
    }
    

}
