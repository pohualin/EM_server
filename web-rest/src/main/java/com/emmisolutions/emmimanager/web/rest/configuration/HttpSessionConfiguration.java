package com.emmisolutions.emmimanager.web.rest.configuration;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * A session listener that sets the session timeout interval..
 */
@WebListener
public class HttpSessionConfiguration implements HttpSessionListener {

    private transient static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionConfiguration.class);

    private static final int TIMEOUT_MINUTES = 15;

    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(TIMEOUT_MINUTES * 60);
        LOGGER.debug("Session ID: {} created at {} and is valid for {} minutes", event.getSession().getId(), DateTime.now(), TIMEOUT_MINUTES);
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        LOGGER.debug("Session ID: {} destroyed at {}, last activity was {}", event.getSession().getId(), DateTime.now(), new DateTime(event.getSession().getLastAccessedTime()));
    }

}

