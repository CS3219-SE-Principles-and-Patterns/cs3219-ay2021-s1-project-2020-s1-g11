package sg.edu.nus.comp.cs3219.viz.common.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link Presentation}
 */
class  PresentationTest {

    private Presentation presentation;

    @BeforeEach
    void setUp() {
        presentation = new Presentation();
    }

    @Test
    void get_set_Id() {
        Long id = 1L;
        assertNull(presentation.getId());
        presentation.setId(id);
        assertEquals(id, presentation.getId());
    }

    @Test
    void get_set_Name() {
        String test = "test";
        assertNull(presentation.getName());
        presentation.setName(test);
        assertEquals(test, presentation.getName());
    }

    @Test
    void get_set_Version() {
        String test = "test";
        assertNull(presentation.getVersion());
        presentation.setVersion(test);
        assertEquals(test, presentation.getVersion());
    }

    @Test
    void get_set_Description() {
        String test = "test";
        assertNull(presentation.getDescription());
        presentation.setDescription(test);
        assertEquals(test, presentation.getDescription());
    }

    @Test
    void getCreatorIdentifier() {
        String test = "test";
        assertNull(presentation.getCreatorIdentifier());
        presentation.setCreatorIdentifier(test);
        assertEquals(test, presentation.getCreatorIdentifier());
    }
}