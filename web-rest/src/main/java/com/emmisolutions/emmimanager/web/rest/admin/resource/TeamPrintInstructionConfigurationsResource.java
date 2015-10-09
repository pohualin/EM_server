package com.emmisolutions.emmimanager.web.rest.admin.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.service.TeamPrintInstructionConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.TeamPrintInstructionConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.TeamPrintInstructionConfigurationResourceAssembler;

/**
 * TeamPrintInstructionConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class TeamPrintInstructionConfigurationsResource {
    @Resource
    TeamPrintInstructionConfigurationService teamPrintInstructionConfigurationService;

    @Resource
    TeamPrintInstructionConfigurationResourceAssembler teamPrintInstructionConfigurationAssembler;

    /**
     * Get PrintInstructionConfiguration for a Team
     * 
     * @param teamId
     *            to lookup
     * @return TeamPrintInstructionConfigurationResource along with
     *         HTTPStatus.OK or HTTPStatus.NO_CONTENT
     */
    @RequestMapping(value = "/teams/{teamId}/print_instruction_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<TeamPrintInstructionConfigurationResource> getPrintInstructionConfiguration(
            @PathVariable("teamId") Long teamId) {
        TeamPrintInstructionConfiguration teamPrintInstructionConfiguration = teamPrintInstructionConfigurationService
                .get(new Team(teamId));

        if (teamPrintInstructionConfiguration != null) {
            return new ResponseEntity<>(
                    teamPrintInstructionConfigurationAssembler
                            .toResource(teamPrintInstructionConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a TeamPrintInstructionConfiguration
     * 
     * @param teamId
     *            to use
     * @param configuration
     *            to create
     * @return TeamPrintInstructionConfigurationResource contains created
     *         TeamPrintInstructionConfiguration
     */
    @RequestMapping(value = "/teams/{teamId}/print_instruction_configuration", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<TeamPrintInstructionConfigurationResource> create(
            @PathVariable("teamId") Long teamId,
            @RequestBody TeamPrintInstructionConfiguration configuration) {
        TeamPrintInstructionConfiguration created = teamPrintInstructionConfigurationService
                .create(configuration);
        if (created != null) {
            return new ResponseEntity<>(
                    teamPrintInstructionConfigurationAssembler
                            .toResource(created),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get an existing TeamPrintInstructionConfiguration
     * 
     * @param id
     *            to lookup
     * @return TeamPrintInstructionConfigurationResource along with
     *         HTTPStatus.OK or HTTPStatus.NO_CONTENT
     */
    @RequestMapping(value = "/team_print_instruction_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<TeamPrintInstructionConfigurationResource> get(
            @PathVariable("id") Long id) {
        TeamPrintInstructionConfiguration configuration = teamPrintInstructionConfigurationService
                .reload(new TeamPrintInstructionConfiguration(id));

        if (configuration != null) {
            return new ResponseEntity<>(
                    teamPrintInstructionConfigurationAssembler
                            .toResource(configuration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Update an existing TeamPrintInstructionConfiguration
     * 
     * @param id
     *            to use
     * @param configuration
     *            to update
     * @return TeamPrintInstructionConfigurationResource contains an updated
     *         TeamPrintInstructionConfiguration
     */
    @RequestMapping(value = "/team_print_instruction_configuration/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<TeamPrintInstructionConfigurationResource> update(
            @PathVariable("id") Long id,
            @RequestBody TeamPrintInstructionConfiguration configuration) {
        TeamPrintInstructionConfiguration updated = teamPrintInstructionConfigurationService
                .update(configuration);
        if (updated != null) {
            return new ResponseEntity<>(
                    teamPrintInstructionConfigurationAssembler
                            .toResource(updated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
