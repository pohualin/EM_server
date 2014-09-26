package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.persistence.TeamTagPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TeamTagPersistenceImpl implements TeamTagPersistence {
    @Resource
    TeamTagRepository teamTagRepository;

    @Override
    public TeamTag saveTeamTag(TeamTag tag) {
       return teamTagRepository.save(tag);
    }

    @Override
    public void deleteTeamTag(TeamTag tag) {
        teamTagRepository.delete(tag.getId());
    }

    @Override
    public TeamTag reload(TeamTag tag) {
        return teamTagRepository.getOne(tag.getId());
    }

    @Override
    public Page<TeamTag> getAllTeamTagsForTeam(Pageable pageable,Team team) {
       return teamTagRepository.findByTeam(team, pageable);
    }
}
