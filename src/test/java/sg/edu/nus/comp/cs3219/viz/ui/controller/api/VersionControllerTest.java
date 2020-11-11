package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import sg.edu.nus.comp.cs3219.viz.test.BaseComponentTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link VersionController}
 */
@WebMvcTest
@AutoConfigureMockMvc
class VersionControllerTest extends BaseComponentTest {

    @Autowired
    private VersionController controller;

    @BeforeEach
    void setUp() {
    }

    @Test
    void all() {
    }
}