package com.emmisolutions.emmimanager.model.constraint;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Does a Damm checksum validation for a String
 */
public class DammChecksumConstraintValidator implements ConstraintValidator<DammChecksum,String> {

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

    @Override
    public void initialize(DammChecksum constraintAnnotation) {
        // no - op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNotEmpty(value) && calculateCheckSumDigit(value) == 0;
    }

    private int calculateCheckSumDigit(String number) {

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
}
