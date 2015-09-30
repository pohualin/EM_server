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

    @Test
    public void testSuccessEmptyResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("", testNote, is(nullValue()));
    }

    /**
     * 500
     */
    @Test
    public void testServerError() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("", testNote, is(nullValue()));
    }

    /**
     * 400
     */
    @Test
    public void testBadRequest() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("", testNote, is(nullValue()));
    }

    /**
     * 204
     */
    @Test
    public void testNoContent() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withNoContent());

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("", testNote, is(nullValue()));
    }

    @Test(expected = HttpMessageNotReadableException.class)
    public void testMalformedResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[}}{", MediaType.APPLICATION_JSON));

        scheduledProgramNotesRepository.find("test");
    }

    @Test
    public void testUnorderedResponse() {
        mockServer.expect(requestTo("http://localhost:3000/webapi/test/scheduled_programs/notes/test")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(UNORDERED_RESPONSE, MediaType.APPLICATION_JSON));

        ScheduledProgramNote testNote = scheduledProgramNotesRepository.find("test");

        mockServer.verify();
        assertThat("", testNote.getNote(), is("here are some new notes\n\nanother note\nSome random message here.\n"));

    }
}


