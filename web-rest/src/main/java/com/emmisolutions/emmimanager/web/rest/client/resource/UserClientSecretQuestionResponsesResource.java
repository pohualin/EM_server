package com.emmisolutions.emmimanager.web.rest.client.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResourceAssembler;
import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponsePage;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponseResource;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponseResourceAssembler;

@RestController
@RequestMapping(value = "/webapi-client", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class UserClientSecretQuestionResponsesResource {

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;
   
    @Resource
    UserClientService userClientService;
    
    @Resource
    UserClientSecretQuestionResponseResourceAssembler questionResponseAssembler;
    
   /**
    * Get the list of secret question response 
    * @param pageable the pagination
    * @param sort sort by rank
    * @param assembler the assembler
    * @return secret question response entity
    */
    @RequestMapping(value = "/secret_questions/questions", method = RequestMethod.GET)
    public ResponseEntity<Page<SecretQuestion>> secretQuestions(
            @PageableDefault(size = 10, sort = "rank") Pageable pageable,
            @SortDefault(sort = "rank") Sort sort,
            PagedResourcesAssembler<SecretQuestion> assembler) {

        Page<SecretQuestion> page = userClientSecretQuestionResponseService
                .list(pageable);
        
        if(page != null) {
        	return new ResponseEntity<>(page, HttpStatus.OK);
          
        }else{
        	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
   }


    /**
     * Get the user client secret question response with the secret question response id
     * @param userClientId the user client id
     * @param id the secret question response id
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserClientSecretQuestionResponseResource> get(
            @PathVariable("userClientId") Long userClientId,
            @PathVariable("id") Long id) {
        
        UserClientSecretQuestionResponse ucsqr = userClientSecretQuestionResponseService
                .reload(new UserClientSecretQuestionResponse(id));
               
        if( ucsqr != null) {
            return new ResponseEntity<>(
            		questionResponseAssembler
                        .toResource(ucsqr),
                        HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
             
    }


    /**
     * Save or update the user client secret question response
     * @param userClientId the user client id
     * @param userClientSecretQuestionResponse the user client secret question response
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientSecretQuestionResponseResource> saveOrUpdate(
            @PathVariable("userClientId") Long userClientId,
            @RequestBody UserClientSecretQuestionResponse userClientSecretQuestionResponse) {

        UserClient savedUserClient = userClientService.reload(new UserClient(userClientId));
    	userClientSecretQuestionResponse.setUserClient(savedUserClient);
        UserClientSecretQuestionResponse ucsqr = userClientSecretQuestionResponseService
                .saveOrUpdate(userClientSecretQuestionResponse);

        if( ucsqr != null) {
            return new ResponseEntity<>(
            		questionResponseAssembler
                        .toResource(ucsqr),
                        HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the user client secret question response 
     * @param userClientId the user client id
     * @param pageable the pagination
     * @param assembler the assembler
     * @param password pass in password to verify
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@password, #password)")
    public ResponseEntity<UserClientSecretQuestionResponsePage> secretQuestionResponses(
            @PathVariable("userClientId") Long userClientId,
            @RequestParam(value = "password", required = false) String password,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<UserClientSecretQuestionResponse> assembler) {
    	Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findByUserClient(new UserClient(userClientId), pageable);
        if(page != null) {
            return new ResponseEntity<>(
                    new UserClientSecretQuestionResponsePage(
                            assembler
                                .toResource(page,
                                		questionResponseAssembler),
                                        page), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
    }
    
    /**
     * Get the user client secret question asterisk responses 
     * @param userClientId the user client id
     * @param assembler the assembler
     * @param userClient user client to verify
     * @return user client secret question response entity with asterisk response
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/asteriskResponses", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientSecretQuestionResponsePage> secretQuestionAsteriskResponse(
            @PathVariable("userClientId") Long userClientId,
            PagedResourcesAssembler<UserClientSecretQuestionResponse> assembler) {
       
       Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findByUserClient(new UserClient(userClientId), new PageRequest(0, 10));
       if(page != null) {
        	Pageable pageRequest=new PageRequest(0, 10, Sort.Direction.ASC, "id");
            List<UserClientSecretQuestionResponse> page2 = new ArrayList<UserClientSecretQuestionResponse>();
        	for(UserClientSecretQuestionResponse response : page){
        		response.setResponse("**********");
        	    page2.add(response);
        	}
        	Page<UserClientSecretQuestionResponse> asteriskPage = new PageImpl<UserClientSecretQuestionResponse>(page2,pageRequest,0);
            return new ResponseEntity<>(
                    new UserClientSecretQuestionResponsePage(
                            assembler
                                .toResource(asteriskPage,
                                		questionResponseAssembler),
                                		asteriskPage), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
    }
    
    
   

}
