package sg.edu.nus.comp.cs3219.viz.logic;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.VizApplication;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;
import sg.edu.nus.comp.cs3219.viz.test.BaseComponentTest;

/**
 * SUT: {@link GateKeeper}
 */
@SpringBootTest(classes = VizApplication.class)
@AutoConfigureMockMvc
class GateKeeperTest extends BaseComponentTest {

    @Autowired
    private static PresentationAccessControlRepository repo;

    private static final GateKeeper gateKeeper = new GateKeeper(repo);
    private final JSONObject data = loadDataBundle("GateKeeperTest.json");

    @BeforeEach
    public void setUp() {
        gaeSimulation.loginUser("user@test.com");
    }
}
