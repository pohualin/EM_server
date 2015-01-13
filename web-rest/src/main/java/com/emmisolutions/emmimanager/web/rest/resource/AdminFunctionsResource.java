package com.emmisolutions.emmimanager.web.rest.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagResourceAssembler;

@RestController
@RequestMapping(value = "/admin", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class AdminFunctionsResource {

@Resource
ProviderService providerService;

@Resource
ReferenceTagResourceAssembler referenceTagResourceAssembler;
    /**
     * GET to retrieve Reference Tags with Specialty type.
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return ReferenceTagPage matching the search request
     */
    @RequestMapping(value = "/referenceTags", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD"})
    public ResponseEntity<ReferenceTagPage> getRefData(@PageableDefault(size = 10) Pageable pageable,
                                                       @SortDefault(sort = "id") Sort sort,
                                                       PagedResourcesAssembler<ReferenceTag> assembler) {

        Page<ReferenceTag> tagPage = providerService.findAllSpecialties(pageable);

        if (tagPage.hasContent()) {
            return new ResponseEntity<>(new ReferenceTagPage(assembler.toResource(tagPage, referenceTagResourceAssembler), tagPage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
