package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * TeamTag Service API
 */
@Service
public interface TeamTagService {
    /**
     * @param pageable pagable object
     * @param team team to find
     * @return Page of TeamTags that have the given team
     */
    Page<TeamTag> findAllTeamTagsWithTeam(Pageable pageable, Team team);

    /**
     * saves an association of a team with a set of tags
     * @param team to save
     * @param tagSet to save
     */
    void save(Team team, Set<Tag> tagSet);

    /**
     *
     * @param teamTag to reload
     * @return teamTag that was reloaded
     */
    TeamTag reload(TeamTag teamTag);
}
