package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.service.ClientContentSubscriptionConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.IpRestrictConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientContentSubscriptionConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientContentSubscriptionConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription.ClientContentSubscriptionConfigurationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * ClientContentSubscritpionConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })

public class ClientContentSubscriptionConfigurationsResource {
	
    @Resource
    ClientContentSubscriptionConfigurationService clientContentSubscriptionConfigurationService;
    
    @Resource
    ClientContentSubscriptionConfigurationResourceAssembler contentSubscriptionConfigurationAssembler;
    
    /**
     * Get the page of content subscription
     *
     * @param pageable  the pagination
     * @return content subscription entity
     */
    @RequestMapping(value = "/content_subscriptions", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "20", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "rank,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<Page<ContentSubscription>> getContentSubscriptionList(
    		@PageableDefault(size = 20, sort = "rank") Pageable pageable) {

        Page<ContentSubscription> page = clientContentSubscriptionConfigurationService
                .list(pageable);
       
        if (page != null) {
 	    	 return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
 
   
    /**
     * Find client content subscription configuration if there are any
     *
     * @param clientId    for the content subscription configuration
     * @param pageable  which page to fetch
     * @param assembler makes a page for ClientContentSubscriptionConfiguration
     * @return a ClientContentSubscriptionConfiguration response entity
     */
    @RequestMapping(value = "/clients/{clientId}/content_subscription_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "20", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientContentSubscriptionConfigurationPage> findClientContentSubscriptionConfig(
            @PathVariable("clientId") Long clientId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable,
            PagedResourcesAssembler<ClientContentSubscriptionConfiguration> assembler) {
    	
        Page<ClientContentSubscriptionConfiguration> page = clientContentSubscriptionConfigurationService
                .findByClient(new Client(clientId), pageable);
    	if (page.hasContent()) {
    	   return new ResponseEntity<>(new ClientContentSubscriptionConfigurationPage(
                 assembler.toResource(page,
                		 contentSubscriptionConfigurationAssembler), page),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        
    }
    
    
    /**
     * create content subscription configuration  
     *
     * @param clientId   for the content subscription configuration
     * @param clientContentSubscriptionConfiguration the user content subscription configuration that needs save
     * @return a ClientContentSubscriptionConfiguration response entity
     */
    @RequestMapping(value = "/clients/{clientId}/content_subscription_configuration", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientContentSubscriptionConfigurationResource> create(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
    	clientContentSubscriptionConfiguration.setClient(new Client(clientId));
    	ClientContentSubscriptionConfiguration contentSubscriptionConfiguration = clientContentSubscriptionConfigurationService
    			.create(clientContentSubscriptionConfiguration);
               
        if (contentSubscriptionConfiguration != null) {
            return new ResponseEntity<>(
            		contentSubscriptionConfigurationAssembler
                            .toResource(contentSubscriptionConfiguration),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
   /**
     * Update an existing content subscription configuration
     * 
     * @param id   for the content subscription configuration
     * @param contentSubscritpionConfiguration
     *            contains updated information
     * @return an updated ContentSubscritpionConfiguration
     */
    @RequestMapping(value = "/content_subscription_configuration/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientContentSubscriptionConfigurationResource> update(
            @PathVariable("id") Long id,
            @RequestBody ClientContentSubscriptionConfiguration clientContentSubscriptionConfiguration) {
        ClientContentSubscriptionConfiguration updated = clientContentSubscriptionConfigurationService
                .update(clientContentSubscriptionConfiguration);
        if (updated != null) {
            return new ResponseEntity<>(
            		contentSubscriptionConfigurationAssembler.toResource(updated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Reload an existing content subscription configuration
     * 
     * @param id
     *            to reload
     * @return an existing ContentSubscritpionConfiguration
     */
    @RequestMapping(value = "/content_subscription_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientContentSubscriptionConfigurationResource> get(
            @PathVariable("id") Long id) {
    	ClientContentSubscriptionConfiguration  reload = clientContentSubscriptionConfigurationService
                .reload(new ClientContentSubscriptionConfiguration(id));
        if (reload != null) {
            return new ResponseEntity<>(
            		contentSubscriptionConfigurationAssembler.toResource(reload),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Delete an content subscription configuration
     * @param id   id for the content subscription configuration to delete 
     */
    @RequestMapping(value = "/content_subscription_configuration/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    	clientContentSubscriptionConfigurationService
                .delete(new ClientContentSubscriptionConfiguration(id));
    	return new ResponseEntity<>(HttpStatus.OK);
    }
}
