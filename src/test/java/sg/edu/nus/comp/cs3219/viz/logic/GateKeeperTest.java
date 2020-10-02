package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.VizApplication;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationAccessControlRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VizApplication.class)
@AutoConfigureMockMvc
class GateKeeperTest {

    @Autowired
    private static PresentationAccessControlRepository pacr;

    private static GateKeeper gateKeeper = new GateKeeper(pacr);
}