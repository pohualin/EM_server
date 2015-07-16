package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.InfoHeaderConfigSearchFilter;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.service.InfoHeaderConfigService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patient_self_reg.InfoHeaderConfigPage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
