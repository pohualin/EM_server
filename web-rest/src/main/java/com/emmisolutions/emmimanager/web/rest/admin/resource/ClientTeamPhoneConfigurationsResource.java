package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamPhoneConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamPhoneConfigurationResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * ClientTeamPhoneConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })

public class ClientTeamPhoneConfigurationsResource {
	
    @Resource
    ClientTeamPhoneConfigurationService clientTeamPhoneConfigurationService;
    
    @Resource
    ClientTeamPhoneConfigurationResourceAssembler phoneConfigurationAssembler;
    
   
    /**
     * Find client team phone configuration if there are any
     *
     * @param teamId    for the phone configuration
     * @param assembler makes a page for ClientTeamPhoneConfiguration
     * @return a ClientTeamPhoneConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/phone_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamPhoneConfigurationResource> findTeamPhoneConfig(
            @PathVariable("teamId") Long teamId,
            PagedResourcesAssembler<ClientTeamPhoneConfiguration> assembler) {
    	
        ClientTeamPhoneConfiguration clientTeamPhoneConfiguration = clientTeamPhoneConfigurationService
                .findByTeam(new Team(teamId));
        
    	if (clientTeamPhoneConfiguration != null) {
      	 return new ResponseEntity<>(phoneConfigurationAssembler.toResource(
    			 clientTeamPhoneConfiguration),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
     }
    
    
    /**
     * Save or update client team phone configuration 
     *
     * @param teamId    for the phone configuration
     * @param clientTeamPhoneConfiguration the user client team configuration that needs to save or update
     * @return a ClientTeamPhoneConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/phone_configuration", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamPhoneConfigurationResource> saveOrUpdate(
            @PathVariable("teamId") Long teamId,
            @RequestBody ClientTeamPhoneConfiguration clientTeamPhoneConfiguration) {
    	clientTeamPhoneConfiguration.setTeam(new Team(teamId));
    	ClientTeamPhoneConfiguration phoneConfiguration = clientTeamPhoneConfigurationService.saveOrUpdate(clientTeamPhoneConfiguration);
               
        if (phoneConfiguration != null) {
            return new ResponseEntity<>(
            		phoneConfigurationAssembler
                            .toResource(phoneConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
