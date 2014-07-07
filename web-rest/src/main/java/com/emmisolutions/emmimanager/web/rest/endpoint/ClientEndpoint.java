package com.emmisolutions.emmimanager.web.rest.endpoint;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Client Endpoint
 */
@Path("/clients")
@Component
@Scope("prototype")
public class ClientEndpoint {

    @GET
    @Produces(APPLICATION_JSON)
    public String list() {
        return "[" +
                "        {" +
                "            \"id\": 1," +
                "            \"active\": 1," +
                "            \"name\": \"Demo hospital client 1\"," +
                "            \"type\": \"Hospital\"," +
                "            \"region\": \"Midwest\"," +
                "            \"tier\": \"Level 1\"," +
                "            \"owner\": \"Evan\"," +
                "            \"start\": \"2014-5-16\"," +
                "            \"end\": \"2015-5-16\"," +
                "            \"salesforce\": {" +
                "                \"id\": 10," +
                "                \"name\": \"Salesforce Account name\"" +
                "            }," +
                "            \"teams\": [" +
                "                { \"name\": \"Team 1\" }," +
                "                { \"name\": \"Team 2\" }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"id\": 2," +
                "            \"active\": 1," +
                "            \"name\": \"Demo provider client 2\"," +
                "            \"type\": \"Provider\"," +
                "            \"region\": \"Midwest\"," +
                "            \"tier\": \"Level 2\"," +
                "            \"owner\": \"Kevin\"," +
                "            \"start\": \"2014-4-26\"," +
                "            \"end\": \"2015-4-26\"," +
                "            \"salesforce\": {" +
                "                \"id\": 8," +
                "                \"name\": \"Another Salesforce Account name\"" +
                "            }," +
                "            \"teams\": [" +
                "                { \"name\": \"Team 1\" }," +
                "                { \"name\": \"Team 2\" }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"id\": 3," +
                "            \"active\": 1," +
                "            \"name\": \"Demo hospital client 3\"," +
                "            \"type\": \"Hospital\"," +
                "            \"region\": \"Midwest\"," +
                "            \"tier\": \"Level 1\"," +
                "            \"owner\": \"Evan\"," +
                "            \"start\": \"2014-5-16\"," +
                "            \"end\": \"2015-5-16\"," +
                "            \"salesforce\": {" +
                "                \"id\": 10," +
                "                \"name\": \"Salesforce Account name\"" +
                "            }," +
                "            \"teams\": [" +
                "                { \"name\": \"Team 1\" }," +
                "                { \"name\": \"Team 2\" }" +
                "            ]" +
                "        }," +
                "        {" +
                "            \"id\": 4," +
                "            \"active\": 1," +
                "            \"name\": \"Demo provider client 4\"," +
                "            \"type\": \"Provider\"," +
                "            \"region\": \"Midwest\"," +
                "            \"tier\": \"Level 2\"," +
                "            \"owner\": \"Kevin\"," +
                "            \"start\": \"2014-4-26\"," +
                "            \"end\": \"2015-4-26\"," +
                "            \"salesforce\": {" +
                "                \"id\": 8," +
                "                \"name\": \"Another Salesforce Account name\"" +
                "            }," +
                "            \"teams\": [" +
                "                { \"name\": \"Team 1\" }," +
                "                { \"name\": \"Team 2\" }" +
                "            ]" +
                "        }" +
                "    ]";
    }

}
