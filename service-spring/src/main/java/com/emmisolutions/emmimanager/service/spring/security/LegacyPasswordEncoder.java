package com.emmisolutions.emmimanager.service.spring.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * A PasswordEncoder for legacy Emmi passwords
 */
@Component
public class LegacyPasswordEncoder implements PasswordEncoder {

    private static int PASSWORD_BITS = 160; // maximum strength for SHA1
    /**
     * The password size is num bits * 1 byte/8 bits * 2 hex chars/byte.
     * So for SHA1 160 bit password we have 40 Hex characters.
     */
    public static int PASSWORD_SIZE = PASSWORD_BITS / 8 * 2;
    public static int SALT_SIZE = 32;
    private static int SALT_BYTES = SALT_SIZE / 2;

    private SecureRandom random = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        String plainText;
        if (rawPassword == null) {
            plainText = "";
        } else {
            plainText = rawPassword.toString();
        }
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return String.valueOf(Hex.encode(hashPassword(plainText, salt))).toUpperCase()
                + String.valueOf(Hex.encode(salt)).toUpperCase();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (StringUtils.isBlank(rawPassword)) {
            // blank passwords should not match ever
            return false;
        }
        if (encodedPassword == null || encodedPassword.length() != PASSWORD_SIZE + SALT_SIZE) {
            // bad encoded password
            return false;
        }
        // split the encoded password into SALT and HASH
        String salt = encodedPassword.subSequence(encodedPassword.length() - SALT_SIZE, encodedPassword.length()).toString();
        String hash = encodedPassword.subSequence(0, encodedPassword.length() - SALT_SIZE).toString();

        // create the encoded Hex string from the password and hash
        String hashedAttempt = new String(Hex.encode(hashPassword(rawPassword, Hex.decode(salt))));

        // compare the hex string created from the salt and raw password with the existing hash
        return hashedAttempt.equalsIgnoreCase(hash);
    }

    private byte[] hashPassword(CharSequence password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toString().toCharArray(), salt, 64000, 160);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Cannot hash password", e);
        }
    }

}
