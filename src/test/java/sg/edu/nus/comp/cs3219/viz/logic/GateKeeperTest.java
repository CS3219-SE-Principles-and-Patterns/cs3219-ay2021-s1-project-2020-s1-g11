package sg.edu.nus.comp.cs3219.viz.logic;

import com.google.appengine.repackaged.com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationAccessControl;
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
    private PresentationAccessControlRepository repo;

    private GateKeeper gateKeeper = new GateKeeper(repo);
    private JSONObject data = loadDataBundle("GateKeeperTest.json");

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
        Presentation presentation = getPresentation("presentationA");
        if (presentation == null) {
            fail("Null Presentation");
        }

        gaeSimulation.logoutUser();
        assertThrows(UnauthorisedException.class,
                () -> gateKeeper.verifyDeletionAccessForPresentation(presentation));

        String user = "user@test.com";
        gaeSimulation.loginUser(user);
        assertThrows(UnauthorisedException.class,
                () -> gateKeeper.verifyDeletionAccessForPresentation(presentation));

        user = "test@viz.test";
        gaeSimulation.loginUser(user);
        gateKeeper.verifyDeletionAccessForPresentation(presentation);
    }

    @Test
    public void testVerifyAccessForPresentation() {
        assertThrows(UnauthorisedException.class, () -> gateKeeper.verifyAccessForPresentation(null, null));
        Presentation presentation = getPresentation("presentationA");
        if (presentation == null) {
            fail("Null Presentation");
        }
        String user = "test@viz.test";
        gaeSimulation.loginUser(user);
        // TODO: Repo autowiring doesn't seem to work (always null)
        // setUpPresentationAccessControl();
        // gateKeeper.verifyAccessForPresentation(presentation, AccessLevel.CAN_WRITE);
    }

    private void setUpPresentationAccessControl() {
        try {
            for (int i = 1; i <= 4; i++) {
                repo.save(new Gson().fromJson(
                        data.getJSONObject("presentationAccessControls").getJSONObject("AC" + i).toString(),
                        PresentationAccessControl.class)
                );
            }
        } catch (JSONException e) {
            fail(e);
        }
    }

    private Presentation getPresentation(String option) {
        Presentation presentation = null;
        try {
            presentation = new Gson().fromJson(
                    data.getJSONObject("presentations").getJSONObject(option).toString(), Presentation.class
            );
        } catch (JSONException e) {
            fail(e);
        }
        return presentation;
    }
}
