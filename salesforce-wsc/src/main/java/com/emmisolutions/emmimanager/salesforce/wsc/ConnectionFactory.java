package com.emmisolutions.emmimanager.salesforce.wsc;

import com.sforce.soap.partner.PartnerConnection;
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
public class ConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);
    private static final String ESCAPE_CHARS = "?&|!{}[]()^~*:\\'+-";
    private PartnerConnection connection;

    @Value("${salesforce.username}")
    private String username;

    @Value("${salesforce.password}")
    private String password;

    @Value("${salesforce.url}")
    private String url;

    public static String escape(String searchQuery) {
        StringBuilder ret = new StringBuilder();
        for (Character character : searchQuery.toCharArray()) {
            if (ESCAPE_CHARS.contains(character.toString())) {
                ret.append('\\').append(character);
            } else {
                ret.append(character);
            }
        }
        return ret.toString();
    }

    private synchronized void init() {
        try {
            if (connection == null) {
                LOGGER.debug("Attempting to connect to SalesForce");
                ConnectorConfig config = new ConnectorConfig();
                config.setUsername(username);
                config.setPassword(password);
                config.setAuthEndpoint(url);
                connection = new PartnerConnection(config);
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
    public PartnerConnection get() {
        init();
        return connection;
    }

    public void reUp() {
        connection = null;
    }
}
