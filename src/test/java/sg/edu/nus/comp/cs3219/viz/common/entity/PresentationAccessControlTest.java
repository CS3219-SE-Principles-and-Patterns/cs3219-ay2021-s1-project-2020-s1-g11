package sg.edu.nus.comp.cs3219.viz.common.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AccessLevel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link PresentationAccessControl}
 */
class PresentationAccessControlTest {

    private PresentationAccessControl entity;

    @BeforeEach
    void setUp() {
        entity = new PresentationAccessControl();
    }

    @Test
    void get_set_Presentation() {
        Presentation test = new Presentation();
        assertNull(entity.getPresentation());
        entity.setPresentation(test);
        assertEquals(test, entity.getPresentation());
    }

    @Test
    void get_set_UserIdentifier() {
        String test = "test";
        assertNull(entity.getUserIdentifier());
        entity.setUserIdentifier(test);
        assertEquals(test, entity.getUserIdentifier());
    }

    @Test
    void get_set_AccessLevel() {
        AccessLevel test = AccessLevel.CAN_WRITE;
        assertNull(entity.getAccessLevel());
        entity.setAccessLevel(test);
        assertEquals(test, entity.getAccessLevel());
    }
}