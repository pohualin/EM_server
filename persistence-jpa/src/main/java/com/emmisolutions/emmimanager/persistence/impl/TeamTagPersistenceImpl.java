package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.TeamTagSearchFilter;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.TeamTagSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.TeamTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * TeamTag persistence implementation
 */
@Repository
public class TeamTagPersistenceImpl implements TeamTagPersistence {
    @Resource
    TeamTagRepository teamTagRepository;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    TagPersistence tagPersistence;

    @Resource
    TeamTagSpecifications teamTagSpecifications;

    @Override
    public TeamTag saveTeamTag(TeamTag teamTag) {
        checkTeamTagNull(teamTag);
        Team team = teamPersistence.reload(teamTag.getTeam());
        Tag tag = tagPersistence.reload(teamTag.getTag());
        if (team == null || tag == null) {
            throw new IllegalArgumentException("Team or Tag is not in the database");
        }
        if (!tag.getGroup().getClient().equals(team.getClient())) {
            throw new IllegalArgumentException("Tag and Team must share the same client");
        }
        return teamTagRepository.save(teamTag);
    }

    @Override
    public void deleteTeamTag(TeamTag teamTag) {
        checkTeamTagNull(teamTag);
        teamTagRepository.delete(teamTag);
    }

    @Override
    public TeamTag reload(TeamTag teamTag) {
        checkTeamTagNull(teamTag);
        return teamTagRepository.findOne(teamTag.getId());
    }

    @Override
    public Page<TeamTag> getAllTeamTagsForTeam(Pageable pageable, Team team) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return teamTagRepository.findByTeam(team, pageable);
    }

    @Override
    public void deleteTeamTagsWithTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("team tag is null");
        }
        teamTagRepository.deleteByTeam(team);
        teamTagRepository.flush();
    }

    @Override
    public long removeTeamTagsThatAreNotAssociatedWith(Long clientId, Set<Long> groupIdsToKeep) {
        if (clientId != null) {
            if (groupIdsToKeep == null || groupIdsToKeep.isEmpty()) {
                return teamTagRepository.deleteByTeamClientIdEquals(clientId);
            } else {
                return teamTagRepository.deleteByTeamClientIdEqualsAndTagGroupIdNotIn(clientId, groupIdsToKeep);
            }
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamTag> findTeamsWithTag(Pageable page, TeamTagSearchFilter teamTagSearchFilter) {
        return teamTagRepository.findAll(where(
                        teamTagSpecifications.tagsFollowRequirementsLogic(teamTagSearchFilter))
                        .and(teamTagSpecifications.isInStatus(teamTagSearchFilter)),
                page == null ? new PageRequest(0, 50, Sort.Direction.ASC, "id") : page);
    }

    private void checkTeamTagNull(TeamTag teamTag) {
        if (teamTag == null) {
            throw new IllegalArgumentException("team tag is null");
        }
    }

}
