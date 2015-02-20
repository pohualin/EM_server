package com.emmisolutions.emmimanager.persistence;

import java.util.List;
import java.util.Set;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.SecretQuestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Provider persistence class
 */
public interface SecretQuestionPersistence {

       
    /**
     * Query the original list of secret question objects
     * @param pageable
     * @return a list of secret question objects
     */
    Page<SecretQuestion> findAll(Pageable pageable);
    
    
    /**
     * Saves the secret question
     *
     * @param secret question to be saved
     * @return the saved secret question
     */
    SecretQuestion save(SecretQuestion secretQuestion);


    /**
     * Query the secret question object by id
     *
     * @return a secret question object
     */
    SecretQuestion reload(SecretQuestion secretQuestion);
    
   
  
}
