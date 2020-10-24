package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link Version}
 */
class VersionTest {

    private Version.VersionPk testPk;
    private final String dataSet = "data_set";
    private final String recordType = "record_type";
    private final String versionId = "version_id";

    @BeforeEach
    void setUp() {
        testPk = new Version.VersionPk(dataSet, recordType, versionId);
    }

    @Test
    void getId() {
        Version test = new Version(testPk);
        assertEquals(testPk, test.getId());
    }

    @Test
    void setId() {
        Version test = new Version(null);
        assertNull(test.getId());
        test.setId(testPk);
        assertEquals(testPk, test.getId());
    }

    @Test
    void versionPk_getRecordType() {
        assertEquals(recordType, testPk.getRecordType());
    }

    @Test
    void versionPk_setRecordType() {
        String newType = "new_type";
        testPk.setRecordType(newType);
        assertEquals(newType, testPk.getRecordType());
    }

    @Test
    void versionPk_getVersionId() {
        assertEquals(versionId, testPk.getVersionId());
    }

    @Test
    void versionPk_setVersionId() {
        String newId = "new_id";
        testPk.setVersionId(newId);
        assertEquals(newId, testPk.getVersionId());
    }

    @Test
    void versionPk_getDataSet() {
        assertEquals(dataSet, testPk.getDataSet());
    }

    @Test
    void versionPk_setDataSet() {
        String newDataset = "new_dataset";
        testPk.setDataSet(newDataset);
        assertEquals(newDataset, testPk.getDataSet());
    }

    @Test
    void versionPk_equals() {
        Version.VersionPk newPk = new Version.VersionPk(dataSet, recordType, versionId);
        assertEquals(testPk, newPk);
        Version.VersionPk falsePk = new Version.VersionPk("1", "2", "3");
        assertNotEquals(testPk, falsePk);
    }

    @Test
    void versionPk_hashCode() {
        Version.VersionPk newPk = new Version.VersionPk(dataSet, recordType, versionId);
        assertEquals(testPk.hashCode(), newPk.hashCode());
        Version.VersionPk falsePk = new Version.VersionPk("1", "2", "3");
        assertNotEquals(testPk.hashCode(), falsePk.hashCode());
    }
}