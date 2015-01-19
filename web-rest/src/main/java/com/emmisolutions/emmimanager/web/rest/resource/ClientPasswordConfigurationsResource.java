package com.emmisolutions.emmimanager.web.rest.resource;

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
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.web.rest.model.configuration.ClientPasswordConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.model.configuration.ClientPasswordConfigurationResourceAssembler;

/**
 * ClientPasswordConfiguration REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class ClientPasswordConfigurationsResource {

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Resource
    ClientPasswordConfigurationResourceAssembler clientPasswordConfigurationResourceAssembler;

    /**
     * Delete an existing ClientPasswordConfiguration
     * 
     * @param clientPasswordConfigurationId
     *            to delete
     * @return a default ClientPasswordConfiguration
     */
    @RequestMapping(value = "/client_password_configuration/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientPasswordConfigurationResource> delete(
            @PathVariable("id") Long clientPasswordConfigurationId) {

        ClientPasswordConfiguration returned = clientPasswordConfigurationService
                .delete(new ClientPasswordConfiguration(
                        clientPasswordConfigurationId));

        if (returned == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    clientPasswordConfigurationResourceAssembler
                            .toResource(returned),
                    HttpStatus.OK);
        }
    }

    /**
     * Reload an existing ClientPasswordConfiguration by id
     * 
     * @param clientPasswordConfigurationId
     *            to use
     * @return an existing ClientPasswordConfiguration
     */
    @RequestMapping(value = "/client_password_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientPasswordConfigurationResource> get(
            @PathVariable("id") Long clientPasswordConfigurationId) {
        ClientPasswordConfiguration clientPasswordConfiguration = clientPasswordConfigurationService
                .reload(new ClientPasswordConfiguration(
                        clientPasswordConfigurationId));
        if (clientPasswordConfiguration != null) {
            return new ResponseEntity<>(
                    clientPasswordConfigurationResourceAssembler
                            .toResource(clientPasswordConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get ClientPasswordConfiguration by Client
     * 
     * @param clientId
     *            to lookup
     * @return a ClientPasswordConfiguration
     */
    @RequestMapping(value = "/client/{clientId}/password_configuration", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientPasswordConfigurationResource> getByClient(
            @PathVariable("clientId") Long clientId) {
        ClientPasswordConfiguration clientPasswordConfiguration = clientPasswordConfigurationService
                .get(new Client(clientId));
        if (clientPasswordConfiguration != null) {
            return new ResponseEntity<>(
                    clientPasswordConfigurationResourceAssembler
                            .toResource(clientPasswordConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Save ClientPasswordConfiguration
     * 
     * @param clientId
     *            to use
     * @param clientPasswordConfiguration
     *            to save
     * @return saved ClientPasswordConfigurationResource
     */
    @RequestMapping(value = "/client/{clientId}/password_configuration", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<ClientPasswordConfigurationResource> save(
            @PathVariable("clientId") Long clientId,
            @RequestBody ClientPasswordConfiguration clientPasswordConfiguration) {

        ClientPasswordConfiguration saved = clientPasswordConfigurationService
                .save(clientPasswordConfiguration);

        if (saved == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                    clientPasswordConfigurationResourceAssembler
                            .toResource(saved),
                    HttpStatus.CREATED);
        }
    }
}
