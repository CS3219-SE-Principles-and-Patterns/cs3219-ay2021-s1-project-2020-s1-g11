package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link SubmissionAuthorRecord}
 */
class SubmissionAuthorRecordTest {

    private SubmissionAuthorRecord record;

    @BeforeEach
    void setUp() {
        record = new SubmissionAuthorRecord();
    }

    @Test
    void get_set_Id() {
        assertNull(record.getId());
        record.setId(1L);
        assertEquals(new Long(1), record.getId());
    }

    @Test
    void get_set_DataSet() {
        String test = "test";
        assertNull(record.getDataSet());
        record.setDataSet(test);
        assertEquals(test, record.getDataSet());
    }

    @Test
    void get_set_Name() {
        String test = "test";
        assertNull(record.getName());
        record.setName(test);
        assertEquals(test, record.getName());
    }
}