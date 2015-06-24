package com.emmisolutions.emmimanager.salesforce.wsc;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Manages the salesforce connection
 */
@Component
public class SalesForceOneConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalesForceOneConnection.class);

    private EnterpriseConnection connection;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    @Value("${salesforce.url}")
    private String url;

    private synchronized void init() {
        try {
            if (connection == null) {
                LOGGER.debug("Attempting to connect to SalesForce");
                ConnectorConfig config = new ConnectorConfig();
                config.setUsername(username);
                config.setPassword(password);
                config.setAuthEndpoint(url);
                connection = new EnterpriseConnection(config);
            }
        } catch (ConnectionException e) {
            LOGGER.error("Error connecting to SalesForce", e);
        }
    }

    /**
     * Retrieves the SF connection
     *
     * @return EnterpriseConnection to use
     */
    public EnterpriseConnection get() {
        init();
        return connection;
    }

    public void reUp() {
        connection = null;
    }
}
