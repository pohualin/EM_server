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
     * Update a ClientProgramContentInclusion
     * 
     * @param clientProgramContentInclusionn
     *            to update
     * @return a associated ClientProgramContentInclusion
     */
    public ClientProgramContentInclusion update(
    		ClientProgramContentInclusion clientProgramContentInclusion);
    
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
     * Find a Page of available programs based on program search filters and 
     * filter out the already associated programs by Client 
     * 
     * @param client
     *            to find
     * @param  programSearchFilter
     *            to filer          
     * @param pageable
     *            to use
     * @return a Page of available program contents
     */
    public Page<Program> findPossibleProgramByClient(Client client,
			ProgramSearchFilter programSearchFilter, Pageable pageable);
    
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
