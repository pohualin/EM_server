package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientService.UserClientConflict;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.security.RootTokenBasedRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.client.model.ValidationToken;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.resource.TrackingEmailsResource.emailViewedTrackingLink;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


/**
 * User REST API
 */
@RestController("clientUserClientsResource")
@RequestMapping(value = "/webapi-client", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class UserClientsResource {

    private static final String VALIDATION_CLIENT_APPLICATION_URI = "#/validateEmail/%s";
    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;
    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<UserClient, UserClientResource> userResourceAssembler;
    @Resource(name = "userClientResourceAssembler")
    ResourceAssembler<UserClient, UserClientResource>
            userClientResourceAssembler;
    @Resource(name = "clientUserConflictResourceAssembler")
    ResourceAssembler<List<UserClientService.UserClientConflict>, UserClientResource>
            conflictsResourceAssembler;
    @Resource(name = "clientUserClientResourceAssembler")
    ResourceAssembler<UserClient, UserClientResource> clientUserClientResourceAssembler;
    @Resource
    UserClientSecretQuestionResponseService userClientSecretQuestionResponseService;
    @Resource
    MailService mailService;
    @Resource(name = "clientCsrfAuthenticationStrategy")
    CsrfAuthenticationStrategy clientCsrfAuthenticationStrategy;
    @Resource
    UserClientService userClientService;
    @Resource(name = "clientUserClientValidationErrorResourceAssembler")
    ResourceAssembler<UserClientService.UserClientValidationError, UserClientResource>
            clientUserClientValidationErrorResourceAssembler;
    @Resource
    UserClientValidationEmailService userClientValidationEmailService;
    @Resource(name = "clientTokenBasedRememberMeServices")
    RootTokenBasedRememberMeServices tokenBasedRememberMeServices;
    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    /**
     * send validation email
     *
     * @param userId user to get for email personalization
     * @return OK (200): if everything worked
     * <p/>
     * GONE (410): if the validation token is expired or invalid
     */
    @RequestMapping(value = "/user_client/{userId}/send_email", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user, #userId)")
    public ResponseEntity<Void> sendValidationEmail(@PathVariable Long userId) {
        // update the validation token, invalidating others
        UserClient savedUserClient = userClientValidationEmailService.addValidationTokenTo(new UserClient(userId));
        if (savedUserClient != null) {
            // get the proper url (the way we make hateoas links), then replace the path with the client entry point
            String validationHref =
                    UriComponentsBuilder.fromHttpUrl(
                            linkTo(methodOn(UserClientsResource.class)
                                    .validateEmailToken(null)).withSelfRel().getHref())
                            .replacePath(clientEntryPoint + String.format(VALIDATION_CLIENT_APPLICATION_URI,
                                    savedUserClient.getValidationToken()))
                            .build(false)
                            .toUriString();
            // send the email (asynchronously)
            mailService.sendValidationEmail(savedUserClient, validationHref, emailViewedTrackingLink());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * validate email token
     *
     * @param validationToken token to validate
     * @return OK (200): if everything worked
     * <p/>
     * GONE (410): if the email token is not valid
     */
    @RequestMapping(value = "/validate/", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@validateEmailWithinIpRange, #validationToken.validationToken)")
    public ResponseEntity<Void> validateEmailToken(@RequestBody ValidationToken validationToken) {
        UserClient savedUserClient = userClientValidationEmailService
                .validateEmailToken(validationToken.getValidationToken());
        if (savedUserClient != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * GET to retrieve authenticated user.
     *
     * @return OK (200): containing UserClientResource when authorized or 401 if the user is not
     * authorized.
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    public ResponseEntity<UserClientResource> authenticated() {
        return new ResponseEntity<>(
                userResourceAssembler.toResource((UserClient) userDetailsService
                        .getLoggedInUser()), HttpStatus.OK);
    }

    /**
     * PUT for updates to a given user client
     *
     * @param userClientId to update
     * @param password     to verify permission by password
     * @param userClient   the updated user client
     * @return OK (200): containing the newly updated UserClientResource
     * <p/>
     * NOT_ACCEPTABLE (406): containing the UserClients that are conflicting with the inbound request. This
     * happens when there are conflicts with the save (same login, email or email mask doesn't match)
     * <p/>
     * INTERNAL_SERVER_ERROR (500): when the save doesn't return an updated client.
     */
    @RequestMapping(value = "/user_client/{userClientId}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@user, #userClientId) and hasPermission(@password, #password)")
    public ResponseEntity<UserClientResource> updateUserClientEmail(@PathVariable("userClientId") Long userClientId,
                                                                    @RequestParam(value = "password", required = false) String password,
                                                                    @RequestBody UserClient userClient,
                                                                    HttpServletRequest request,
                                                                    HttpServletResponse response) {

        userClient.setId(userClientId);
        if (StringUtils.isNotBlank(userClient.getEmail())
                && !userClientService.validateEmailAddress(userClient)) {
            UserClientService.UserClientValidationError validationError = new UserClientService.UserClientValidationError(
                    UserClientService.Reason.EMAIL_RESTRICTION, userClient);
            return new ResponseEntity<>(clientUserClientValidationErrorResourceAssembler.toResource(validationError), HttpStatus.NOT_ACCEPTABLE);
        } else {

            // look for conflicts before attempting to save
            List<UserClientConflict> conflicts = userClientService.findConflictingUsers(userClient);

            if (conflicts.isEmpty()) {
                UserClient updatedUserClient = userClientService.update(userClient);

                // update auth token
                tokenBasedRememberMeServices.rewriteLoginToken(request, response, updatedUserClient);

                // update CSRF token due to login token changing
                clientCsrfAuthenticationStrategy.onAuthentication(null, request, response);

                if (updatedUserClient != null) {
                    return new ResponseEntity<>(clientUserClientResourceAssembler.toResource(updatedUserClient), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // found some conflicting users
                return new ResponseEntity<>(conflictsResourceAssembler.toResource(conflicts), HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    /**
     * GET for a given user client
     *
     * @param userClientId the user client to secure
     * @return OK (200): that has the UserClientResource
     */
    @RequestMapping(value = "/user_client/{userClientId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientResource> getById(@PathVariable("userClientId") Long userClientId) {
        return new ResponseEntity<>(
                clientUserClientResourceAssembler.toResource(
                        userDetailsService.get(new UserClient(userClientId))), HttpStatus.OK);
    }

    /**
     * GET for a given user client verified with password
     *
     * @param password to check
     * @return OK (200) if the password checks out, FORBIDDEN (403) if it is wrong
     */
    @RequestMapping(value = "/user_client/verify_password", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@password, #password)")
    public ResponseEntity<Void> verifyPassword(@RequestParam(value = "password", required = false) String password) {

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * PUT for a given user client verified with password
     *
     * @param userClientId to secure
     * @return OK (200) always
     */
    @RequestMapping(value = "/user_client/{userClientId}/not_now", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<Void> notNow(@PathVariable("userClientId") Long userClientId) {

        userClientService.saveNotNowExpirationTime(userClientId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * PUT for secret question flag to a given user client
     *
     * @param userClientId           user client id
     * @param secretQuestionsCreated secret question has created or not
     * @return OK (200): containing the UserClientResource
     * GONE (410): if the updated user client is null
     */
    @RequestMapping(value = "/user_client/{userClientId}/secret_questions_created", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientResource> updateUserClient(
            @PathVariable("userClientId") Long userClientId,
            @RequestParam(value = "secretQuestionsCreated", required = false) Boolean secretQuestionsCreated) {

        UserClient userClient = new UserClient(userClientId);
        userClient.setSecretQuestionCreated(secretQuestionsCreated);
        userClient = userClientSecretQuestionResponseService.saveOrUpdateUserClient(userClient);

        if (userClient != null) {
            return new ResponseEntity<>(clientUserClientResourceAssembler.toResource(userClient), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.GONE);
        }

    }

    /**
     * PUT lock the user who has the passed reset token
     *
     * @param resetToken password reset token for user client
     * @return OK (200): LocalDateTime locked out timestamp
     * NO_CONTENT (204): if no user were locked out with the reset token
     */
    @RequestMapping(value = "/user_client/lock_out_user/with_reset_token", method = RequestMethod.PUT)
    @PermitAll
    public ResponseEntity<LocalDateTime> lockOutUserWithResetToken(
            @RequestParam(value = "token", required = false) String resetToken) {

        UserClient userClient = userClientService.lockedOutUserWithResetToken(resetToken);
        if (userClient != null && userClient.getLockExpirationDateTime() != null) {
            return new ResponseEntity<>(userClient.getLockExpirationDateTime(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
