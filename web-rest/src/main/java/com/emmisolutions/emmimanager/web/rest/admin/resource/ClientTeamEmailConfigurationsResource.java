package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamEmailConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamEmailConfigurationResourceAssembler;

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
 * ClientTeamEmailConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {
    APPLICATION_JSON_VALUE,
    APPLICATION_XML_VALUE
})
public class ClientTeamEmailConfigurationsResource {
    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;

    @Resource
    ClientTeamEmailConfigurationResourceAssembler emailConfigurationAssembler;

    /**
     * Find client team email configuration if there are any
     *
     * @param teamId for the email configuration
     * @return a ClientTeamEmailConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/email_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamEmailConfigurationResource> findTeamEmailConfig(@PathVariable("teamId") Long teamId) {
        ClientTeamEmailConfiguration clientTeamEmailConfiguration = clientTeamEmailConfigurationService.findByTeam(new Team(teamId));

        if (clientTeamEmailConfiguration != null) {
            return new ResponseEntity<>(emailConfigurationAssembler.toResource(clientTeamEmailConfiguration), HttpStatus.OK);
        } else {
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
            return new ResponseEntity<>(emailConfigurationAssembler.toResource(emailConfiguration), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
