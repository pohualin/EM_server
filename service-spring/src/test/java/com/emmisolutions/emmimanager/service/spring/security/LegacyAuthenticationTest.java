package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test password implementation
 */
public class LegacyAuthenticationTest extends BaseIntegrationTest {


    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * This is a one way test to ensure that a known password in the
     * legacy emmi database can be authenticated against successfully.
     * <p/>
     * User: hudson
     * Password: hudsons1
     * Password Hash: 0x6C87C720B80C109472C77065798C6080B3EB53CB
     * Password Salt: 0xA6A2737553FE3CF29BD0D117CED6AD34
     */
    @Test
    public void emmiPassword() {
        String passwordEnteredByUser = "hudsons1A6A2737553FE3CF29BD0D117CED6AD34".toLowerCase();

        assertThat("password should match",
                passwordEncoder.matches(passwordEnteredByUser,
                        "6C87C720B80C109472C77065798C6080B3EB53CB"), is(true));
    }

    /**
     * This ensures that a password can be encoded using the encoder and then
     * using the plain text plus the hash, can be validated against the
     * encoded password itself.
     */
    @Test
    public void roundTrip() {
        String password = "matt_wuz_here||^";
        String encodedPasswordPlusHash = passwordEncoder.encode(password);

        // first characters are the password
        String encodedPassword = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String hash = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("round trip password should match", passwordEncoder.matches(password + hash, encodedPassword), is(true));
    }

    /**
     * No matter what the password the resulting length will be the same
     */
    @Test
    public void sameLength() {
        assertThat("lengths of two different passwords is the same", passwordEncoder.encode("1").length(),
                is(passwordEncoder.encode("1234567890asdfkajsdflkjasdlfkjlaksdjflkajsdflkjalsdkjflkasjdf").length()));
    }

    /**
     * Encoding the same text twice should yield different results
     */
    @Test
    public void samePasswordEncodedTwiceResultsInDifferentPassword() {
        assertThat("same password encoded twice is not the same", passwordEncoder.encode("1"),
                is(not(passwordEncoder.encode("1"))));
    }

    @Test
    public void nullPassword() {
        String encodedPasswordPlusHash = passwordEncoder.encode(null);

        // first characters are the password
        String encodedPassword = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String hash = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("null password should fail", passwordEncoder.matches("" + hash, encodedPassword), is(false));
    }

}
