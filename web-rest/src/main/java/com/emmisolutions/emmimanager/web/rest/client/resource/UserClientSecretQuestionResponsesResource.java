package com.emmisolutions.emmimanager.web.rest.client.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
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
    * 
    * @param pageable
    * @param sort
    * @param assembler
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
     * 
     * @param userClientId
     * @param id
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
     * 
     * @param userClientId
     * @param userClientSecretQuestionResponse
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.POST)
    public ResponseEntity<UserClientSecretQuestionResponseResource> saveOrUpdate(
            @PathVariable("userClientId") Long userClientId,
            @RequestBody UserClientSecretQuestionResponse userClientSecretQuestionResponse) {
       
        userClientSecretQuestionResponse.setUserClient(new UserClient(userClientId));
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
     * 
     * @param userClientId
     * @param pageable
     * @param assembler
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.GET)
    public ResponseEntity<UserClientSecretQuestionResponsePage> secretQuestionResponses(
            @PathVariable("userClientId") Long userClientId,
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

}
