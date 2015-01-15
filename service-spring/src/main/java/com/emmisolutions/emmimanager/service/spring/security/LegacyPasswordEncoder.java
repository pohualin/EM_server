package com.emmisolutions.emmimanager.service.spring.security;

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

    private static int PASSWORD_BITS = 160;
    /**
     * The password size is num bits * 1 byte/8 bits * 2 hex chars/byte.
     * So for SHA1 160 bit password we have 40 Hex characters.
     */
    public static int PASSWORD_SIZE = PASSWORD_BITS / 8 * 2;
    public static int HASH_SIZE = 32;
    private static int BYTES = HASH_SIZE / 2;

    private SecureRandom random = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        String plainText;
        if (rawPassword == null) {
            plainText = "";
        } else {
            plainText = rawPassword.toString();
        }
        byte[] salt = new byte[BYTES];
        random.nextBytes(salt);
        return String.valueOf(Hex.encode(hashPassword(plainText, salt)))
                + String.valueOf(Hex.encode(salt));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.length() <= HASH_SIZE) {
            // password must be longer than hash
            return false;
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            // Empty encoded password
            return false;
        }
        String hash = rawPassword.subSequence(rawPassword.length() - HASH_SIZE, rawPassword.length()).toString();
        String plainText = rawPassword.subSequence(0, rawPassword.length() - HASH_SIZE).toString();

        // create the encoded Hex string from the password and hash
        String hashedAttempt = new String(Hex.encode(hashPassword(plainText, Hex.decode(hash))));
        return hashedAttempt.equalsIgnoreCase(encodedPassword);
    }

    private byte[] hashPassword(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 64000, 160);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Cannot hash password", e);
        }
    }

}
