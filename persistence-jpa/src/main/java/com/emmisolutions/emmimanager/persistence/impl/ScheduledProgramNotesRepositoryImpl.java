package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.model.schedule.remote.ProgramNotesJson;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramNotesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Implementation of the ScheduledProgramNotesRepository to query the Emmi1.5 API
 */
@Repository
public class ScheduledProgramNotesRepositoryImpl implements ScheduledProgramNotesRepository {

    RestTemplate restTemplate;
    @Value("${scheduledProgramNotes.service.url:http://127.0.0.1:8080/webapi/test/scheduled_programs/notes}")
    private String baseUrl;

    /**
     * Given a unique identifier, query the API for a given program's notes
     *
     * TODO: How to handle non-2xx responses? Currently it is being silently ignored and treated as if a note doesn't exist
     *
     * @param accessCode unique identifier
     * @return a ScheduledProgramNote containing the concatenated notes from all viewings
     */
    @Override
    public ScheduledProgramNote find(String accessCode) {
        ScheduledProgramNote retval = null;
        List<ProgramNotesJson> responseResults;
        Map<String, List<ProgramNotesJson>> orderedResults;

        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment(accessCode);

        getRestTemplate().setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });

        ResponseEntity<List<ProgramNotesJson>> responseEntity = getRestTemplate().exchange(uri.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProgramNotesJson>>() {});

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null && !responseEntity.getBody().isEmpty()) {
            retval = new ScheduledProgramNote();
            orderedResults = new HashMap<>();

            for (ProgramNotesJson programNote : responseEntity.getBody()) {
                if (orderedResults.containsKey(programNote.getViewId())) {
                    List<ProgramNotesJson> programNotesJsonList = orderedResults.get(programNote.getViewId());

                    int index;
                    for (index = 0; index < programNotesJsonList.size(); index++) {
                        if (programNote.getSequenceNumber() < programNotesJsonList.get(index).getSequenceNumber()) {
                            break;
                        }
                    }
                    programNotesJsonList.add(index, programNote);
                } else {
                    List<ProgramNotesJson> programNotesJsonList = new LinkedList<>();
                    programNotesJsonList.add(programNote);
                    orderedResults.put(programNote.getViewId(), programNotesJsonList);
                }
            }

            responseResults = new ArrayList<>();
            StringBuilder allNotes = new StringBuilder();
            for (List<ProgramNotesJson> programNotesList : orderedResults.values()) {
                if (programNotesList.size() > 1) {
                    ProgramNotesJson item = new ProgramNotesJson();
                    StringBuilder stringBuilder = new StringBuilder();

                    item.setSequenceNumber(programNotesList.get(0).getSequenceNumber());
                    item.setViewId(programNotesList.get(0).getViewId());

                    for (ProgramNotesJson note : programNotesList) {
                        stringBuilder.append(note.getNotes());
                    }

                    item.setNotes(stringBuilder.toString());

                    responseResults.add(item);

                    allNotes.append(item.getNotes());
                } else {
                    ProgramNotesJson note = programNotesList.get(0);
                    responseResults.add(note);

                    allNotes.append(note.getNotes());
                }
            }

            retval.setNote(allNotes.toString());
        }

        return retval;
    }

    RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            setRestTemplate(new RestTemplate());
        }

        return restTemplate;
    }

    void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    String getBaseUrl() {
        return baseUrl;
    }

    void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
