package sg.edu.nus.comp.cs3219.viz.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.test.BaseComponentTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link VersionLogic}
 */
@DataJpaTest
@AutoConfigureDataJpa
class VersionLogicTest extends BaseComponentTest {

    @Autowired
    private VersionLogic logic;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllForUser() {
    }

    @Test
    void findAllForUserWithRecordType() {
    }

    @Test
    void saveForUser() {
        /*
        String email = "viz@gmail.com";
        UserInfo user = new UserInfo();
        user.setUserEmail(email);
        gaeSimulation.loginUser(email);
        logic.saveForUser(new Version(new Version.VersionPk("test", "test", "test")), "test");
        assertEquals(1, logic.findAllForUser(user).size());
         */
    }

    @Test
    void testSaveForUser() {
    }

    @Test
    void editVersionId() {
    }

    @Test
    void deleteVersion() {
    }
}