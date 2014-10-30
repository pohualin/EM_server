package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * TeamTag Service API
 */
@Service
public interface TeamTagService {
    /**
     * Find all TeamTags for a team
     *
     * @param pageable pagable object
     * @param team     team to find
     * @return Page of TeamTags that have the given team
     */
    Page<TeamTag> findAllTeamTagsWithTeam(Pageable pageable, Team team);

    /**
     * saves an association of a team with a set of tags
     *
     * @param team   to save
     * @param tagSet to save
     * @return list of saved TeamTag
     */
    List<TeamTag> save(Team team, Set<Tag> tagSet);

    /**
     * saves an association of a team with single tag
     *
     * @param team to associate tag with
     * @param tag  to save
     */
    TeamTag saveSingleTeamTag(Team team, Tag tag);

    /**
     * deletes a single TeamTag
     *
     * @param teamTag to delete
     */
    void deleteSingleTeamTag(TeamTag teamTag);

    /**
     * Reloads a TeamTag from the db
     *
     * @param teamTag to reload
     * @return the TeamTag that was reloaded
     * @throws java.lang.IllegalArgumentException if teamTag is not found
     */
    TeamTag reload(TeamTag teamTag);
}
