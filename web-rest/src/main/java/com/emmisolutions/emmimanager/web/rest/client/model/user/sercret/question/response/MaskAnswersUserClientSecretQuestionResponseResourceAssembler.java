package com.emmisolutions.emmimanager.web.rest.client.model.user.sercret.question.response;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;
import org.springframework.stereotype.Component;

/**
 * Extension of the SecretQuestionResponse Assembler that masks the answers
 */
@Component("maskAnswersUserClientSecretQuestionResponseResourceAssembler")
public class MaskAnswersUserClientSecretQuestionResponseResourceAssembler extends
        UserClientSecretQuestionResponseResourceAssembler {

    @Override
    public UserClientSecretQuestionResponseResource toResource(
            UserClientSecretQuestionResponse entity) {
        UserClientSecretQuestionResponseResource ret = super.toResource(entity);
        ret.getEntity().setResponse("***************************");
        return ret;
    }
      
}
