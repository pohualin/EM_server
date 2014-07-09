package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Team Service Implementation.
 */
@Service
public class TeamServiceImpl implements TeamService {

    @Override
    public Team reload(Team team) {
        return null;
    }

    @Override
    public List<Team> listWhereClientEquals(Client client, Integer page, Integer numberPerPage) {
        return null;
    }

    @Override
    public Integer lastPage(Client client, Integer numberPerPage) {
        return 10;
    }
}
