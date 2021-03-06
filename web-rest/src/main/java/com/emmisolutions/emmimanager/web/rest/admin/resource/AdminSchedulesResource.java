package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.model.schedule.remote.ProgramNotesJson;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import com.emmisolutions.emmimanager.web.rest.admin.model.schedule.ScheduledProgramResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.schedule.ScheduledProgramResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter.with;
import static com.emmisolutions.emmimanager.web.rest.client.resource.TrackingEmailsResource.emailViewedTrackingLink;
import static com.emmisolutions.emmimanager.web.rest.client.resource.TrackingEmailsResource.patientRedirectLink;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * REST interface for admin facing scheduling
 */
@RestController("adminSchedulesResource")
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class AdminSchedulesResource {

    /**
     * request parameter name
     */
    public static final String ACCESS_CODES_REQUEST_PARAM = "accessCode";

    /**
     * request parameter name
     */
    public static final String PATIENT_REQUEST_PARAM = "patient";

    /**
     * request parameter name
     */
    public static final String CLIENT_REQUEST_PARAM = "client";

    /**
     * request parameter name
     */
    public static final String EXPIRED_REQUEST_PARAM = "expired";

    @Resource
    ScheduleService scheduleService;

    @Resource
    ScheduleProgramReminderEmailJobMaintenanceService scheduledProgramReminderEmailJob;

    @Resource(name = "adminScheduledProgramResourceAssembler")
    ResourceAssembler<ScheduledProgram, ScheduledProgramResource> scheduledProgramResourceResourceAssembler;

    /**
     * GET to retrieve a page of ScheduledProgram objects for a particular patient.
     *
     * @return OK (200): ScheduledProgramResourcePage when authorized
     * NO_CONTENT (204): when nothing is found
     * NOT_AUTHORIZED (403): if the user is not authorized.
     */
    @RequestMapping(value = "/scheduled_programs", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "createdDate,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = ACCESS_CODES_REQUEST_PARAM, defaultValue = "", value = "Access codes to filter by", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = PATIENT_REQUEST_PARAM, defaultValue = "", value = "Patient IDs to filter by", dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = EXPIRED_REQUEST_PARAM, defaultValue = "", value = "Show expired", dataType = "boolean", paramType = "query")
    })
    public ResponseEntity<ScheduledProgramResourcePage> find(
            @RequestParam(value = ACCESS_CODES_REQUEST_PARAM, required = false) List<String> accessCodes,
            @RequestParam(value = PATIENT_REQUEST_PARAM, required = false) Long patientId,
            @RequestParam(value = EXPIRED_REQUEST_PARAM, required = false) Boolean expired,
            @PageableDefault(size = 25, sort = "viewByDate", direction = Sort.Direction.ASC) Pageable page,
            PagedResourcesAssembler<ScheduledProgram> assembler) {

        ScheduledProgramSearchFilter filter = with()
                .accessCodes(accessCodes)
                .includeExpired(Boolean.TRUE.equals(expired));

        if (patientId != null){
            filter.patients(new Patient(patientId));
        }

        Page<ScheduledProgram> scheduledPrograms = scheduleService.find(filter, page);

        if (scheduledPrograms.hasContent()) {
            return new ResponseEntity<>(new ScheduledProgramResourcePage(
                    assembler.toResource(scheduledPrograms, scheduledProgramResourceResourceAssembler),
                    scheduledPrograms, filter), OK);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * GET to retrieve a specific schedule program by id
     *
     * @param id to load
     * @return OK (200): with the scheduled program resource
     * NO_CONTENT (204): if there isn't one found
     * NOT_AUTHORIZED (403): if the user is not authorized
     */
    @RequestMapping(value = "/scheduled_programs/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ScheduledProgramResource> get(@PathVariable("id") Long id) {
        ScheduledProgram scheduledProgram = scheduleService.reload(new ScheduledProgram(id));
        if (scheduledProgram != null) {
            return new ResponseEntity<>(scheduledProgramResourceResourceAssembler.toResource(scheduledProgram), OK);
        }
        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * PUT to update a specific schedule program by id
     *
     * @param id to update
     * @return OK (200): when the update is successful
     * INTERNAL_SERVER_ERROR (500): if there isn't one found
     * NOT_AUTHORIZED (403): if the user is not authorized
     */
    @RequestMapping(value = "/scheduled_programs/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ScheduledProgramResource> update(@PathVariable("id") Long id,
                                                           @RequestBody ScheduledProgram scheduledProgram) {
        scheduledProgram.setId(id);
        ScheduledProgram updatedProgram = scheduleService.update(scheduledProgram);
        scheduledProgramReminderEmailJob.updateScheduledReminders(updatedProgram, patientRedirectLink(),
                emailViewedTrackingLink());
        if (updatedProgram != null) {
            return new ResponseEntity<>(scheduledProgramResourceResourceAssembler.toResource(updatedProgram), OK);
        }
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }

    /**
     * GET to retrieve notes for a specific scheduled program
     *
     * @param id of scheduled program to retrieve notes for
     * @return OK (200): when notes are found
     * INTERNAL_SERVER_ERROR (500): if there isn't one found
     * NO_CONTENT (204): if there isn't one found
     */
    @RequestMapping(value = "/scheduled_programs/{id}/notes", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<ScheduledProgramNote> getNotes(@PathVariable("id") Long id) {
        ScheduledProgram scheduledProgram = new ScheduledProgram(id);
        ScheduledProgramNote note = scheduleService.findNotes(scheduledProgram);

        if (note != null) {
            return new ResponseEntity<>(note, OK);
        }

        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * Test endpoint set up to return randomly generated notes
     *
     * @param id of scheduled program (unused)
     * @return a list of ProgramNotesJson to simulate the ePlayerWebService response.
     */
    @RequestMapping(value = "/test/scheduled_programs/notes/{id}", method = RequestMethod.GET)
    public List<ProgramNotesJson> testFindNotes(@PathVariable("id") String id) {
        ProgramNotesJson testNotes = new ProgramNotesJson();
        List<ProgramNotesJson> ret = new ArrayList<>();
        ret.add(testNotes);

        testNotes.setSequenceNumber(1);
        testNotes.setViewId(id);
        testNotes.setNotes(generateRandomNotes());

        return ret;
    }

    private String generateRandomNotes() {
        Random rand = new Random();
        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < rand.nextInt(300) + 5; i++) {
            ret.append(RandomStringUtils.randomAlphabetic(rand.nextInt(10) + 1)).append(" ");
        }

        return ret.toString();
    }
}
