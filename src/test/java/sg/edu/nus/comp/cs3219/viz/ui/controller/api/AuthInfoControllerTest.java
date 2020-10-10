package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.comp.cs3219.viz.VizApplication;
import sg.edu.nus.comp.cs3219.viz.test.BaseTestCase;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VizApplication.class)
@AutoConfigureMockMvc
class AuthInfoControllerTest extends BaseTestCase {

    @Test
    void getAuthInfo() {
        assertTrue(true);
    }
}
