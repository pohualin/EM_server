package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.ClientPasswordConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.ClientPasswordConfigurationResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

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
     */
    @RequestMapping(value = "/client_password_configuration/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public void delete(@PathVariable("id") Long clientPasswordConfigurationId) {
        clientPasswordConfigurationService
                .delete(new ClientPasswordConfiguration(
                        clientPasswordConfigurationId));
    }

    /**
     * Reload an existing ClientPasswordConfiguration by id
     * 
     * @param clientPasswordConfigurationId
     *            to use
     * @return an existing ClientPasswordConfiguration
     */
    @RequestMapping(value = "/client_password_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
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
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
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
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
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
