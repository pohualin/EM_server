package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamEmailConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamEmailConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamEmailConfigurationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.SecretQuestionResource;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponseResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * ClientTeamEmailConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })

public class ClientTeamEmailConfigurationsResource {
	
    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;
    
    @Resource
    ClientTeamEmailConfigurationResourceAssembler emailConfigurationAssembler;
    
   
    /**
     * Find client team email configuration if there are any
     *
     * @param teamId    for the email configuration
     * @param pageable  which page to fetch
     * @param assembler makes a page for ClientTeamEmailConfiguration
     * @return a ClientTeamEmailConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/email_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "rank,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<ClientTeamEmailConfigurationPage> findTeamEmailConfig(
            @PathVariable("teamId") Long teamId,
            @PageableDefault(size = 10, sort = "rank") Pageable pageable,
            PagedResourcesAssembler<ClientTeamEmailConfiguration> assembler) {
    	
        Page<ClientTeamEmailConfiguration> page = clientTeamEmailConfigurationService
                .findByTeam(new Team(teamId), pageable);
    	if (page.hasContent()) {
    	 return new ResponseEntity<>(new ClientTeamEmailConfigurationPage(
                 assembler.toResource(page,
                		 emailConfigurationAssembler), page),
                 HttpStatus.OK);
    	}else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        
    }
    
    
    /**
     * Save or update client team email configuration 
     *
     * @param teamId    for the email configuration
     * @param clientTeamEmailConfiguration the user client team configuration that needs to save or update
     * @return a ClientTeamEmailConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/email_configuration", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamEmailConfigurationResource> saveOrUpdate(
            @PathVariable("teamId") Long teamId,
            @RequestBody ClientTeamEmailConfiguration clientTeamEmailConfiguration) {
    	clientTeamEmailConfiguration.setTeam(new Team(teamId));
    	ClientTeamEmailConfiguration emailConfiguration = clientTeamEmailConfigurationService.saveOrUpdate(clientTeamEmailConfiguration);
               
        if (emailConfiguration != null) {
            return new ResponseEntity<>(
            		emailConfigurationAssembler
                            .toResource(emailConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
