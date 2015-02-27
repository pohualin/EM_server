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
 * Secret question persistence
 *
 */
public interface SecretQuestionPersistence {

       
    /**
     * 
     * @param pageable
     * @return page of secret question
     */
    Page<SecretQuestion> findAll(Pageable pageable);
    
    
    /** 
     * 
     * @param secretQuestion 
     * @return secret question
     */
    SecretQuestion save(SecretQuestion secretQuestion);


   /**
    * 
    * @param secretQuestion
    * @return secret question
    */
    SecretQuestion reload(SecretQuestion secretQuestion);
    
   
  
}
