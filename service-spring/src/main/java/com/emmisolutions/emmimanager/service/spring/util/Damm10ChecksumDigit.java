package com.emmisolutions.emmimanager.service.spring.util;

import org.springframework.stereotype.Component;

/**
 * The implementation of the damm algorithm based on the details on http://en.wikipedia.org/wiki/Damm_algorithm
 */
@Component
public class Damm10ChecksumDigit {

    /**
     * The quasigroup table from http://en.wikipedia.org/wiki/Damm_algorithm
     */
    private static int[][] matrix = new int[][]{
            {0, 3, 1, 7, 5, 9, 8, 6, 4, 2},
            {7, 0, 9, 2, 1, 5, 4, 8, 6, 3},
            {4, 2, 0, 6, 8, 7, 1, 3, 5, 9},
            {1, 7, 5, 0, 9, 8, 3, 4, 2, 6},
            {6, 1, 2, 3, 0, 4, 5, 9, 7, 8},
            {3, 6, 7, 4, 2, 0, 9, 5, 8, 1},
            {5, 8, 6, 9, 7, 2, 0, 1, 3, 4},
            {8, 9, 4, 5, 3, 6, 2, 0, 1, 7},
            {9, 4, 3, 8, 6, 1, 7, 2, 0, 5},
            {2, 5, 8, 1, 4, 3, 6, 7, 9, 0}};

    /**
     * Calculate the checksum digit from provided number
     *
     * @param number to calculate on
     * @return calculated Damm checksum digit
     */
    public int calculateCheckSumDigit(String number) {

        int interim = 0;
        for (int index = 0; index < number.length(); index++) {
            char currCh = number.charAt(index);
            if (!Character.isDigit(currCh)) {
                throw new RuntimeException(number + " is not a valid number");
            }

            int currentIndex = currCh - 48;
            interim = matrix[interim][currentIndex];
        }

        return interim;
    }

    /**
     * Calculate the checksum digit from provided number
     *
     * @param number to use
     * @return calculated Damm checksum digit
     */
    public int calculateCheckSumDigit(int number) {
        return calculateCheckSumDigit(String.valueOf(number));
    }

    /**
     * Calculate the checksum digit from provided number
     *
     * @param number to use
     * @return calculated Damm checksum digit
     */
    public int calculateCheckSumDigit(long number) {
        return calculateCheckSumDigit(String.valueOf(number));
    }

    /**
     * Calculate the checksum digit from provided number and return the full
     * number with the checksum
     *
     * @param number to use
     * @return full number with the Damm checksum
     */
    public String generateCheckSum(String number) {
        int checkSumDigit = calculateCheckSumDigit(number);

        return number + String.valueOf(checkSumDigit);
    }

    /**
     * Calculate the checksum digit from provided number and return the full
     * number with the checksum
     *
     * @param number to use
     * @return full number with the Damm checksum
     */
    public int generateCheckSum(int number) {
        int checkSumDigit = calculateCheckSumDigit(number);
        return (number * 10) + checkSumDigit;
    }

    /**
     * Calculate the checksum digit from provided number and return the full
     * number with the checksum
     *
     * @param number to use
     * @return full number with the Damm checksum
     */
    public long generateCheckSum(long number) {
        int checkSumNumber = calculateCheckSumDigit(number);
        return (number * 10) + checkSumNumber;
    }

    /**
     * validates the number using the last digit as the Damm checksum
     *
     * @param number to use
     * @return True if valid; otherwise false
     */
    public boolean validate(String number) {
        return calculateCheckSumDigit(number) == 0;
    }

    /**
     * validates the number using the last digit as the Damm checksum
     *
     * @param number to use
     * @return True if valid; otherwise false
     */
    public boolean validate(int number) {
        return calculateCheckSumDigit(number) == 0;
    }

    /**
     * validates the number using the last digit as the Damm checksum
     *
     * @param number to use
     * @return True if valid; otherwise false
     */
    public boolean validate(long number) {
        return calculateCheckSumDigit(number) == 0;
    }
}
