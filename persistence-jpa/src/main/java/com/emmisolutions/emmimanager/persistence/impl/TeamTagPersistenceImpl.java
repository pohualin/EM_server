package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/*
 * TeamTag persistence implementation
 */
@Repository
public class TeamTagPersistenceImpl implements TeamTagPersistence {
    @Resource
    TeamTagRepository teamTagRepository;

    @Override
    public TeamTag saveTeamTag(TeamTag teamTag) {
        if (checkForNull(teamTag)|| areClientsDifferentOnTagAndTeam(teamTag)){
            return null;
        }
        return teamTagRepository.save(teamTag);
    }

    private boolean areClientsDifferentOnTagAndTeam(TeamTag teamTag) {
        return !teamTag.getTag().getGroup().getClient().equals(teamTag.getTeam().getClient());
    }

    @Override
    public void deleteTeamTag(TeamTag teamTag) {
        if (checkForNull(teamTag)){
            return;
        }
        teamTagRepository.delete(teamTag.getId());
    }

    @Override
    public TeamTag reload(TeamTag teamTag) {
        if (checkForNull(teamTag)){
            return null;
        }
        return teamTagRepository.getOne(teamTag.getId());
    }

    @Override
    public Page<TeamTag> getAllTeamTagsForTeam(Pageable pageable,Team team) {
       if(team==null){
           // default pagination request if none
           pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
       }
       return teamTagRepository.findByTeam(team, pageable);
    }

    private boolean checkForNull(TeamTag teamTag) {
        if(teamTag==null || teamTag.getTeam()==null || teamTag.getTag()==null){
            return true;
        }
        return false;
    }
}
