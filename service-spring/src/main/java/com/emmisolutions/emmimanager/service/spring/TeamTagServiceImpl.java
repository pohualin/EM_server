package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.persistence.TagPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.service.TeamTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class TeamTagServiceImpl implements TeamTagService {

    @Resource
    TeamTagPersistence teamTagPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Override
    public Page<TeamTag> findAllTeamTagsWithTeam(Pageable pageable, Team team) {
        return teamTagPersistence.getAllTeamTagsForTeam(pageable, team);
    }

    @Override
    public void save(Team team, Set<Tag> tagSet) {
        if(tagSet==null||tagSet.isEmpty()){
            return;
        }
        Team teamToFind = teamPersistence.reload(team);
        teamTagPersistence.deleteTeamTagsWithTeam(teamToFind);

        for(Tag tag: tagSet){
            TeamTag teamTag = new TeamTag(team, tag);
            teamTagPersistence.saveTeamTag(teamTag);
        }
    }
}
