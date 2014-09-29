package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/**
 * TeamTag persistence class
 */
public interface TeamTagPersistence {
    /**
     * Saves a TeamTag
     * @param TeamTag 	to save
     * @return saved TeamTag
     */
    TeamTag saveTeamTag(TeamTag tag);

    /**
     * Deletes a TeamTag
     * @param TeamTag 	to delete
     */
    void deleteTeamTag(TeamTag tag);

    /**
     * Reloads a TeamTag by given ID
     * @param Long id	of the Tag
     * @return Tag
     */
    TeamTag reload(TeamTag tag);

    /**
     * Gets all the tags associated with a given team
     * @param TeamTag that contains team	to search for
     * @return Page of associated tags for given team
     */
    Page<TeamTag> getAllTeamTagsForTeam(Pageable pageable,Team team);

    /**
     * Gets all the teams associated with a given tag
     * @param Team to look for
     * @return List of matching TeamTags
     */
    List<TeamTag> getAllTagsForTeam(Team team);
}
