package com.emmisolutions.emmimanager.web.rest.endpoint;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.model.TeamResource;
import com.emmisolutions.emmimanager.web.rest.model.TeamsResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * Teams Endpoint
 */
@Path("")
@Produces({
        APPLICATION_JSON,
        APPLICATION_XML})
@Consumes({
        APPLICATION_JSON,
        APPLICATION_XML})
@Component
@Scope("prototype")
public class TeamsEndpoint {

    @Resource
    TeamService teamService;

    String basePath;

    @GET
    @Path("teams/{id}")
    public Response get(@PathParam("id") Long id) {
        Team team = new Team();
        team.setId(id);
        return Response.ok(new TeamResource(
                teamService.reload(team),
                basePath))
                .build();
    }

    @GET
    @Path("clients/{id}/teams")
    public Response listByClient(@PathParam("id") Client client,
                                 @QueryParam("page") @DefaultValue("0") Integer page,
                                 @QueryParam("max") @DefaultValue("10") Integer max) {
        List<Team> teams = teamService.listWhereClientEquals(client, page, max);
        if (CollectionUtils.isEmpty(teams)) {
            return Response.noContent().build();
        } else {
            return Response.ok(new TeamsResource(client,
                    teams, page, max, teamService.lastPage(client, max), 100, basePath)).build();
        }
    }

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        basePath = uriInfo.getBaseUri().getPath();
    }

}
