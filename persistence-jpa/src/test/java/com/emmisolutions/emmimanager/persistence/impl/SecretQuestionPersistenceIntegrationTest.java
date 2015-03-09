package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		Page<SecretQuestion> set = secretQuestionPersistence.findAll(new PageRequest(0, 10));
		assertThat("SecretQuestions have been created by database script", set.getSize() > 0, is(true));
		
		SecretQuestion aQuestion = set.getContent().get(1);
		
		SecretQuestion secretQuestion = secretQuestionPersistence.reload( aQuestion);
		assertThat("Reload of a question returns the same question", secretQuestion, is(aQuestion));
   }
}
