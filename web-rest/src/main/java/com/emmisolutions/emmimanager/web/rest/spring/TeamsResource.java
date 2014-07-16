package com.emmisolutions.emmimanager.web.rest.spring;

import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.web.rest.model.TeamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/webapi",
        consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE},
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class TeamsResource {

    @Resource
    TeamService teamService;

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.GET)
    public ResponseEntity<TeamResource> get(@PathVariable("id") Long id) {
        return null;
    }

//    @RequestMapping(value = "/clients/{id}/teams", method = RequestMethod.GET)
//    public ResponseEntity<TeamPageResource> listByClient(
//            @PathVariable("id") Long id,
//            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
//            @RequestParam(value = "max", required = false, defaultValue = "10") Integer max) {
//        return null;
//    }

}
