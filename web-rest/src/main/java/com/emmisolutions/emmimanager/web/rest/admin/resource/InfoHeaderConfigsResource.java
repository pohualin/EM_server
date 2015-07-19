package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.service.InfoHeaderConfigService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigResource;
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
public class InfoHeaderConfigsResource {

    @Resource
    InfoHeaderConfigService infoHeaderConfigService;

    @Resource
    InfoHeaderConfigAssembler infoHeaderConfigAssembler;

    @RequestMapping(value = "/patient_self_reg/{patientSelfRegConfigId}/info_header_config", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "50", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<InfoHeaderConfigPage> listByPatientSelfRegConfig(
            @PageableDefault(size = 50) Pageable pageable,
            PagedResourcesAssembler<InfoHeaderConfig> assembler,
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId) {

        InfoHeaderConfigSearchFilter infoHeaderConfigSearchFilter = new InfoHeaderConfigSearchFilter(new PatientSelfRegConfig(patientSelfRegConfigId));

        Page<InfoHeaderConfig> infoHeaderConfigPage = infoHeaderConfigService.list(pageable, infoHeaderConfigSearchFilter);

        if (infoHeaderConfigPage.hasContent()) {
            return new ResponseEntity<>(new InfoHeaderConfigPage(assembler.toResource(infoHeaderConfigPage, infoHeaderConfigAssembler), infoHeaderConfigPage, infoHeaderConfigSearchFilter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/patient_self_reg/{patientSelfRegConfigId}/info_header_config", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<InfoHeaderConfigResource> save(
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId,
            @RequestBody InfoHeaderConfig infoHeaderConfig) {
        infoHeaderConfig.setPatientSelfRegConfig(new PatientSelfRegConfig(patientSelfRegConfigId));
        InfoHeaderConfig infoHeaderConfigCreated = infoHeaderConfigService.create(infoHeaderConfig);

        if (infoHeaderConfigCreated != null) {
            return new ResponseEntity<>(
                    infoHeaderConfigAssembler
                            .toResource(infoHeaderConfig),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/patient_self_reg/{patientSelfRegConfigId}/info_header_config", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<InfoHeaderConfigResource> update(
            @PathVariable("patientSelfRegConfigId") Long patientSelfRegConfigId,
            @RequestBody InfoHeaderConfig infoHeaderConfig) {
        infoHeaderConfig.setPatientSelfRegConfig(new PatientSelfRegConfig(patientSelfRegConfigId));
        InfoHeaderConfig infoHeaderConfigCreated = infoHeaderConfigService.update(infoHeaderConfig);

        if (infoHeaderConfigCreated != null) {
            return new ResponseEntity<>(
                    infoHeaderConfigAssembler
                            .toResource(infoHeaderConfig),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    /**
     * Get a info header configuration by given id
     *
     * @param id to load
     * @return
     */
    @RequestMapping(value = "/info_header_config/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<InfoHeaderConfigResource> getById(@PathVariable("id") Long id) {

        InfoHeaderConfig toReload = new InfoHeaderConfig();
        toReload.setId(id);
        InfoHeaderConfig infoHeaderConfig = infoHeaderConfigService.reload(toReload);

        if (infoHeaderConfig != null) {
            return new ResponseEntity<>(infoHeaderConfigAssembler.toResource(
                    infoHeaderConfig),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
