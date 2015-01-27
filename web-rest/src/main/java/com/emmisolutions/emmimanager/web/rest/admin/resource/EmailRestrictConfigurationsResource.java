package com.emmisolutions.emmimanager.web.rest.admin.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.EmailRestrictConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.EmailRestrictConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.EmailRestrictConfigurationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

/**
 * EmailRestrictConfiguration REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class EmailRestrictConfigurationsResource {

    @Resource
    EmailRestrictConfigurationService emailRestrictConfigurationService;

    @Resource
    EmailRestrictConfigurationResourceAssembler emailRestrictConfigurationAssembler;

    /**
     * Get a page of EmailRestrictConfiguration for a Client
     * 
     * @param clientId
     *            to lookup
     * @param pageable
     *            to use
     * @param sort
     *            to use
     * @param assembler
     *            to use
     * @return an EmailRestrictConfigurationPage
     */
    @RequestMapping(value = "/client/{id}/email_restrict_configurations", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "emailEnding,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<EmailRestrictConfigurationPage> list(
            @PathVariable("id") Long clientId,
            @PageableDefault(size = 10, sort = "emailEnding", direction = Direction.ASC) Pageable pageable,
            @SortDefault(sort = "emailEnding", direction = Direction.ASC) Sort sort,
            PagedResourcesAssembler<EmailRestrictConfiguration> assembler) {

        Page<EmailRestrictConfiguration> page = emailRestrictConfigurationService
                .getByClient(pageable, new Client(clientId));

        if (page.hasContent()) {
            // create a EmailRestrictConfigurationPage containing the response
            return new ResponseEntity<>(new EmailRestrictConfigurationPage(
                    assembler.toResource(page,
                            emailRestrictConfigurationAssembler), page),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a brand new EmailRestrictConfiguration
     * 
     * @param clientId
     *            to assign
     * @param emailRestrictConfiguration
     *            to create
     * @return a created EmailRestrictConfiguration
     */
    @RequestMapping(value = "/client/{id}/email_restrict_configurations", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<EmailRestrictConfigurationResource> create(
            @PathVariable("id") Long clientId,
            @RequestBody EmailRestrictConfiguration emailRestrictConfiguration) {
        emailRestrictConfiguration.setClient(new Client(clientId));
        EmailRestrictConfiguration created = emailRestrictConfigurationService
                .create(emailRestrictConfiguration);
        if (created != null) {
            return new ResponseEntity<>(
                    emailRestrictConfigurationAssembler.toResource(created),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Reload an existing EmailRestrictConfiguartion
     * 
     * @param id
     *            to reload
     * @return an existing EmailRestrictConfiguartion
     */
    @RequestMapping(value = "/email_restrict_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<EmailRestrictConfigurationResource> get(
            @PathVariable("id") Long id) {
        EmailRestrictConfiguration emailRestrictConfiguration = emailRestrictConfigurationService
                .reload(new EmailRestrictConfiguration(id));
        if (emailRestrictConfiguration != null) {
            return new ResponseEntity<EmailRestrictConfigurationResource>(
                    emailRestrictConfigurationAssembler
                            .toResource(emailRestrictConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Update an existing EmailRestrictConfiguration
     * 
     * @param id
     *            to reload
     * @param emailRestrictConfiguration
     *            contains updated information
     * @return an updated EmailRestrictConfiguration
     */
    @RequestMapping(value = "/email_restrict_configuration/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public ResponseEntity<EmailRestrictConfigurationResource> update(
            @PathVariable("id") Long id,
            @RequestBody EmailRestrictConfiguration emailRestrictConfiguration) {
        EmailRestrictConfiguration updated = emailRestrictConfigurationService
                .update(emailRestrictConfiguration);
        if (updated != null) {
            return new ResponseEntity<>(
                    emailRestrictConfigurationAssembler.toResource(updated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete an existing EmailRestrictConfiguration
     * 
     * @param id
     *            to delete
     */
    @RequestMapping(value = "/email_restrict_configuration/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER" })
    public void delete(@PathVariable Long id) {
        emailRestrictConfigurationService
                .delete(new EmailRestrictConfiguration(id));
    }

}
