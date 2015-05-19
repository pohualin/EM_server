package com.emmisolutions.emmimanager.service.spring.util;

import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Generates unique access codes
 */
@Component
public class AccessCodeGenerator {

    private static final Random random = new SecureRandom();

    @Resource
    Damm10ChecksumDigit checksum;

    @Resource
    SchedulePersistence schedulePersistence;

    public String next() {
        return findUniqueCode();
    }

    private String findUniqueCode(){
        String ret = generateRandomCode();
        while (!schedulePersistence.isAccessCodeUnique(ret)){
            ret = generateRandomCode();
        }
        return ret;
    }

    private String generateRandomCode(){
        return checksum.generateCheckSum(String.format("2%09d", random.nextInt(999999999)));
    }
}
