package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
        if (team == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return teamTagRepository.findByTeam(team, pageable);
    }

    @Override
    @Transactional
    public void deleteTeamTagsWithTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("team tag is null");
        }
        teamTagRepository.deleteByTeam(team);
        teamTagRepository.flush();
    }

    private void checkTeamTagNull(TeamTag teamTag) {
        if (teamTag == null) {
            throw new IllegalArgumentException("team tag is null");
        }
    }

}
