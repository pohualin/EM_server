package com.emmisolutions.emmimanager.persistence.impl;

import org.junit.Before;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.persistence.BaseUnitTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

public class ScheduledProgramNotesRespositoryImplUnitTest extends BaseUnitTest {

    private static final String UNORDERED_RESPONSE = "[{\"sequenceNumber\":1,\"notes\":\"Some \",\"viewId\":\"C\"},{\"sequenceNumber\":1,\"notes\":\"another note\\n\",\"viewId\":\"B\"},{\"sequenceNumber\":3,\"notes\":\"message \",\"viewId\":\"C\"},{\"sequenceNumber\":4,\"notes\":\"here.\\n\",\"viewId\":\"C\"},{\"sequenceNumber\":1,\"notes\":\"here are some new notes\\n\\n\",\"viewId\":\"A\"},{\"sequenceNumber\":2,\"notes\":\"random \",\"viewId\":\"C\"}]";

    private ScheduledProgramNotesRepositoryImpl scheduledProgramNotesRepository = new ScheduledProgramNotesRepositoryImpl();

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(scheduledProgramNotesRepository.getRestTemplate());
        scheduledProgramNotesRepository.setBaseUrl("http://localhost:3000/webapi/test/scheduled_programs/notes/");
    }

    /**
     * Test a 200 response with a blank message.
     */
    @Test
    public void testSuccessEmptyResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("The returned note is null when a blank response is returned.", testNote, is(nullValue()));
    }

    /**
     * Test a 500 response from the server
     */
    @Test
    public void testServerError() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("The returned note is null when there is a server error.", testNote, is(nullValue()));
    }

    /**
     * Test a 400 response from the server
     */
    @Test
    public void testBadRequest() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("The returned note is null when there is a bad request", testNote, is(nullValue()));
    }

    /**
     * Test a 204 response from the server
     */
    @Test
    public void testNoContent() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withNoContent());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("The returned note is null when no content is returned", testNote, is(nullValue()));
    }

    /**
     * Return a malformed string. Should throw an exception
     */
    @Test(expected = HttpMessageNotReadableException.class)
    public void testMalformedResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[}}{", MediaType.APPLICATION_JSON));

        scheduledProgramNotesRepository.find("test");
    }

    /**
     * Return a valid message containing two (2) single sequence notes and one (1) note containing five (5) out of order sequences.
     */
    @Test
    public void testUnorderedResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(UNORDERED_RESPONSE, MediaType.APPLICATION_JSON));

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("Note should contain a concatenated string of all the messages", testNote.getNote(), is("here are some new notes\n\nanother note\nSome random message here.\n"));

    }
}


