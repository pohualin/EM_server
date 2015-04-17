package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.EmailRestrictConfigurationPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.configuration.EmailRestrictConfigurationResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * EmailRestrictConfiguration REST API
 */
@RestController
@RequestMapping(value = "/webapi-client", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })

public class UserEmailRestrictConfigurationsResource {
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
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "emailEnding,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<EmailRestrictConfigurationPage> list(
            @PathVariable("id") Long clientId,
            @PageableDefault(size = 10, sort = "emailEnding", direction = Sort.Direction.ASC) Pageable pageable,
            @SortDefault(sort = "emailEnding", direction = Sort.Direction.ASC) Sort sort,
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
}
