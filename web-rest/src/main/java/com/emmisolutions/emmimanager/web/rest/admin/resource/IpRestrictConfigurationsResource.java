package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.service.IpRestrictConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.IpRestrictConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.IpRestrictConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.IpRestrictConfigurationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * IpRestrictConfiguration REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class IpRestrictConfigurationsResource {

    @Resource
    IpRestrictConfigurationService ipRestrictConfigurationService;

    @Resource
    IpRestrictConfigurationResourceAssembler ipRestrictConfigurationAssembler;

    /**
     * Get a page of IpRestrictConfiguration for a Client
     * 
     * @param clientId
     *            to lookup
     * @param pageable
     *            to use
     * @param sort
     *            to use
     * @param assembler
     *            to use
     * @return an IpRestrictConfigurationPage
     */
    @RequestMapping(value = "/client/{id}/ip_restrict_configurations", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "ipRangeStart,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<IpRestrictConfigurationPage> list(
            @PathVariable("id") Long clientId,
            @PageableDefault(size = 10, sort = "ipRangeStart", direction = Direction.ASC) Pageable pageable,
            @SortDefault(sort = "ipRangeStart", direction = Direction.ASC) Sort sort,
            PagedResourcesAssembler<IpRestrictConfiguration> assembler) {

        Page<IpRestrictConfiguration> page = ipRestrictConfigurationService
                .getByClient(pageable, new Client(clientId));

        if (page.hasContent()) {
            // create a IpRestrictConfigurationPage containing the response
            return new ResponseEntity<>(
                    new IpRestrictConfigurationPage(assembler.toResource(page,
                            ipRestrictConfigurationAssembler), page),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a brand new IpRestrictConfiguration
     * 
     * @param clientId
     *            to assign
     * @param ipRestrictConfiguration
     *            to create
     * @return a created IpRestrictConfiguration
     */
    @RequestMapping(value = "/client/{id}/ip_restrict_configurations", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<IpRestrictConfigurationResource> create(
            @PathVariable("id") Long clientId,
            @RequestBody IpRestrictConfiguration ipRestrictConfiguration) {
        ipRestrictConfiguration.setClient(new Client(clientId));
        IpRestrictConfiguration created = ipRestrictConfigurationService
                .create(ipRestrictConfiguration);
        if (created != null) {
            return new ResponseEntity<>(
                    ipRestrictConfigurationAssembler.toResource(created),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Reload an existing IpRestrictConfiguartion
     * 
     * @param id
     *            to reload
     * @return an existing IpRestrictConfiguartion
     */
    @RequestMapping(value = "/ip_restrict_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<IpRestrictConfigurationResource> get(
            @PathVariable("id") Long id) {
        IpRestrictConfiguration ipRestrictConfiguration = ipRestrictConfigurationService
                .reload(new IpRestrictConfiguration(id));
        if (ipRestrictConfiguration != null) {
            return new ResponseEntity<>(
                    ipRestrictConfigurationAssembler
                            .toResource(ipRestrictConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Update an existing IpRestrictConfiguration
     * 
     * @param id
     *            to reload
     * @param ipRestrictConfiguration
     *            contains updated information
     * @return an updated IpRestrictConfiguration
     */
    @RequestMapping(value = "/ip_restrict_configuration/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public ResponseEntity<IpRestrictConfigurationResource> update(
            @PathVariable("id") Long id,
            @RequestBody IpRestrictConfiguration ipRestrictConfiguration) {
        IpRestrictConfiguration updated = ipRestrictConfigurationService
                .update(ipRestrictConfiguration);
        if (updated != null) {
            return new ResponseEntity<>(
                    ipRestrictConfigurationAssembler.toResource(updated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete an existing IpRestrictConfiguration
     * 
     * @param id
     *            to delete
     */
    @RequestMapping(value = "/ip_restrict_configuration/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER" })
    public void delete(@PathVariable Long id) {
        ipRestrictConfigurationService.delete(new IpRestrictConfiguration(id));
    }

}
