package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.PatientService;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ReferenceData;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.patient.PatientResourcePage;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Patients REST API
 */
@RestController
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class PatientsResource {

    @Resource
    PatientService patientService;

    @Resource
    ClientService clientService;

    @Resource
    PatientResourceAssembler patientResourceAssembler;

    @XmlElement(name = "genders")
    @XmlElementWrapper(name = "genders")
    private Gender[] genders = Gender.values();


    @RequestMapping(value = "/clients/{clientId}/patients", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    public ResponseEntity<PatientResource> create(@PathVariable("clientId") Long clientId, @RequestBody Patient patient) {
        patient.setClient(clientService.reload(new Client(clientId)));
        return new ResponseEntity<PatientResource>(patientResourceAssembler.toResource(patientService.create(patient)), HttpStatus.OK);
    }

    /**
     * GET to Retrieve reference data about clients.
     *
     * @return ReferenceData
     */
    @RequestMapping(value = "/patients/ref", method = RequestMethod.GET)
    public PatientReferenceData getReferenceData() {

        return new PatientReferenceData();
    }

    @RequestMapping(value = "/clients/{clientId}/patients/", method = RequestMethod.GET)
    public ResponseEntity<PatientResource> get(@PathVariable("clientId") Long clientId,
                                               @RequestParam(value = "patientId", required = false) Long patientId) {
        if (patientId != null) {
            Patient toLoad = new Patient();
            toLoad.setId(patientId);
            return new ResponseEntity<PatientResource>(patientResourceAssembler.toResource(patientService.reload(toLoad)), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * GET for searching for providers
     *
     * @param page      paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param name      filter
     * @param status    a filter for providers
     * @return ProviderResource
     */
    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<PatientResourcePage> list(
            @PageableDefault(size = 10, sort = "lastName") Pageable page,
            @SortDefault(sort = "lastName") Sort sort,
            PagedResourcesAssembler<Patient> assembler,
            @RequestParam(value = "name", required = false) String name) {

        PatientSearchFilter filter = new PatientSearchFilter(name);

        Page<Patient> patientsPage = patientService.list(page, filter);

        if (patientsPage.hasContent()) {
            return new ResponseEntity<>(new PatientResourcePage(assembler.toResource(patientsPage, patientResourceAssembler), patientsPage, filter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
