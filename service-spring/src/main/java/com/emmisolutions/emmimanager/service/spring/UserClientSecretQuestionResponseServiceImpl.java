package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SecretQuestion;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import com.emmisolutions.emmimanager.persistence.SecretQuestionPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserClientSecretQuestionResponsePersistence;
import com.emmisolutions.emmimanager.service.UserClientSecretQuestionResponseService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User client secret question response service Impl
 */
@Service
public class UserClientSecretQuestionResponseServiceImpl implements UserClientSecretQuestionResponseService {

    @Resource
    UserClientSecretQuestionResponsePersistence userClientSecretQuestionResponsePersistence;

    @Resource
    SecretQuestionPersistence secretQuestionPersistence;

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    @Transactional
    public UserClientSecretQuestionResponse saveOrUpdate(
            UserClientSecretQuestionResponse questionResponse) {
        if (questionResponse == null ||
                secretQuestionPersistence.reload(questionResponse.getSecretQuestion()) == null ||
                userClientPersistence.reload(questionResponse.getUserClient()) == null ||
                StringUtils.isBlank(questionResponse.getResponse())) {
            throw new InvalidDataAccessApiUsageException("Question Response and Secret Question cannot be null");
        }

        // reload UserClientSecretQuestionResponse
        UserClientSecretQuestionResponse inDb = userClientSecretQuestionResponsePersistence.
                reload(questionResponse);

        // which is user_client_id, secret_question_id
        UserClientSecretQuestionResponse toSave;
        if (inDb != null) {
            // there is currently a response to this question
            toSave = inDb;
            toSave.setResponse(questionResponse.getResponse());
            toSave.setSecretQuestion(questionResponse.getSecretQuestion());
        } else {
            // check to see how many responses are currently stored
            if (userClientSecretQuestionResponsePersistence.findByUserClient(questionResponse.getUserClient(), null)
                    .getNumberOfElements() >= 2) {
                throw new InvalidDataAccessApiUsageException("There are already 2 responses in the database");
            } else {
                toSave = questionResponse;
            }
        }
        return userClientSecretQuestionResponsePersistence.saveOrUpdate(toSave);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SecretQuestion> list(Pageable pageable) {
        return secretQuestionPersistence.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public UserClientSecretQuestionResponse reload(
            UserClientSecretQuestionResponse questionResponse) {
        if (questionResponse == null || questionResponse.getId() == null) {
            return null;
        }
        return userClientSecretQuestionResponsePersistence.reload(questionResponse);
    }

    @Override
    @Transactional
    public Page<UserClientSecretQuestionResponse> findByUserClient(
            UserClient userClient, Pageable pageable) {
        userClient = userClientService.reload(userClient);
        if (userClient == null || userClient.getId() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClient and UserClientId cannot be null");
        }
        return userClientSecretQuestionResponsePersistence
                .findByUserClient(userClient, pageable);

    }

    @Override
    @Transactional
    public UserClient saveOrUpdateUserClient(
            UserClient userClient) {
        UserClient inDb = userClientService.reload(userClient);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        inDb.setSecretQuestionCreated(userClient.isSecretQuestionCreated());
        return userClientPersistence.saveOrUpdate(inDb);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserClientSecretQuestionResponse> findSecretQuestionToken(
            String resetToken, Pageable pageable) {
        if (StringUtils.isNotBlank(resetToken)) {
            UserClient userClient =
                    userClientPersistence.findByResetToken(resetToken);
            return userClientSecretQuestionResponsePersistence
                    .findByUserClient(userClient, pageable);

        }
        return null;
    }

    @Override
    public boolean validateSecurityResponse(
            String resetToken,
            UserClientSecretQuestionResponse questionResponse) {
        boolean ret = false;
        UserClient userClient =
                userClientPersistence.findByResetToken(resetToken);
        boolean isValid = false;
        if(userClient != null && (userClient.getPasswordResetExpirationDateTime() == null || LocalDateTime.now(DateTimeZone.UTC).isBefore(userClient.getPasswordResetExpirationDateTime()))){
            isValid = true;
        }
        if (userClient != null && isValid) {
            if (userClient.isSecurityQuestionsNotRequiredForReset()) {
                // security questions not required for this user
                ret = true;
            } else {
                if (questionResponse != null) {
                    // security questions are required for this user
                    Page<UserClientSecretQuestionResponse> savedResponses =
                            userClientSecretQuestionResponsePersistence
                                    .findByUserClient(userClient, new PageRequest(0, 2));
                    for (UserClientSecretQuestionResponse savedResponse : savedResponses) {
                        if (savedResponse.getSecretQuestion().equals(questionResponse.getSecretQuestion())) {
                            // found the saved question in the db, ensure response text is the same
                            ret = questionResponse.getResponse().replaceAll("\\s+", "")
                                    .equalsIgnoreCase(savedResponse.getResponse().replaceAll("\\s+", ""));
                            break;
                        }
                    }
                }
            }
        } else {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing valid reset token");
        }

        return ret;
    }

}

