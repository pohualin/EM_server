package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientNoteService;

/**
 * Test Service Implementation for ClientNote
 */
public class ClientNoteServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientNoteService clientNoteService;

    /**
     * Test get, save, reload, delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        ClientNote note = new ClientNote();
        note.setClient(new Client(client.getId()));
        note.setNote(RandomStringUtils.randomAlphanumeric(800));
        note = clientNoteService.create(note);
        assertThat("should create a ClientNote", note.getId(),
                is(notNullValue()));

        // Version and Id are required for update
        ClientNote updateWithIdAndVersion = new ClientNote();
        updateWithIdAndVersion.setClient(client);
        updateWithIdAndVersion.setId(note.getId());
        updateWithIdAndVersion.setVersion(note.getVersion());
        updateWithIdAndVersion.setNote(RandomStringUtils
                .randomAlphanumeric(800));
        updateWithIdAndVersion = clientNoteService
                .update(updateWithIdAndVersion);
        assertThat("should update an existing ClientNote",
                updateWithIdAndVersion.getId().equals(note.getId()), is(true));
        assertThat("should have different version", updateWithIdAndVersion
                .getVersion().equals(note.getVersion()), is(false));

        ClientNote reload = clientNoteService.reload(new ClientNote(note
                .getId()));
        assertThat("reload the same instance", reload, is(note));

        clientNoteService.delete(reload);
        ClientNote reloadAfterDelete = clientNoteService.reload(new ClientNote(
                note.getId()));
        assertThat("delete ClientNote successfully", reloadAfterDelete,
                is(nullValue()));

        assertThat("reload will return null with null object",
                clientNoteService.reload(null), is(nullValue()));
        assertThat("reload will return null with null id",
                clientNoteService.reload(new ClientNote()), is(nullValue()));
    }

    /**
     * Test get ClientNote by Client
     */
    @Test
    public void testGet() {
        Client client = makeNewRandomClient();
        ClientNote note = new ClientNote();
        note.setClient(client);
        note.setNote(RandomStringUtils.randomAlphanumeric(800));
        note = clientNoteService.create(note);

        assertThat("should find null with another client without note",
                clientNoteService.findByClient(makeNewRandomClient()),
                is(nullValue()));

        assertThat("should find null with client without id",
                clientNoteService.findByClient(new Client()), is(nullValue()));

        assertThat("should find null with null client",
                clientNoteService.findByClient(null), is(nullValue()));

        assertThat("should find the same instance",
                clientNoteService.findByClient(new Client(client.getId())),
                is(note));

    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNull() {
        clientNoteService.delete(null);
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeDeleteWithNullId() {
        clientNoteService.delete(new ClientNote());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateWithNullVersion() {
        Client client = makeNewRandomClient();
        ClientNote note = new ClientNote();
        note.setClient(client);
        note.setNote(RandomStringUtils.randomAlphanumeric(800));
        note = clientNoteService.create(note);
        clientNoteService.update(new ClientNote(note.getId()));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateWithNullId() {
        clientNoteService.update(new ClientNote());
    }

}
