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

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.ClientRestrictConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.ClientRestrictConfigurationResourceAssembler;

/**
 * ClientRestrictConfiguration REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class ClientRestrictConfigurationsResource {

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    @Resource
    ClientRestrictConfigurationResourceAssembler clientRestrictConfigurationAssembler;

    /**
     * Get ClientRestrictConfiguration by Client
     * 
     * @param clientId
     *            to lookup
     * @return an existing ClientRestrictCongifuration or create one if non
     *         exists
     */
    @RequestMapping(value = "/clients/{clientId}/client_restrict_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientRestrictConfigurationResource> getByClient(
            @PathVariable Long clientId) {
        ClientRestrictConfiguration found = clientRestrictConfigurationService
                .getByClient(new Client(clientId));
        if (found != null) {
            // found a clientRestrictConfiguration successfully
            return new ResponseEntity<ClientRestrictConfigurationResource>(
                    clientRestrictConfigurationAssembler.toResource(found),
                    HttpStatus.OK);
        } else {
            // nothing found, create a new one but don't save
            ClientRestrictConfiguration created = new ClientRestrictConfiguration();
            created.setClient(new Client(clientId));
            return new ResponseEntity<ClientRestrictConfigurationResource>(
                    clientRestrictConfigurationAssembler.toResource(created),
                    HttpStatus.OK);
        }
    }

    /**
     * Get an ClientRestrictConfiguration by id
     * 
     * @param id
     *            to reload
     * @return an existing ClientRestrictConfiguration
     */
    @RequestMapping(value = "/client_restrict_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientRestrictConfigurationResource> get(
            @PathVariable("id") Long id) {
        ClientRestrictConfiguration clientRestrictConfiguration = clientRestrictConfigurationService
                .reload(new ClientRestrictConfiguration(id));
        if (clientRestrictConfiguration != null) {
            return new ResponseEntity<ClientRestrictConfigurationResource>(
                    clientRestrictConfigurationAssembler
                            .toResource(clientRestrictConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Create a new ClientRestrictConfiguration for a client
     * 
     * @param clientId
     *            to use
     * @param clientRestrictConfiguration
     *            to save
     * @return saved ClientRestrictConfiguration
     */
    @RequestMapping(value = "/clients/{clientId}/client_restrict_configuration", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientRestrictConfigurationResource> save(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientRestrictConfiguration clientRestrictConfiguration) {

        ClientRestrictConfiguration saved = clientRestrictConfigurationService
                .create(clientRestrictConfiguration);
        if (saved != null) {
            return new ResponseEntity<>(
                    clientRestrictConfigurationAssembler.toResource(saved),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing ClientRestrictConfiguration
     * 
     * @param id
     *            to reload
     * @param clientRestrictConfiguration
     *            to update
     * @return an updated ClientRestrictConfiguration
     */
    @RequestMapping(value = "/client_restrict_configuration/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientRestrictConfigurationResource> update(
            @PathVariable("id") Long id,
            @RequestBody ClientRestrictConfiguration clientRestrictConfiguration) {

        ClientRestrictConfiguration updated = clientRestrictConfigurationService
                .update(clientRestrictConfiguration);
        if (updated != null) {
            return new ResponseEntity<>(
                    clientRestrictConfigurationAssembler.toResource(updated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
