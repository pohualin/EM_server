package com.emmisolutions.emmimanager.service.spring;

import static com.emmisolutions.emmimanager.model.TeamSearchFilter.StatusFilter.fromStringOrActive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.model.UserClientUserClientTeamRoleSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientTeamRoleService;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;

/**
 * 
 * 
 */
@Service
public class UserClientUserClientTeamRoleServiceImpl implements
		UserClientUserClientTeamRoleService {

	@Resource
	TeamService teamService;

	@Resource
	UserClientService userClientService;

	@Resource
	UserClientTeamRoleService userClientTeamRoleService;

	@Resource
	UserClientUserClientTeamRolePersistence userClientUserClientTeamRolePersistence;

	@Override
	@Transactional
	public Set<UserClientUserClientTeamRole> associate(
			List<UserClientUserClientTeamRole> userClientUserClientTeamRoles) {
		Set<UserClientUserClientTeamRole> added = new HashSet<UserClientUserClientTeamRole>();
		for (UserClientUserClientTeamRole ucucr : userClientUserClientTeamRoles) {
			added.add(userClientUserClientTeamRolePersistence
					.saveOrUpdate(ucucr));
		}
		return added;
	}

	@Override
	@Transactional
	public void delete(UserClientUserClientTeamRole userClientUserClientTeamRole) {
		if (userClientUserClientTeamRole == null
				|| userClientUserClientTeamRole.getId() == null) {
			throw new InvalidDataAccessApiUsageException(
					"userClientUserClientId can not be null.");
		}
		userClientUserClientTeamRolePersistence
				.delete(userClientUserClientTeamRole.getId());
	}

	@Override
	@Transactional
	public void delete(UserClient userClient,
			UserClientTeamRole userClientTeamRole) {
		if (userClient == null || userClient.getId() == null
				|| userClientTeamRole == null
				|| userClientTeamRole.getId() == null) {
			throw new InvalidDataAccessApiUsageException(
					"userClientId and userClientTeamRoleId can not be null.");
		}
		userClientUserClientTeamRolePersistence.delete(userClient.getId(),
				userClientTeamRole.getId());
	}

	@Override
	@Transactional
	public Page<UserClientUserClientTeamRole> findByUserClientAndUserClientTeamRole(
			UserClient userClient, UserClientTeamRole userClientTeamRole,
			Pageable pageable) {
		if (userClient == null || userClient.getId() == null
				|| userClientTeamRole == null
				|| userClientTeamRole.getId() == null) {
			throw new InvalidDataAccessApiUsageException(
					"UserClient and UserClientTeamRole cannot be null");
		}
		return userClientUserClientTeamRolePersistence
				.findByUserClientIdAndUserClientTeamRoleId(userClient.getId(),
						userClientTeamRole.getId(), pageable);
	}

	@Override
	@Transactional
	public List<UserClientUserClientTeamRole> findExistingByUserClientInTeams(
			UserClient userClient, List<Team> teams) {
		if (userClient == null || userClient.getId() == null || teams == null
				|| teams.isEmpty()) {
			return new ArrayList<UserClientUserClientTeamRole>();
		}
		return userClientUserClientTeamRolePersistence
				.findByUserClientIdAndTeamsIn(userClient.getId(), teams);
	}

	@Override
	@Transactional
	public Page<UserClientUserClientTeamRole> findPossible(
			UserClientUserClientTeamRoleSearchFilter filter, Pageable pageable) {

		// Reload userClient by userClientId
		UserClient userClient = userClientService
				.reload(filter.getUserClient());

		// First fetch all Team by Client and Term
		String status = filter.getStatus() != null ? filter.getStatus().name()
				: null;
		TeamSearchFilter teamSearchFilter = new TeamSearchFilter(userClient
				.getClient().getId(), fromStringOrActive(status),
				filter.getTerm());
		Page<Team> teamPage = teamService.list(pageable, teamSearchFilter);

		Map<Team, UserClientUserClientTeamRole> existingMap = new HashMap<Team, UserClientUserClientTeamRole>();
		List<UserClientUserClientTeamRole> ucuctrs = new ArrayList<UserClientUserClientTeamRole>();

		/**
		 * Put all existing UCUCTR in existingMap. Also put them in ucuctrs,
		 * later to be used in paged contents
		 */
		for (UserClientUserClientTeamRole ucuctr : findExistingByUserClientInTeams(
				filter.getUserClient(), teamPage.getContent())) {
			existingMap.put(ucuctr.getTeam(), ucuctr);
			ucuctrs.add(ucuctr);
		}

		/**
		 * All other teams that are not currently associated with this
		 * UserClientUserClientTeamRole will be put in a new
		 * UserClientUserClientTeamRole with UserClient
		 */
		for (Team team : teamPage.getContent()) {
			if (!existingMap.containsKey(team)) {
				UserClientUserClientTeamRole ucuctr = new UserClientUserClientTeamRole();
				ucuctr.setUserClient(userClient);
				ucuctr.setTeam(team);
				ucuctrs.add(ucuctr);
			}
		}

		return new PageImpl<>(ucuctrs, pageable, teamPage.getTotalElements());
	}

	@Override
	@Transactional
	public UserClientUserClientTeamRole reload(
			UserClientUserClientTeamRole userClientUserClientTeamRole) {
		if (userClientUserClientTeamRole == null
				|| userClientUserClientTeamRole.getId() == null) {
			throw new InvalidDataAccessApiUsageException(
					"userClientUserClientId can not be null.");
		}
		return userClientUserClientTeamRolePersistence
				.reload(userClientUserClientTeamRole.getId());
	}

}
