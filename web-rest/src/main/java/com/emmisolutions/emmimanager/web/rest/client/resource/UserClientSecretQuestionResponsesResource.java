package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.mail.TrackingService;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.SecretQuestionResource;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponsePage;
import com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response.UserClientSecretQuestionResponseResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
public class UserClientSecretQuestionResponsesResource {

    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;

    @Resource
    UserClientService userClientService;

    @Resource(name = "userClientSecretQuestionResponseResourceAssembler")
    ResourceAssembler<UserClientSecretQuestionResponse, UserClientSecretQuestionResponseResource>
            questionResponseAssembler;

    @Resource(name = "maskAnswersUserClientSecretQuestionResponseResourceAssembler")
    ResourceAssembler<UserClientSecretQuestionResponse, UserClientSecretQuestionResponseResource>
            maskResponsesAssembler;

    @Resource(name = "secretQuestionResourceAssembler")
    ResourceAssembler<SecretQuestion, SecretQuestionResource> secretQuestionAssembler;

    @Resource
    TrackingService trackingService;

    /**
     * Get the page of secret question response
     *
     * @param pageable  the pagination
     * @return secret question entity
     */
    @RequestMapping(value = "/secret_questions", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "rank,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    @PermitAll
    public ResponseEntity<Page<SecretQuestion>> secretQuestions(
            @PageableDefault(size = 10, sort = "rank") Pageable pageable) {

        Page<SecretQuestion> page = userClientSecretQuestionResponseService
                .list(pageable);
        if (page != null) {
            return new ResponseEntity<>(page, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get the user client secret question response with the secret question response id
     *
     * @param userClientId the user client id
     * @param id           the secret question response id
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientSecretQuestionResponseResource> get(
            @PathVariable("userClientId") Long userClientId,
            @PathVariable("id") Long id) {

        UserClientSecretQuestionResponse secretQuestionResponse = userClientSecretQuestionResponseService
                .reload(new UserClientSecretQuestionResponse(id));

        if (secretQuestionResponse != null) {
            return new ResponseEntity<>(
                    questionResponseAssembler
                            .toResource(secretQuestionResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Save or update the user client secret question response
     *
     * @param userClientId                     the user client id
     * @param userClientSecretQuestionResponse the user client secret question response
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientSecretQuestionResponseResource> saveOrUpdate(
            @PathVariable("userClientId") Long userClientId,
            @RequestBody UserClientSecretQuestionResponse userClientSecretQuestionResponse) {

        userClientSecretQuestionResponse.setUserClient(new UserClient(userClientId));
        UserClientSecretQuestionResponse secretQuestionResponse = userClientSecretQuestionResponseService
                .saveOrUpdate(userClientSecretQuestionResponse);

        if (secretQuestionResponse != null) {
            return new ResponseEntity<>(
                    questionResponseAssembler
                            .toResource(secretQuestionResponse),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the user client secret question response
     *
     * @param userClientId the user client id
     * @param pageable     the pagination
     * @param assembler    the assembler
     * @param password     pass in password to verify
     * @return user client secret question response entity
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    @PreAuthorize("hasPermission(@password, #password)")
    public ResponseEntity<UserClientSecretQuestionResponsePage> secretQuestionResponses(
            @PathVariable("userClientId") Long userClientId,
            @RequestParam(value = "password", required = false) String password,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<UserClientSecretQuestionResponse> assembler) {
        Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findByUserClient(new UserClient(userClientId), pageable);
        if (page != null) {
            return new ResponseEntity<>(
                    new UserClientSecretQuestionResponsePage(
                            assembler
                                    .toResource(page,
                                            questionResponseAssembler),
                            page), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Get the user client secret question asterisk responses
     *
     * @param userClientId the user client id
     * @param assembler    the assembler
     * @param pageable     the page specification
     * @return user client secret question response entity with asterisk response
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions/responses_masked", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientSecretQuestionResponsePage> secretQuestionAsteriskResponse(
            @PathVariable("userClientId") Long userClientId,
            PagedResourcesAssembler<UserClientSecretQuestionResponse> assembler,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findByUserClient(new UserClient(userClientId), pageable);
        if (page != null) {
            return new ResponseEntity<>(
                    new UserClientSecretQuestionResponsePage(
                            assembler
                                    .toResource(page,
                                            maskResponsesAssembler),
                            page), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * Get the user client secret question with empty responses
     *
     * @param resetToken password reset token for user client
     * @param pageable   the page specification
     * @param assembler  to assemble SecretQuestionResource objects
     * @return secret questions with empty response
     */
    @RequestMapping(value = "/secret_questions/using_reset_token", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    @PreAuthorize("hasPermission(@resetWithinIpRange, #resetToken)")
    public ResponseEntity<UserClientSecretQuestionResponsePage> getSecretQuestionWithResetToken(
            @RequestParam(value = "token", required = false) String resetToken,
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            PagedResourcesAssembler<UserClientSecretQuestionResponse> assembler) {

        Page<UserClientSecretQuestionResponse> page = userClientSecretQuestionResponseService
                .findSecretQuestionToken(resetToken, pageable);
        if (page != null) {
            return new ResponseEntity<>(
                    new UserClientSecretQuestionResponsePage(
                            assembler
                                    .toResource(page,
                                            maskResponsesAssembler),
                            page), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * PUT for secret question response for a given user client to verify
     *
     * @param resetToken                       user client id
     * @param userClientSecretQuestionResponse secret question for verification
     * @return OK (200): true - secret question responses are all good, false - not good
     */
    @RequestMapping(value = "/secret_questions/is_response_correct", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@resetWithinIpRange, #resetToken)")
    public ResponseEntity<Boolean> validateSecretResponses(
            @RequestParam(value = "token", required = false) String resetToken,
            @RequestParam(value = "trackingToken", required = false) String trackingToken,
            @RequestBody UserClientSecretQuestionResponse userClientSecretQuestionResponse) {

        trackingService.actionTaken(trackingToken); // action taken on reset email
        return new ResponseEntity<>(userClientSecretQuestionResponseService.validateSecurityResponse(resetToken,
                userClientSecretQuestionResponse), HttpStatus.OK);
    }
}
