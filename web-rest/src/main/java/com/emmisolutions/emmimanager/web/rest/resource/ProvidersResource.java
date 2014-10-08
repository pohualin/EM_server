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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.ReferenceTagService;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceTagResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResource;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResourceAssembler;

/**
 * Providers REST API
 *
 */

@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
		APPLICATION_XML_VALUE })
public class ProvidersResource {

	@Resource
	ProviderService providerService;
	
	@Resource
	ReferenceTagService referenceTagService;
	
	@Resource
	ProviderResourceAssembler providerResourceAssembler;
	
    @Resource
    ReferenceTagResourceAssembler referenceTagResourceAssembler;

    /**
     * POST for creating a Provider
     * @param Provider
     * @return ProviderResource
     *
     */
	@RequestMapping(value = "/provider", method = RequestMethod.POST)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_CREATE" })
	public ResponseEntity<ProviderResource> create(
			@RequestBody Provider provider) {

		provider = providerService.create(provider);
		if (provider == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(
					providerResourceAssembler.toResource(provider),
					HttpStatus.CREATED);
		}

	}

	 /**
     * GET for searching for providers
     * 
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @param name
     * @param status
     * @return ProviderResource
     */
	@RequestMapping(value = "/provider", method = RequestMethod.GET)
	@RolesAllowed({ "PERM_GOD", "PERM_PROVIDER_LIST" })
	public ResponseEntity<ProviderPage> findProviders(
			@PageableDefault(size = 50) Pageable pageable,
			@SortDefault(sort = "id") Sort sort,
			@RequestParam(value = "status", required = false) String status,
			PagedResourcesAssembler<Provider> assembler,
			@RequestParam(value = "name", required = false) String... names) {


		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
    /**
     * GET to retrieve ReferenceData Specialty types for Provider.
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return ReferenceTagPage matching the search request
     */
    @RequestMapping(value = "/referenceDataSpecialties", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_PROVIDER_VIEW"})
    public ResponseEntity<ReferenceTagPage> getRefData(@PageableDefault(size = 50) Pageable pageable,
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
