package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.TeamTagSearchFilter;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.service.TeamTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of TeamTagService
 */
@Service
public class TeamTagServiceImpl implements TeamTagService {

    @Resource
    TeamTagPersistence teamTagPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    TagPersistence tagPersistence;

    @Override
    public Page<TeamTag> findAllTeamTagsWithTeam(Pageable pageable, Team team) {
        return teamTagPersistence.getAllTeamTagsForTeam(pageable, team);
    }

    @Override
    @Transactional
    public List<TeamTag> save(Team team, Set<Tag> tagSet) {
        Team teamToFind = teamPersistence.reload(team);
        if (teamToFind != null) {
            teamTagPersistence.deleteTeamTagsWithTeam(teamToFind);
            if (tagSet != null) {
                List<TeamTag> savedTeamTags = new ArrayList<>();
                for (Tag tag : tagSet) {
                    TeamTag teamTag = new TeamTag(teamToFind, tag);
                    TeamTag savedTeamTag = teamTagPersistence.saveTeamTag(teamTag);
                    savedTeamTags.add(savedTeamTag);
                }
                return savedTeamTags;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public TeamTag saveSingleTeamTag(Team team, Tag tag) {
        Team teamToFind = teamPersistence.reload(team);
        Tag tagToFind = tagPersistence.reload(tag);
        if (teamToFind != null && tagToFind != null) {
            TeamTag teamTag = new TeamTag(teamToFind, tag);
            return teamTagPersistence.saveTeamTag(teamTag);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteSingleTeamTag(TeamTag teamTag) {
        TeamTag newTeamTag = teamTagPersistence.reload(teamTag);
        if (newTeamTag != null) {
            teamTagPersistence.deleteTeamTag(newTeamTag);
        }
    }


    @Override
    public TeamTag reload(TeamTag teamTag) {
        return teamTagPersistence.reload(teamTag);
    }

    @Override
    public Page<TeamTag> findTeamsWithTag(Pageable pageable, TeamTagSearchFilter teamTagSearchFilter) {
        return teamTagPersistence.findTeamsWithTag(pageable, teamTagSearchFilter);
    }
}
