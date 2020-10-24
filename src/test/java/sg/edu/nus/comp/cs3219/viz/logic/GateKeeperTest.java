package sg.edu.nus.comp.cs3219.viz.logic;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
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
    public void login() {
        String user = "user@test.com";
        assertEquals(Optional.empty(), gateKeeper.getCurrentLoginUser());
        gaeSimulation.loginUser(user);
        assertTrue(gateKeeper.getCurrentLoginUser().isPresent());
        assertEquals(user, gateKeeper.getCurrentLoginUser().get().getUserEmail());
    }
}
