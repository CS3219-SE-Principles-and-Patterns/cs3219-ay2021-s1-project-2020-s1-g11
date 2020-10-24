package sg.edu.nus.comp.cs3219.viz.logic;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.exception.UnauthorisedException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;
import sg.edu.nus.comp.cs3219.viz.test.BaseComponentTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link GateKeeper}
 */
@SpringBootTest
class GateKeeperTest extends BaseComponentTest {

    @Autowired
    private static PresentationAccessControlRepository repo;

    private static final GateKeeper gateKeeper = new GateKeeper(repo);
    private final JSONObject data = loadDataBundle("GateKeeperTest.json");

    @Test
    public void login_logout() {
        gaeSimulation.logoutUser();
        assertEquals(Optional.empty(), gateKeeper.getCurrentLoginUser());
        String user = "user@test.com";
        gaeSimulation.loginUser(user);
        assertTrue(gateKeeper.getCurrentLoginUser().isPresent());
        assertEquals(user, gateKeeper.getCurrentLoginUser().get().getUserEmail());
        assertEquals(user, gateKeeper.verifyLoginAccess().getUserEmail());
    }

    @Test
    public void testGetLoginUrl() {
        gaeSimulation.logoutUser();
        assertEquals("/_ah/login?continue=www.abc.com", gateKeeper.getLoginUrl("www.abc.com"));
    }

    @Test
    public void testGetLogoutUrl() {
        gaeSimulation.loginUser("any.user");
        assertEquals("/_ah/logout?continue=www.def.com", gateKeeper.getLogoutUrl("www.def.com"));
    }

    @Test
    public void testVerifyDeletionAccessForPresentation() {
        assertThrows(UnauthorisedException.class, () -> gateKeeper.verifyDeletionAccessForPresentation(null));
        Presentation presentation = null;
        try {
            presentation = new Gson().fromJson(
                    data.getJSONObject("presentations").getJSONObject("presentationA").toString(), Presentation.class
            );
        } catch (JSONException e) {
            fail(e);
        }
        if (presentation == null) {
            fail("Null Presentation");
        }

        gaeSimulation.logoutUser();
        Presentation finalPresentation = presentation;
        assertThrows(UnauthorisedException.class,
                () -> gateKeeper.verifyDeletionAccessForPresentation(finalPresentation));

        String user = "user@test.com";
        gaeSimulation.loginUser(user);
        assertThrows(UnauthorisedException.class,
                () -> gateKeeper.verifyDeletionAccessForPresentation(finalPresentation));

        user = "test@viz.test";
        gaeSimulation.loginUser(user);
        gateKeeper.verifyDeletionAccessForPresentation(finalPresentation);
    }

    @Test
    public void testVerifyAccessForPresentation() {
        assertThrows(UnauthorisedException.class, () -> gateKeeper.verifyAccessForPresentation(null, null));
        Presentation presentation = null;
        try {
            presentation = new Gson().fromJson(
                    data.getJSONObject("presentations").getJSONObject("presentationA").toString(), Presentation.class
            );
        } catch (JSONException e) {
            fail(e);
        }
        if (presentation == null) {
            fail("Null Presentation");
        }
        Presentation finalPresentation = presentation;
        String user = "test@viz.test";
        gaeSimulation.loginUser(user);
        // TODO: Find out how to mock repo, currently throws NPE
        // gateKeeper.verifyAccessForPresentation(finalPresentation, AccessLevel.CAN_WRITE);
    }
}
