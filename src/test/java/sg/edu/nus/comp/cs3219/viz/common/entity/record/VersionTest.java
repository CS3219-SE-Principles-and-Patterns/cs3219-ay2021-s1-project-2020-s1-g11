package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void getId() {
        Version.VersionPk testPk = new Version.VersionPk("data_set", "record_type", "version_id");
        Version test = new Version(testPk);
        assertEquals(testPk, test.getId());
    }

    @Test
    void setId() {
        Version.VersionPk testPk = new Version.VersionPk("data_set", "record_type", "version_id");
        Version test = new Version(null);
        test.setId(testPk);
        assertEquals(testPk, test.getId());
    }
}