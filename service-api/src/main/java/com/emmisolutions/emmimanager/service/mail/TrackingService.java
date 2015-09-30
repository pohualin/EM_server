package com.emmisolutions.emmimanager.service.mail;

/**
 * For tracking emails
 */
public interface TrackingService {

    /**
     * Used to hold the constant for signature swapping during email context generation and
     * link generation to the resource
     */
    String SIGNATURE_VARIABLE_NAME = "TO_BE_REPLACED_WITH_SIGNATURE";

    /**
     * Handles when an email is viewed
     *
     * @param signature of the tracker
     */
    void viewed(String signature);

    /**
     * Handles when an email link is clicked on
     *
     * @param signature of the tracker
     */
    void actionTaken(String signature);
}
