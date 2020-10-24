package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT {@link SubmissionRecord}
 */
class SubmissionRecordTest {

    private SubmissionRecord record;

    @BeforeEach
    void setUp() {
        record = new SubmissionRecord();
    }

    @Test
    void get_set_Version() {
        Version version = new Version(new Version.VersionPk("data_set", "author_record", "version_id"));
        assertNull(record.getVersion());
        record.setVersion(version);
        assertEquals(version, record.getVersion());
    }

    @Test
    void get_set_Id() {
        assertNull(record.getId());
        record.setId(1L);
        assertEquals(new Long(1), record.getId());
    }

    @Test
    void get_set_SubmissionId() {
        String test = "test";
        assertNull(record.getSubmissionId());
        record.setSubmissionId(test);
        assertEquals(test, record.getSubmissionId());
    }

    @Test
    void get_set_TrackId() {
        String test = "test";
        assertNull(record.getTrackId());
        record.setTrackId(test);
        assertEquals(test, record.getTrackId());
    }

    @Test
    void get_set_TrackName() {
        String test = "test";
        assertNull(record.getTrackName());
        record.setTrackName(test);
        assertEquals(test, record.getTrackName());
    }

    @Test
    void get_set_Title() {
        String test = "test";
        assertNull(record.getTitle());
        record.setTitle(test);
        assertEquals(test, record.getTitle());
    }

    @Test
    void get_set_Authors() {
        List<String> test = Collections.singletonList("test");
        assertNull(record.getAuthors());
        record.setAuthors(test);
        assertEquals(test, record.getAuthors());
    }

    @Test
    void get_set_AuthorSet() {
        List<SubmissionAuthorRecord> test = Collections.singletonList(new SubmissionAuthorRecord());
        assertNull(record.getAuthorSet());
        record.setAuthorSet(test);
        assertEquals(test, record.getAuthorSet());
    }

    @Test
    void get_set_SubmissionTime() {
        Date test = new Date();
        assertNull(record.getSubmissionTime());
        record.setSubmissionTime(test);
        assertEquals(test, record.getSubmissionTime());
    }

    @Test
    void get_set_LastUpdatedTime() {
        Date test = new Date();
        assertNull(record.getLastUpdatedTime());
        record.setLastUpdatedTime(test);
        assertEquals(test, record.getLastUpdatedTime());
    }

    @Test
    void get_set_Keywords() {
        String test = "test";
        assertNull(record.getKeywords());
        record.setKeywords(test);
        assertEquals(test, record.getKeywords());
    }

    @Test
    void get_set_IsAccepted() {
        String test = "test";
        assertNull(record.getIsAccepted());
        record.setIsAccepted(test);
        assertEquals(test, record.getIsAccepted());
    }

    @Test
    void get_set_IsNotified() {
        String test = "test";
        assertNull(record.getIsNotified());
        record.setIsNotified(test);
        assertEquals(test, record.getIsNotified());
    }

    @Test
    void get_set_IsReviewsSent() {
        String test = "test";
        assertNull(record.getIsReviewsSent());
        record.setIsReviewsSent(test);
        assertEquals(test, record.getIsReviewsSent());
    }

    @Test
    void get_set_SubmissionAbstract() {
        String test = "test";
        assertNull(record.getSubmissionAbstract());
        record.setSubmissionAbstract(test);
        assertEquals(test, record.getSubmissionAbstract());
    }
}