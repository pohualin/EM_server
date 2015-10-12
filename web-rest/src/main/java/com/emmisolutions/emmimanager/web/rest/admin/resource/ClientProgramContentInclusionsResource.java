package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.service.*;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientContentSubscriptionConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientContentSubscriptionConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientProgramContentInclusionPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientProgramContentInclusionResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientProgramContentInclusionResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.program.ProgramContentResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.program.ProgramContentResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.program.specialty.SpecialtyContentResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.program.specialty.SpecialtyContentResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Client Program Content REST Resource
 */
@RestController("ClientProgramContentInclusionsResource")
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class ClientProgramContentInclusionsResource {

    public static final String SPECIALTY_ID_REQUEST_PARAM = "s";
    public static final String TERM_REQUEST_PARAM = "q";
          
    @Resource
    ProgramService programService;
    @Resource(name = "programResourceAssembler")
    ResourceAssembler<Program, ProgramContentResource> programResourceResourceAssembler;
    @Resource(name = "specialtyResourceAssembler")
    ResourceAssembler<Specialty, SpecialtyContentResource> specialtyResourceAssembler;
    @Resource
    ClientProgramContentInclusionService clientProgramContentInclusionService;
    
    @Resource
    ClientProgramContentInclusionResourceAssembler clientProgramContentInclusionAssembler;
   
    /**
     * Find possible programs that can be include for a client
     *
     * @param clientId  for which to load the programs
     * @param pageable  which page to fetch
     * @param assembler makes a page of Program objects into ProgramResource objects
     * @return a ProgramResourcePage
     */
    @RequestMapping(value = "/clients/{clientId}/programs", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = SPECIALTY_ID_REQUEST_PARAM, value = "the specialty id to narrow by", dataType = "integer", paramType = "query")
    })
    public ProgramContentResourcePage possibleProgramContent(
            @PathVariable("clientId") Long clientId,
            @PageableDefault(size = 10) Pageable pageable,
            PagedResourcesAssembler<Program> assembler,
            @RequestParam(value = SPECIALTY_ID_REQUEST_PARAM, required = false) Set<Integer> specialtyIds,
            @RequestParam(value = TERM_REQUEST_PARAM, required = false) String term){

    	 ProgramSearchFilter programSearchFilter = new ProgramSearchFilter().addTerm(term);
    	 programSearchFilter.client(new Client(clientId));
        if (!CollectionUtils.isEmpty(specialtyIds)) {
            for (Integer specialtyId : specialtyIds) {
                programSearchFilter.addSpecialty(new Specialty(specialtyId));
            }
        }
        
        Page<Program> programPage = programService.find(programSearchFilter, pageable);
      
        return new ProgramContentResourcePage(programSearchFilter,
                assembler.toResource(programPage, programResourceResourceAssembler),
                programPage);
    }

    /**
     * Find the specialties for the client
     *
     * @param clientId  for the programs with the filter/filters that is provided
     * @param pageable  which page to fetch
     * @param assembler makes a page of  objects into SpecialtyContentResource objects
     * @return a SpecialtyContentResourcePage
     */
    @RequestMapping(value = "/clients/{clientId}/specialties", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public SpecialtyContentResourcePage findSpecialties(
            @PathVariable("clientId") Long clientId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<Specialty> assembler) {

        Page<Specialty> specialtyPage = programService.findSpecialties(pageable);

        return new SpecialtyContentResourcePage(
                assembler.toResource(specialtyPage, specialtyResourceAssembler),
                specialtyPage);
    }
    
    /**
     * Find client program content inclusion if there are any
     *
     * @param clientId    for the program content inclusion
     * @param pageable  which page to fetch
     * @param assembler makes a page for ClientProgramContentInclusion
     * @return a ClientProgramContentInclusion response entity
     */
    @RequestMapping(value = "/clients/{clientId}/content_program_content_inclusion", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "20", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientProgramContentInclusionPage> findClientProgramContentInclusion(
            @PathVariable("clientId") Long clientId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<ClientProgramContentInclusion> assembler) {
    	
        Page<ClientProgramContentInclusion> page = clientProgramContentInclusionService
                .findByClient(new Client(clientId), pageable);
    	if (page.hasContent()) {
    	   return new ResponseEntity<>(new ClientProgramContentInclusionPage(
                 assembler.toResource(page,
                		 clientProgramContentInclusionAssembler), page),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
    
    /**
     * create client program content inclusion  
     *
     * @param clientId   for the program content inclusion
     * @param ClientProgramContentInclusion the user program content inclusion that needs save
     * @return a ClientProgramContentInclusion response entity
     */
    @RequestMapping(value = "/clients/{clientId}/content_program_content_inclusion", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientProgramContentInclusionResource> create(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientProgramContentInclusion clientProgramContentInclusion) {
    	clientProgramContentInclusion.setClient(new Client(clientId));
    	ClientProgramContentInclusion created =  clientProgramContentInclusionService
    			.create(clientProgramContentInclusion);
               
        if (created != null) {
            return new ResponseEntity<>(
            		clientProgramContentInclusionAssembler
                            .toResource(created),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    /**
     * Get an existing client program content inclusion
     * 
     * @param id
     *            to reload
     * @return an existing ClientProgramContentInclusion
     */
    @RequestMapping(value = "/clients/{clientId}/content_program_content_inclusion/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientProgramContentInclusionResource> get(
    		@PathVariable("clientId") Long clientId,
            @PathVariable("id") Long id) {
    	ClientProgramContentInclusion  reload = clientProgramContentInclusionService
                .reload(new ClientProgramContentInclusion(id));
        if (reload != null) {
            return new ResponseEntity<>(
            		clientProgramContentInclusionAssembler.toResource(reload),
                    HttpStatus.OK);
        } else {
        	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

   /**
     * Delete an Client Program Content Inclusion
     * @param id   id for the client program content inclusion to delete 
     */
    @RequestMapping(value = "/clients/{clientId}/content_program_content_inclusion/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<Void> delete(
    		@PathVariable("clientId") Long clientId,
    		@PathVariable("id") Long id) {
    	clientProgramContentInclusionService
                .delete(new ClientProgramContentInclusion(id));
    	return new ResponseEntity<>(HttpStatus.OK);
    }
  
    
}
