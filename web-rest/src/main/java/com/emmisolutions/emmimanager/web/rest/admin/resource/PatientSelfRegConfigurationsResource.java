package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Language;
import com.emmisolutions.emmimanager.model.PatientSelfRegConfig;
import com.emmisolutions.emmimanager.model.Strings;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.PatientSelfRegConfigurationService;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.PatientSelfRegConfigurationResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.PatientSelfRegReferenceData;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.LanguageResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.LanguageResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.StringsResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration.patientSelfReg.StringsResourcePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * PatientSelfRegConfigurationsResource REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class PatientSelfRegConfigurationsResource {

    @Resource
    PatientSelfRegConfigurationService patientSelfRegConfigurationService;

    @Resource(name = "patientSelfRegConfigurationResourceAssembler")
    ResourceAssembler<PatientSelfRegConfig, PatientSelfRegConfigurationResource> patientSelfRegConfigResourceAssembler;

    @Resource
    LanguageResourceAssembler languageResourceAssembler;

    @Resource
    StringsResourceAssembler stringsResourceAssembler;

    /**
     * Get a patient self-reg configuration by given id
     *
     * @param id to load
     * @return
     */
    @RequestMapping(value = "/patient_self_reg_configuration/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientSelfRegConfigurationResource> getById(@PathVariable("id") Long id) {

        PatientSelfRegConfig toReload = new PatientSelfRegConfig();
        toReload.setId(id);
        PatientSelfRegConfig patientSelfRegConfig = patientSelfRegConfigurationService.reload(toReload);

        if (patientSelfRegConfig != null) {
            return new ResponseEntity<>(patientSelfRegConfigResourceAssembler.toResource(
                    patientSelfRegConfig),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Find patient self registration configuration if there is any
     *
     * @param teamId    for the self reg configuration
     * @param assembler makes a page for PatientSelfRegConfig
     * @return a PatientSelfRegConfig response entity
     */
    @RequestMapping(value = "/teams/{teamId}/patient_self_reg_configuration", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientSelfRegConfigurationResource> findByTeam(
            @PathVariable("teamId") Long teamId,
            PagedResourcesAssembler<PatientSelfRegConfig> assembler) {

        PatientSelfRegConfig patientSelfRegConfig = patientSelfRegConfigurationService.findByTeam(new Team(teamId));

        if (patientSelfRegConfig != null) {
            return new ResponseEntity<>(patientSelfRegConfigResourceAssembler.toResource(
                    patientSelfRegConfig),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    /**
     * POST to save patient self registration configuration
     *
     * @param teamId               for the self reg configuration
     * @param patientSelfRegConfig the patient self reg configuration that needs to save or update
     * @return a PatientSelfRegConfig response entity, with HttpStatus.CREATED or HttpStatus.NOT_ACCEPTABLE
     */
    @RequestMapping(value = "/teams/{teamId}/patient_self_reg_configuration", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientSelfRegConfigurationResource> save(
            @PathVariable("teamId") Long teamId,
            @RequestBody PatientSelfRegConfig patientSelfRegConfig) {
        patientSelfRegConfig.setTeam(new Team(teamId));
        PatientSelfRegConfig patientSelfRegConfiguration = patientSelfRegConfigurationService.create(patientSelfRegConfig);

        if (patientSelfRegConfiguration != null) {
            return new ResponseEntity<>(
                    patientSelfRegConfigResourceAssembler
                            .toResource(patientSelfRegConfiguration),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * PUT to update patient self registration configuration
     *
     * @param teamId               for the self reg configuration
     * @param patientSelfRegConfig the patient self reg configuration that needs to save or update
     * @return a PatientSelfRegConfig response entity, with HttpStatus.OK or HttpStatus.NOT_ACCEPTABLE
     */
    @RequestMapping(value = "/teams/{teamId}/patient_self_reg_configuration", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<PatientSelfRegConfigurationResource> update(
            @PathVariable("teamId") Long teamId,
            @RequestBody PatientSelfRegConfig patientSelfRegConfig) {
        patientSelfRegConfig.setTeam(new Team(teamId));
        PatientSelfRegConfig selfRegConfiguration = patientSelfRegConfigurationService.update(patientSelfRegConfig);

        if (selfRegConfiguration != null) {
            return new ResponseEntity<>(
                    patientSelfRegConfigResourceAssembler
                            .toResource(selfRegConfiguration),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * GET to Retrieve reference data for patients self reg config.
     *
     * @return PatientSelfRegReferenceData
     */
    @RequestMapping(value = "/patients_self_reg/ref", method = RequestMethod.GET)
    public PatientSelfRegReferenceData getReferenceData() {
        return new PatientSelfRegReferenceData(patientSelfRegConfigurationService.getAllPatientIdLabelTypes());
    }


    /**
     * GET to retrieve all available languages
     *
     * @param pageable  the page specification
     * @param assembler
     * @return language resource page
     */
    @RequestMapping(value = "/availableLanguages", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<LanguageResourcePage> getAllAvailableLanguages(@PageableDefault(size = 1) Pageable pageable,
                                                                         PagedResourcesAssembler<Language> assembler) {

        Page<Language> languagePage = patientSelfRegConfigurationService.findAllAvailableLanguages(pageable);

        if (languagePage.hasContent()) {
            return new ResponseEntity<>(new LanguageResourcePage(assembler.toResource(languagePage, languageResourceAssembler), languagePage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Finds a page of stringsresource by given keypath
     *
     * @param key       the search specification
     * @param assembler
     * @param pageable
     * @return a page of stringsresource
     */
    @RequestMapping(value = "/translations", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<StringsResourcePage> findByString(
            @RequestParam(value = "key", required = false) String key,
            PagedResourcesAssembler<Strings> assembler,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Strings> stringsPage = patientSelfRegConfigurationService.findByString(key, pageable);

        if (stringsPage.hasContent()) {
            return new ResponseEntity<>(new StringsResourcePage(assembler.toResource(stringsPage, stringsResourceAssembler), stringsPage),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
