package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.web.rest.admin.model.salesforce.SalesForceSearchResponseResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Access to SalesForce resources
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
)
public class SalesForceResource {

    @Resource
    SalesForceService salesForceService;

    /**
     * Find SalesForce objects
     *
     * @param searchString the name to search for
     * @return a SalesForceSearchResponseResource
     */
    @RequestMapping(value = "/sf/find", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_CLIENT_CREATE", "PERM_CLIENT_EDIT"})
    public ResponseEntity<SalesForceSearchResponseResource> find(@RequestParam(value = "q", required = false) String searchString) {
        SalesForceSearchResponse searchResponse = salesForceService.find(searchString);
        return findAccounts(searchString, searchResponse, false);
    }

    /**
     * Find SalesForce objects in a team
     *
     * @param searchString to find
     * @return SalesForceSearchResponseResource object
     */
    @RequestMapping(value = "/teams/sf/find", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_TEAM_CREATE", "PERM_TEAM_EDIT"})
    public ResponseEntity<SalesForceSearchResponseResource> findForTeam(@RequestParam(value = "q", required = false) String searchString) {
        SalesForceSearchResponse searchResponse = salesForceService.findForTeam(searchString);
        return findAccounts(searchString, searchResponse, true);
    }

    /**
     * Find salesforce accounts
     *
     * @param searchString using this
     * @return the account resources
     */
    private ResponseEntity<SalesForceSearchResponseResource> findAccounts(String searchString,
                                                                          SalesForceSearchResponse searchResponse, boolean forTeam) {
        if (searchResponse != null && !CollectionUtils.isEmpty(searchResponse.getAccounts())) {
            return new ResponseEntity<>(new SalesForceSearchResponseResource(searchResponse, searchString, forTeam), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
