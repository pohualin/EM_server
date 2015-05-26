package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProviderSpecialtyRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ProviderPersistenceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderPersistence providerPersistence;

	@Resource
	ProviderRepository providerRepository;

	@Resource
	ReferenceGroupRepository referenceGroupRepository;

	@Resource
    UserAdminPersistence userAdminPersistence;

	@Resource
	TeamPersistence teamPersistence;

    @Resource
    ProviderSpecialtyRepository providerSpecialtyRepository;
    
    @Resource
    ClientProviderRepository clientProviderRepository;

	private ProviderSpecialty getSpecialty(){
        ProviderSpecialty specialty = new ProviderSpecialty();
        specialty.setName(RandomStringUtils.randomAlphanumeric(9));
        ProviderSpecialty savedSpecialty = providerPersistence.saveSpecialty(specialty);
        ProviderSpecialty reloadedSpecialty = providerSpecialtyRepository.findOne(savedSpecialty.getId());
		return savedSpecialty;
	}

	/**
	 * Testing a provider save
	 */
	@Test
	public void testProviderSave() {
		Provider provider = new Provider();
		provider.setFirstName("A-Za-z- '(),.");
		provider.setMiddleName("A-Za-z- '(),.");
		provider.setLastName("A-Za-z- '(),.");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());
		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(), is("system"));

        assertThat("can reload it", providerPersistence.reload(provider), is(provider));
	}

    /**
     * Find all specialties
     */
    @Test
    public void findSpecialties(){
        assertThat("Reference specialties come back", providerPersistence.findAllProviderSpecialties(null).getTotalElements(),
            is(not(0l)));
    }

	/**
	 * Testing a provider save without the required fielf "last name"
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithoutLastName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(false);
		provider.setSpecialty(getSpecialty());
		providerPersistence.save(provider);
	}

	/**
	 * Testing a provider save with invalid character inside name fields, should fail
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testProviderSaveWithInvalidCharactersForName() {
		Provider provider = new Provider();
		provider.setFirstName("Mary%");
		provider.setLastName("Poppins<3");
		provider.setMiddleName("middle name");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(false);
		provider.setSpecialty(getSpecialty());
		providerPersistence.save(provider);
	}

 	/**
	 * Testing a provider save without a team associated with it
	 */
	@Test
	public void testProviderSaveWithoutATeam() {
		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);
		provider.setSpecialty(getSpecialty());

		provider = providerPersistence.save(provider);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
	}

 	/**
	 * Test to list providers by ProviderSearchFilter
	 */
 	@Test
 	public void searchForProvider(){
 		Provider provider = new Provider();
		provider.setFirstName("Velma");
		provider.setLastName("Kelly");
		provider.setEmail("velmakelly@fourtysecondstreet.com");
		provider.setActive(false);
		provider.setSpecialty(getSpecialty());
		provider = providerPersistence.save(provider);

		assertThat("Provider one was saved", provider.getId(), is(notNullValue()));

		Provider providerTwo = new Provider();
		providerTwo.setFirstName("Roxie");
		providerTwo.setLastName("Hart");
		providerTwo.setEmail("roxyhart@fourtysecondstreet.com");
		providerTwo.setActive(false);
		providerTwo.setSpecialty(getSpecialty());
		providerTwo = providerPersistence.save(providerTwo);

		assertThat("Provider two was saved", providerTwo.getId(), is(notNullValue()));

		Pageable page = new PageRequest(0, 50, Sort.Direction.ASC, "id");

		ProviderSearchFilter searchFilter = new ProviderSearchFilter("velma");
		Page<Provider> providerPage = providerPersistence.list(page, searchFilter);
		assertThat("Provider was found", providerPage.getContent().size(), is(1));
		assertThat("Provider was found", providerPage.getContent().iterator().next().getFirstName(), is("Velma"));

		ProviderSearchFilter searchFilterTwo = new ProviderSearchFilter(StatusFilter.ACTIVE_ONLY, "kel");
		Page<Provider> providerPageTwo = providerPersistence.list(page, searchFilterTwo);
		assertThat("Provider was found", providerPageTwo.getContent().size(), is(0));

		ProviderSearchFilter searchFilterThree = new ProviderSearchFilter(StatusFilter.INACTIVE_ONLY, "kel");
		Page<Provider> providerPageThree = providerPersistence.list(page, searchFilterThree);
		assertThat("Provider was found", providerPageThree.getContent().size(), is(1));
		assertThat("Provider was found", providerPageThree.getContent().iterator().next().getLastName(), is("Kelly"));

		ProviderSearchFilter searchFilterFour = new ProviderSearchFilter(StatusFilter.ALL, "VElma Kelly");
		Page<Provider> providerPageFour = providerPersistence.list(page, searchFilterFour);
		assertThat("Provider was found", providerPageFour.getContent().size(), is(1));
		assertThat("Provider was found", providerPageFour.getContent().iterator().next().getLastName(), is("Kelly"));


		ProviderSearchFilter searchFilterFive = new ProviderSearchFilter(StatusFilter.ALL, "roXi$e");
		Page<Provider> providerPageFive = providerPersistence.list(page, searchFilterFive);
		assertThat("Provider was found", providerPageFive.getContent().size(), is(1));
		assertThat("Provider was found", providerPageFive.getContent().iterator().next().getLastName(), is("Hart"));
 	}
 	
 	@Test
 	public void possibleProviderWithoutClientProvider(){
 	    Client client = makeNewRandomClient();
 	    Provider provider = makeNewRandomProvider();
 	    ClientProvider clientProvider = new ClientProvider(client, provider);
 	    clientProvider = clientProviderRepository.save(clientProvider);
 	    
 	    Client clientB = makeNewRandomClient();
 	    ProviderSearchFilter filter = new ProviderSearchFilter(provider.getLastName());
 	    filter.setNotUsingThisClient(clientB);
 	    Page<Provider> possibles = providerPersistence.list(null, filter);
 	    assertThat("Possible contains provider.", possibles, hasItem(provider));
 	}
}
