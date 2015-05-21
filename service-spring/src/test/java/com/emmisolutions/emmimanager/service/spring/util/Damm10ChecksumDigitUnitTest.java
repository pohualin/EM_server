package com.emmisolutions.emmimanager.service.spring.util;

import com.emmisolutions.emmimanager.service.BaseUnitTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit test of the checksum
 */
public class Damm10ChecksumDigitUnitTest extends BaseUnitTest {

    private Damm10ChecksumDigit checksum = new Damm10ChecksumDigit();

    /**
     * Make sure the checksum can be generated across different input
     * types and that they are equivalent and pass the validity checks
     */
    @Test
    public void roundTrip() {
        String checksumString = checksum.generateCheckSum("345678");
        int checksumInt = checksum.generateCheckSum(345678);
        long checksumLong = checksum.generateCheckSum(345678l);

        assertThat("string and int are equivalent", checksumString, equalTo(String.valueOf(checksumInt)));
        assertThat("string and long are equivalent", checksumString, equalTo(String.valueOf(checksumLong)));
        assertThat("string is valid", checksum.validate(checksumString), is(true));
        assertThat("long is valid", checksum.validate(checksumInt), is(true));
        assertThat("int is valid", checksum.validate(checksumLong), is(true));
    }

    /**
     * Shouldn't be able to generate a checksum on a non numeric number
     */
    @Test(expected = RuntimeException.class)
    public void nonNumeric() {
        checksum.generateCheckSum("AAA");
    }

}
