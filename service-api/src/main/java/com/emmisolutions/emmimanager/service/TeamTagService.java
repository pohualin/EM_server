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
     * Returns a Page of TeamTags that have the given team
     */
    Page<TeamTag> findAllTeamTagsWithTeam(Pageable pageable, Team team);

    /**
     * saves an association of a team with a set of tags
     */
    void save(Team team, Set<Tag> tagSet);
}
