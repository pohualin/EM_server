package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Provider;

/**
 * Provider persistence class
 */
public interface ProviderPersistence {
	
    /**
     * Saves a provider
     * @param Provider - to be saved
     * @return the saved provider
     */
	Provider save(Provider provider);
	
	Provider reload(Long id);

}
