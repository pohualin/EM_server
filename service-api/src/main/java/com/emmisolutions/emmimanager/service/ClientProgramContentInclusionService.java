package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The ClientProgramContentInclusion Service
 */
public interface ClientProgramContentInclusionService {

    /**
     * create ClientProgramContentInclusion
     * 
     * @param clientProgramContentInclusionn
     *            to create
     * @return a associated ClientProgramContentInclusion
     */
    public ClientProgramContentInclusion create(
    		ClientProgramContentInclusion clientProgramContentInclusion);

    
    /**
     * Find a Page of existing ClientProgramContentInclusion by Client 
     * @param client
     *            to find
     * @param pageable
     *            to use
     * @return a Page of existing ClientProgramContentInclusion
     */
    public Page<ClientProgramContentInclusion> findByClient(
            Client client,
            Pageable pageable);
       
    /**
     * Delete an existing ClientProgramContentInclusion
     * 
     * @param clientProgramContentInclusion
     *            to delete
     */
    public void delete(ClientProgramContentInclusion clientProgramContentInclusion);

    /**
     * Reload an existing ClientProgramContentInclusion
     * 
     * @param clientProgramContentInclusion
     *            to reload
     * @return an existing ClientProgramContentInclusion
     */
    public ClientProgramContentInclusion reload(
    		ClientProgramContentInclusion clientProgramContentInclusion);
    
}
