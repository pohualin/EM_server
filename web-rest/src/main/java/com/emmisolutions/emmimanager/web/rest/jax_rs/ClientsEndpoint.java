package com.emmisolutions.emmimanager.web.rest.jax_rs;

import com.emmisolutions.emmimanager.api.ClientApi1;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.jax_rs.util.EndpointHelper;
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
    ClientApi1 clientApi;

    @Resource
    EndpointHelper endpointHelper;

    String basePath;

    @GET
    @Path("clients/{id}")
    public Response get(@PathParam("id") Client client) {
        client = clientApi.get(client);
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
        Page<Client> clients = clientApi.list(
                endpointHelper.createPageable(page, max, sort),
                nameFilter,
                statusFilter);
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
