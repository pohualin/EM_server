package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamSchedulingConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamSchedulingConfigurationResourceAssembler;

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
 * ClientTeamSchedulingConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })

public class ClientTeamSchedulingConfigurationsResource {
	
    @Resource
    ClientTeamSchedulingConfigurationService clientTeamSchedulingConfigurationService;
    
    @Resource
    ClientTeamSchedulingConfigurationResourceAssembler schedulingConfigurationAssembler;
    
   
    /**
     * Find client team scheduling configuration if there are any
     *
     * @param teamId    for the scheduling configuration
     * @param assembler makes a page for ClientTeamSchedulingConfiguration
     * @return a ClientTeamSchedulingConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/scheduling_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamSchedulingConfigurationResource> findTeamSchedulingConfig(
            @PathVariable("teamId") Long teamId,
            PagedResourcesAssembler<ClientTeamSchedulingConfiguration> assembler) {
    	
        ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration = clientTeamSchedulingConfigurationService
                .findByTeam(new Team(teamId));
        
    	if (clientTeamSchedulingConfiguration != null) {
      	 return new ResponseEntity<>(schedulingConfigurationAssembler.toResource(
    			 clientTeamSchedulingConfiguration),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
     }
    
    
    /**
     * Save or update client team scheduling configuration 
     *
     * @param teamId    for the scheduling configuration
     * @param clientTeamSchedulingConfiguration the user client team configuration that needs to save or update
     * @return a ClientTeamSchedulingConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/scheduling_configuration", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamSchedulingConfigurationResource> saveOrUpdate(
            @PathVariable("teamId") Long teamId,
            @RequestBody ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration) {
    	clientTeamSchedulingConfiguration.setTeam(new Team(teamId));
    	ClientTeamSchedulingConfiguration schedulingConfiguration = clientTeamSchedulingConfigurationService.saveOrUpdate(clientTeamSchedulingConfiguration);
               
        if (schedulingConfiguration != null) {
            return new ResponseEntity<>(
            		schedulingConfigurationAssembler
                            .toResource(schedulingConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
