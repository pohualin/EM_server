package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientNotePersistence;

/**
 * Test ClientNotePersistence
 */
public class ClientNotePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ClientNotePersistence clientNotePersistence;

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testReload() {
        assertThat("bad reload", clientNotePersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test save, reload and delete
     */
    @Test
    public void testSave() {
        Client client = makeNewRandomClient();
        ClientNote note = new ClientNote();
        note.setClient(client);
        note.setNote(RandomStringUtils.randomAlphanumeric(4096));
        note = clientNotePersistence.saveOrUpdate(note);
        assertThat("save note successfully", note.getId(), is(notNullValue()));

        ClientNote reload = clientNotePersistence.reload(note.getId());
        assertThat("reload the same instance", reload, is(note));

        clientNotePersistence.delete(reload.getId());
        assertThat("delete note successfully",
                clientNotePersistence.reload(reload.getId()), is(nullValue()));
    }

    /**
     * Test findByClient
     */
    @Test
    public void testFindByClient() {
        Client client = makeNewRandomClient();
        ClientNote note = new ClientNote();
        note.setClient(client);
        note.setNote(RandomStringUtils.randomAlphanumeric(4096));
        note = clientNotePersistence.saveOrUpdate(note);

        ClientNote findByClient = clientNotePersistence.findByClientId(client
                .getId());
        assertThat("should find one ClientNote by client", findByClient,
                is(note));
        assertThat("should find one ClientNote with the passed in client",
                findByClient.getClient(), is(client));
    }

}
