package protocol;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;

/**
 * Author: Ashish Gogna
 */

public class Registery {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Blockchain.class);

    /** Public functions */
    public Registery() {
    }

    public void addNode(String endpoint) {
        try {
            DataStore.addNode(endpoint);
        } catch (Exception e) {
            LOGGER.error("Couldn't add node in registry: ", e);
        }
    }

    public void addAuth(String publicKey) {
        try {
            DataStore.addAuth(publicKey);
        } catch (Exception e) {
            LOGGER.error("Couldn't add Auth in registry: ", e);
        }
    }

    public void addLwc(String publicKey) {
        try {
            DataStore.addLWC(publicKey);
        } catch (Exception e) {
            LOGGER.error("Couldn't add LWC in registry: ", e);
        }
    }

    public void remNode(String endpoint) {
        try {
            DataStore.removeNode(endpoint);
        } catch (Exception e) {
            LOGGER.error("Couldn't remove node from registry: ", e);
        }
    }

    public void remAuth(String publicKey) {
        try {
            DataStore.removeAuth(publicKey);
        } catch (Exception e) {
            LOGGER.error("Couldn't remove Auth from registry: ", e);
        }
    }

    public void remLwc(String publicKey) {
        try {
            DataStore.removeLWC(publicKey);
        } catch (Exception e) {
            LOGGER.error("Couldn't reove LWC from registry: ", e);
        }
    }
}
