package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.service.ClientTeamSelfRegConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamPhoneConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamPhoneConfigurationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamSelfRegConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.ClientTeamSelfRegConfigurationResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * ClientTeamSelfRegConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class ClientTeamSelfRegConfigurationsResource {

    @Resource
    ClientTeamSelfRegConfigurationService clientTeamSelfRegConfigurationService;

    @Resource(name = "clientTeamSelfRegConfigurationResourceAssembler")
    ResourceAssembler<ClientTeamSelfRegConfiguration, ClientTeamSelfRegConfigurationResource> selfRegConfigResourceAssembler;

    /**
     * Find client team self registration configuration if there is any
     *
     * @param teamId    for the self reg configuration
     * @param assembler makes a page for ClientTeamSelfRegConfiguration
     * @return a ClientTeamSelfRegConfiguration response entity
     */
    @RequestMapping(value = "/teams/{teamId}/self_reg_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamSelfRegConfigurationResource> findByTeam(
            @PathVariable("teamId") Long teamId,
            PagedResourcesAssembler<ClientTeamSelfRegConfiguration> assembler) {

        ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration = clientTeamSelfRegConfigurationService
                .findByTeam(new Team(teamId));

        if (clientTeamSelfRegConfiguration != null) {
            return new ResponseEntity<>(selfRegConfigResourceAssembler.toResource(
                    clientTeamSelfRegConfiguration),
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    /**
     * POST to save client team self registration configuration
     *
     * @param teamId    for the self reg configuration
     * @param clientTeamSelfRegConfiguration the user client team self reg configuration that needs to save or update
     * @return a ClientTeamSelfRegConfiguration response entity, with HttpStatus.CREATED or HttpStatus.NOT_ACCEPTABLE
     */
    @RequestMapping(value = "/teams/{teamId}/self_reg_configuration", method = RequestMethod.POST)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamSelfRegConfigurationResource> save(
            @PathVariable("teamId") Long teamId,
            @RequestBody ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
        clientTeamSelfRegConfiguration.setTeam(new Team(teamId));
        ClientTeamSelfRegConfiguration selfRegConfiguration = clientTeamSelfRegConfigurationService.create(clientTeamSelfRegConfiguration);

        if (selfRegConfiguration != null) {
            return new ResponseEntity<>(
                    selfRegConfigResourceAssembler
                            .toResource(selfRegConfiguration),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * PUT to update client team self registration configuration
     *
     * @param teamId    for the self reg configuration
     * @param clientTeamSelfRegConfiguration the user client team self reg configuration that needs to save or update
     * @return a ClientTeamSelfRegConfiguration response entity, with HttpStatus.OK or HttpStatus.NOT_ACCEPTABLE
     */
    @RequestMapping(value = "/teams/{teamId}/self_reg_configuration", method = RequestMethod.PUT)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientTeamSelfRegConfigurationResource> update(
            @PathVariable("teamId") Long teamId,
            @RequestBody ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
        clientTeamSelfRegConfiguration.setTeam(new Team(teamId));
        ClientTeamSelfRegConfiguration selfRegConfiguration = clientTeamSelfRegConfigurationService.update(clientTeamSelfRegConfiguration);

        if (selfRegConfiguration != null) {
            return new ResponseEntity<>(
                    selfRegConfigResourceAssembler
                            .toResource(selfRegConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
}