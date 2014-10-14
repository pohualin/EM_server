package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceGroupTypeRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserService;

public class ProviderServiceIntegrationTest extends BaseIntegrationTest {

	@Resource
	ProviderService providerService;
	
	@Resource
	ClientService clientService;

	@Resource
	TeamService teamService;

	@Resource
	UserService userService;
	
	@Resource
	ReferenceTagRepository referenceTagRepository;

	@Resource
	ReferenceGroupRepository referenceGroupRepository;
	
	@Resource
    ReferenceGroupTypeRepository referenceGroupTypeRepository;

	/**
	 * Testing a provider save without the required fields
	 */
	@Test(expected = ConstraintViolationException.class)
	public void createProviderWithoutRequired() {
		Provider provider = new Provider();
		Team team = new Team();
		providerService.create(provider, team);
	}

	/**
	 * Testing a provider save
	 */
	@Test
	public void testProviderSave() {

		Client client = makeClient("TeamProvi", "teamProUser");
		clientService.create(client);

		Provider provider = new Provider();
		provider.setFirstName("Mary");
		provider.setMiddleName("Broadway");
		provider.setLastName("Poppins");
		provider.setEmail("marypoppins@fourtysecondstreet.com");
		provider.setActive(true);

		Team team = new Team();
		team.setName("Test Team Provider");
		team.setDescription("Test Team description");
		team.setActive(false);
		team.setClient(client);
		team.setSalesForceAccount(new TeamSalesForce("xxxWW"
				+ System.currentTimeMillis()));
        Team savedTeam = teamService.create(team);

        provider.setSpecialty(getSpecialty());
		provider = providerService.create(provider, savedTeam);
		assertThat("Provider was saved", provider.getId(), is(notNullValue()));
		assertThat("system is the created by", provider.getCreatedBy(),
				is("system"));
	}

	protected Client makeClient(String clientName, String username) {
		Client client = new Client();
		client.setType(new ClientType(4l));
		client.setContractStart(LocalDate.now());
		client.setContractEnd(LocalDate.now().plusYears(1));
		client.setName(clientName);
		client.setContractOwner(userService.save(new User(username, "pw")));
		client.setSalesForceAccount(new SalesForce("xxxWW"
				+ System.currentTimeMillis()));
		return client;
	}
	
	private ReferenceTag getSpecialty(){
		ReferenceTag specialty = new ReferenceTag();
		ReferenceGroup group = new ReferenceGroup();
		ReferenceGroupType type = new ReferenceGroupType();
		type.setName("refGroupType");
		type= referenceGroupTypeRepository.save(type);
		group.setName("ref group");
		group.setType(type);
		group = referenceGroupRepository.save(group);
		specialty.setName("ENT");
		specialty.setGroup(group);
		specialty = referenceTagRepository.save(specialty);
		return specialty;
	}
}
