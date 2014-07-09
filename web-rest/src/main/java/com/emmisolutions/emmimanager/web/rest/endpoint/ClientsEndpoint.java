package com.emmisolutions.emmimanager.web.rest.endpoint;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.web.rest.endpoint.util.EndpointHelper;
import com.emmisolutions.emmimanager.web.rest.model.ClientResource;
import com.emmisolutions.emmimanager.web.rest.model.ClientsResource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

/**
 * Clients Endpoint
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
public class ClientsEndpoint {

    @Resource
    ClientService clientService;

    @Resource
    EndpointHelper endpointHelper;

    String basePath;

    @GET
    @Path("clients/{id}")
    public Response get(@PathParam("id") Client client) {
        client = clientService.reload(client);
        if (client == null) {
            return Response.noContent().build();
        } else {
            return Response
                    .ok(new ClientResource(client, basePath))
                    .build();
        }
    }

    @GET
    @Path("clients")
    public Response list(@QueryParam("page") @DefaultValue("0") String page,
                         @QueryParam("max") @DefaultValue("10") String max,
                         @QueryParam("sort") @DefaultValue("id:asc;") String sort,
                         @QueryParam("name") @DefaultValue("") String nameFilter,
                         @QueryParam("status") @DefaultValue("") String statusFilter) {
        Page<Client> clients = clientService.list(endpointHelper.createPageable(page, max, sort));
        if (clients.hasContent()) {
            ClientsResource cr = new ClientsResource(clients, basePath);
            return Response.ok(cr).build();
        } else {
            return Response.noContent().build();
        }
    }

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        basePath = uriInfo.getBaseUri().getPath();
    }

}
