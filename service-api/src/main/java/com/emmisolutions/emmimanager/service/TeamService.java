package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;

import java.util.List;

/**
 * Created by matt on 7/8/14.
 */
public interface TeamService {
    Team reload(Team team);

    List<Team> listWhereClientEquals(Client client, Integer page, Integer numberPerPage);

    Integer lastPage(Client client, Integer numberPerPage);
}
