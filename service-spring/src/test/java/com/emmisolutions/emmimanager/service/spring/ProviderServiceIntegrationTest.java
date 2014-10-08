package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Gender;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ProviderService;

public class ProviderServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderService providerService;

	@Test(expected = ConstraintViolationException.class)
	public void createProviderWithoutRequired() {
		Provider provider = new Provider();
		providerService.create(provider);
	}

	@Test
	public void testProviderSave() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setGender(Gender.FEMALE);
		provider.setActive(true);
		provider = providerService.create(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
	}
}
