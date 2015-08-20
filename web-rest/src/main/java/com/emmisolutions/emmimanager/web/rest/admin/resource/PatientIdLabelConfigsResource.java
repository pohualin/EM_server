package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.service.PatientIdLabelConfigService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.PatientIdLabelConfigAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.PatientIdLabelConfigPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.PatientIdLabelConfigResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class PatientIdLabelConfigsResource {

    @Resource
    PatientIdLabelConfigService patientIdLabelConfigService;

    @Resource
    PatientIdLabelConfigAssembler patientIdLabelConfigAssembler;

    /**
     * GET for a page of patient id label configuration objects based on search criteria
     * @param pageable  page size
     * @param assembler
     * @param patientSelfRegConfigId
     * @return a page of patient id label configuration objects
     */
    @RequestMapping(value = "/patient_self_reg/{patientSelfRegConfigId}/patient_id_label", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<PatientIdLabelConfigPage> listByPatientSelfRegConfig(
            @PageableDefault(size = 50) Pageable pageable,
            PagedResourcesAssembler<PatientIdLabelConfig> assembler,
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId) {

        PatientIdLabelConfigSearchFilter patientIdLabelConfigSearchFilter = new PatientIdLabelConfigSearchFilter(new PatientSelfRegConfig(patientSelfRegConfigId));

        Page<PatientIdLabelConfig> patientIdLabelConfigPage = patientIdLabelConfigService.list(pageable, patientIdLabelConfigSearchFilter);

        if (patientIdLabelConfigPage.hasContent()) {
            return new ResponseEntity<>(new PatientIdLabelConfigPage(assembler.toResource(patientIdLabelConfigPage, patientIdLabelConfigAssembler), patientIdLabelConfigPage, patientIdLabelConfigSearchFilter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * POST to save a given patient id label config
     * @param patientSelfRegConfigId
     * @param patientIdLabelConfig to save
     * @return patient if label config resource if created, NOT_ACCEPTABLE if errors out
     */
    @RequestMapping(value = "/patient_self_reg/{patientSelfRegConfigId}/patient_id_label", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientIdLabelConfigResource> save(
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId,
            @RequestBody PatientIdLabelConfig patientIdLabelConfig) {
        patientIdLabelConfig.setPatientSelfRegConfig(new PatientSelfRegConfig(patientSelfRegConfigId));
        PatientIdLabelConfig patientIdLabelConfigCreated = patientIdLabelConfigService.create(patientIdLabelConfig);

        if (patientIdLabelConfigCreated != null) {
            return new ResponseEntity<>(
                    patientIdLabelConfigAssembler
                            .toResource(patientIdLabelConfigCreated),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * PUT to update a given patient id label config
     * @param patientIdLabelConfigId
     * @param patientSelfRegConfigId
     * @param patientIdLabelConfig to update
     * @return a patient id label config resource
     */
    @RequestMapping(value = "/patient_self_reg_config/{patientSelfRegConfigId}/patient_id_label/{patientIdLabelConfigId}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientIdLabelConfigResource> update(
            @PathVariable("patientIdLabelConfigId") Long patientIdLabelConfigId,
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId,
            @RequestBody PatientIdLabelConfig patientIdLabelConfig) {
        patientIdLabelConfig.setPatientSelfRegConfig(new PatientSelfRegConfig(patientSelfRegConfigId));
        PatientIdLabelConfig patientIdLabelConfigUpdated = patientIdLabelConfigService.update(patientIdLabelConfig);

        if (patientIdLabelConfigUpdated != null) {
            return new ResponseEntity<>(
                    patientIdLabelConfigAssembler
                            .toResource(patientIdLabelConfigUpdated),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    /**
     * Get a patient id label configuration by given id
     *
     * @param patientIdLabelConfigId to load
     * @return
     */
    @RequestMapping(value = "/patient_self_reg_config/{patientSelfRegConfigId}/patient_id_label/{patientIdLabelConfigId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientIdLabelConfigResource> getById(@PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId,
                                                                @PathVariable("patientIdLabelConfigId") Long patientIdLabelConfigId) {

        PatientIdLabelConfig toReload = new PatientIdLabelConfig();
        toReload.setId(patientIdLabelConfigId);
        PatientIdLabelConfig patientIdLabelConfig = patientIdLabelConfigService.reload(toReload);

        if (patientIdLabelConfig != null) {
            return new ResponseEntity<>(patientIdLabelConfigAssembler.toResource(
                    patientIdLabelConfig),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
