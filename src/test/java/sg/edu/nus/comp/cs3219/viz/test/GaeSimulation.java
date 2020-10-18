package sg.edu.nus.comp.cs3219.viz.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.appengine.tools.development.testing.LocalLogServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalModulesServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.logic.GateKeeper;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;

/**
 * Provides a Singleton in-memory simulation of the GAE for unit testing.
 *
 * <p>This is not the same as testing against the dev server.
 * When testing against the GAE simulation, there is no need for the dev server to be running.
 */
@SpringBootTest
public class GaeSimulation {

    // This can be any valid URL; it is not used beyond validation
    private static final String SIMULATION_BASE_URL = "http://localhost:8080";

    @Autowired
    private static PresentationAccessControlRepository pacr;

    private static GateKeeper gateKeeper = new GateKeeper(pacr);
    private static GaeSimulation instance = new GaeSimulation();

    private LocalServiceTestHelper helper;

    /**
     * Gets the GAE simulation instance.
     */
    public static GaeSimulation inst() {
        return instance;
    }

    /**
     * Sets up the GAE simulation.
     */
    public void setUp() {
        synchronized (this) {
            System.out.println("Setting up GAE simulation");

            LocalUserServiceTestConfig localUserServices = new LocalUserServiceTestConfig();
            LocalModulesServiceTestConfig localModules = new LocalModulesServiceTestConfig();
            LocalLogServiceTestConfig localLog = new LocalLogServiceTestConfig();
            helper = new LocalServiceTestHelper(localUserServices, localModules, localLog);

            helper.setEnvAttributes(getEnvironmentAttributesWithApplicationHostname());
            helper.setUp();
        }
    }

    private Optional<UserInfo> loginUser(String userId, boolean isAdmin) {
        helper.setEnvIsLoggedIn(true);
        helper.setEnvEmail(userId);
        helper.setEnvAuthDomain("gmail.com");
        helper.setEnvIsAdmin(isAdmin);
        return gateKeeper.getCurrentLoginUser();
    }

    /**
     * Logs in the user to the GAE simulation environment without admin rights.
     *
     * @return The user info after login process
     */
    public Optional<UserInfo> loginUser(String userId) {
        return loginUser(userId, false);
    }

    /**
     * Logs the current user out of the GAE simulation environment.
     */
    public void logoutUser() {
        helper.setEnvIsLoggedIn(false);
    }

    /**
     * Tears down the GAE simulation.
     */
    public void tearDown() {
        try {
            if (helper != null) {
                helper.tearDown();
            }
        } catch (Exception e) {
            System.out.println("Ignoring exception during teardown...");
        }
    }

    /**
     * Returns an environment attribute with application host name.
     */
    public static Map<String, Object> getEnvironmentAttributesWithApplicationHostname() {
        Map<String, Object> attributes = new HashMap<>();
        try {
            attributes.put("com.google.appengine.runtime.default_version_hostname",
                    new URL(SIMULATION_BASE_URL).getAuthority());
            attributes.put("com.google.appengine.runtime.request_log_id", "samplerequestid123");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return attributes;
    }
}
