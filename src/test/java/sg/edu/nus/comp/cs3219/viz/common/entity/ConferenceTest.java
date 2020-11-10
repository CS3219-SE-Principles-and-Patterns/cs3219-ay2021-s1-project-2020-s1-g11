package sg.edu.nus.comp.cs3219.viz.common.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link Conference}
 */
class ConferenceTest {

    private Conference conference;

    @BeforeEach
    void setUp() {
        conference = new Conference();
    }

    @Test
    void get_set_Id() {
        Long id = 1L;
        assertNull(conference.getId());
        conference.setId(id);
        assertEquals(id, conference.getId());
    }

    @Test
    void get_set_Name() {
        String name = "test";
        assertNull(conference.getName());
        conference.setName(name);
        assertEquals(name, conference.getName());
    }

    @Test
    void get_set_Description() {
        String test = "test";
        assertNull(conference.getDescription());
        conference.setDescription(test);
        assertEquals(test, conference.getDescription());
    }

    @Test
    void get_set_Date() {
        Date test = new Date();
        assertNull(conference.getDate());
        conference.setDate(test);
        assertEquals(test, conference.getDate());
    }

    @Test
    void getCreatorIdentifier() {
        String test = "test";
        assertNull(conference.getCreatorIdentifier());
        conference.setCreatorIdentifier(test);
        assertEquals(test, conference.getCreatorIdentifier());
    }
}