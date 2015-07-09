package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.salesforce.CaseForm;
import com.emmisolutions.emmimanager.model.salesforce.CaseSaveResult;
import com.emmisolutions.emmimanager.model.salesforce.CaseType;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.web.rest.admin.model.case_management.CaseFormResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.case_management.CaseTypeResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.case_management.ReferenceData;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Resources for case creation (i.e. salesforce cases)
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
)
public class CasesResource {

    @Resource
    SalesForceService salesForceService;

    @Resource
    ResourceAssembler<CaseType, CaseTypeResource> caseTypeResourceAssembler;

    @Resource
    ResourceAssembler<CaseForm, CaseFormResource> caseFormResourceAssembler;

    /**
     * Retrieves reference data from
     *
     * @return OK (200): containing a ReferenceData object
     */
    @RequestMapping(value = "/cases/reference_data", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ReferenceData> referenceData() {
        List<CaseType> caseTypes = salesForceService.possibleCaseTypes();
        List<CaseTypeResource> caseTypeResources = new ArrayList<>();
        for (CaseType caseType : caseTypes) {
            caseTypeResources.add(caseTypeResourceAssembler.toResource(caseType));
        }
        return new ResponseEntity<>(new ReferenceData(caseTypeResources), HttpStatus.OK);
    }

    /**
     * Retrieves a blank CaseForm for the passed type
     *
     * @param typeId of form to retrieve
     * @return OK (200): CaseFormResource
     * NOT_FOUNd (404): When the type doesn't have a corresponding form
     */
    @RequestMapping(value = "/cases/new/{typeId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<CaseFormResource> create(@PathVariable String typeId) {
        CaseForm caseForm = salesForceService.blankFormFor(new CaseType(typeId));
        if (caseForm != null) {
            return new ResponseEntity<>(caseFormResourceAssembler.toResource(caseForm), OK);
        }
        return new ResponseEntity<>(NOT_FOUND);
    }

    /**
     * Saves a case form
     *
     * @param caseForm to be saved
     * @return OK (200): CaseSaveResult object with new id
     * NOT_ACCEPTABLE (406): CaseSaveResult object with error messages populated
     */
    @RequestMapping(value = "/cases",
            method = RequestMethod.POST,
            consumes = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<CaseSaveResult> save(@RequestBody CaseForm caseForm) {
        CaseSaveResult caseSaveResult = salesForceService.saveCase(caseForm);
        return new ResponseEntity<>(caseSaveResult, caseSaveResult.isSuccess() ? OK : NOT_ACCEPTABLE);
    }


}
