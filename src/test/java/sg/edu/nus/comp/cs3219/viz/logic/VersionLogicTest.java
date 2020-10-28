package sg.edu.nus.comp.cs3219.viz.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;
import sg.edu.nus.comp.cs3219.viz.test.BaseComponentTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link VersionLogic}
 */
@DataJpaTest
class VersionLogicTest extends BaseComponentTest {

    @Autowired
    private VersionRepository repo;

    private final VersionLogic logic = new VersionLogic(repo);

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