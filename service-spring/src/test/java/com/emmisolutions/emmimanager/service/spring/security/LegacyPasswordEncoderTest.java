package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test password implementation
 */
public class LegacyPasswordEncoderTest extends BaseIntegrationTest {
    private Logger LOGGER = LoggerFactory.getLogger(LegacyPasswordEncoderTest.class);

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
        assertThat("password should match",
                passwordEncoder.matches("hudsons1",
                        "6C87C720B80C109472C77065798C6080B3EB53CBA6A2737553FE3CF29BD0D117CED6AD34"), is(true));
    }

    /**
     * This just runs through the generator to give us some passwords
     * uncomment the @Test if you want to make some
     */
//    @Test
    public void createAdminPasswords() {
        String super_admin = passwordEncoder.encode("W9CJrPfl23UbLPwaWYqcTv");
        LOGGER.debug("sa password: {}", super_admin.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        LOGGER.debug("sa salt: {}", super_admin.substring(LegacyPasswordEncoder.PASSWORD_SIZE));

        String emmi_super_user = passwordEncoder.encode("password");
        LOGGER.debug("emmi super user password: {}", emmi_super_user.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        LOGGER.debug("emmi super user salt: {}", emmi_super_user.substring(LegacyPasswordEncoder.PASSWORD_SIZE));

        String qa_admin_user = passwordEncoder.encode("Best2QAs!Ever");
        LOGGER.debug("qa admin user password: {}", qa_admin_user.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        LOGGER.debug("qa admin user salt: {}", qa_admin_user.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
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
        String hash = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String salt = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("round trip password should match", passwordEncoder.matches(password, hash + salt), is(true));
        assertThat("round trip password should not match without salt", passwordEncoder.matches(password, hash), is(false));
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

    /**
     * A null password can be encoded, just not decoded
     */
    @Test
    public void nullPassword() {
        String encodedPasswordPlusHash = passwordEncoder.encode(null);

        // first characters are the password
        String encodedPassword = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String salt = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("encoded password is still generated", encodedPassword.length(), is(LegacyPasswordEncoder.PASSWORD_SIZE));
        assertThat("null password should fail", passwordEncoder.matches(null, encodedPassword + salt), is(false));
    }

    /**
     * A blank password can be encoded, just not decoded
     */
    @Test
    public void blankPassword() {
        String plainText = "        ";
        String encodedPasswordPlusHash = passwordEncoder.encode(plainText);

        // first characters are the password
        String encodedPassword = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String salt = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("null password should not match", passwordEncoder.matches(plainText, encodedPassword + salt), is(false));
    }

    /**
     * A blank password can be encoded, just not decoded
     */
    @Test
    public void blankEncoded() {
        String plainText = "matt";
        String encodedPasswordPlusHash = passwordEncoder.encode(plainText);

        // first characters are the password
        String encodedPassword = encodedPasswordPlusHash.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE);
        // last 32 characters are hash
        String salt = encodedPasswordPlusHash.substring(LegacyPasswordEncoder.PASSWORD_SIZE);

        assertThat("encoded password should  match", passwordEncoder.matches(plainText, encodedPassword + salt), is(true));
        assertThat("null encoded password should not match", passwordEncoder.matches(plainText, null), is(false));
        assertThat("null plainText password should not match", passwordEncoder.matches(null, encodedPassword + salt), is(false));
        assertThat("zero length encoded password should not match", passwordEncoder.matches(plainText, ""), is(false));
    }

}
