package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT {@link ReviewRecord}
 */
class ReviewRecordTest {

    private ReviewRecord record;

    @BeforeEach
    void setUp() {
        record = new ReviewRecord();
    }

    @Test
    void get_set_Version() {
        Version version = new Version(new Version.VersionPk("data_set", "review_record", "version_id"));
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
    void get_set_ReviewId() {
        String test = "test";
        assertNull(record.getReviewId());
        record.setReviewId(test);
        assertEquals(test, record.getReviewId());
    }

    @Test
    void get_set_NumReviewAssignment() {
        assertEquals(0, record.getNumReviewAssignment());
        record.setNumReviewAssignment(10);
        assertEquals(10, record.getNumReviewAssignment());
    }

    @Test
    void get_set_ReviewerName() {
        String test = "test";
        assertNull(record.getReviewerName());
        record.setReviewerName(test);
        assertEquals(test, record.getReviewerName());
    }

    @Test
    void get_set_ExpertiseLevel() {
        assertEquals(0.0, record.getExpertiseLevel());
        record.setExpertiseLevel(10.5);
        assertEquals(10.5, record.getExpertiseLevel());
    }

    @Test
    void get_set_ConfidenceLevel() {
        assertEquals(0.0, record.getConfidenceLevel());
        record.setConfidenceLevel(10.5);
        assertEquals(10.5, record.getConfidenceLevel());
    }

    @Test
    void get_set_ReviewComment() {
        String test = "test";
        assertNull(record.getReviewComment());
        record.setReviewComment(test);
        assertEquals(test, record.getReviewComment());
    }

    @Test
    void get_set_OverallEvaluationScore() {
        assertEquals(0.0, record.getOverallEvaluationScore());
        record.setOverallEvaluationScore(10.5);
        assertEquals(10.5, record.getOverallEvaluationScore());
    }

    @Test
    void get_set_ReviewSubmissionTime() {
        Date test = new Date();
        assertNull(record.getReviewSubmissionTime());
        record.setReviewSubmissionTime(test);
        assertEquals(test, record.getReviewSubmissionTime());
    }

    @Test
    void get_set_HasRecommendedForBestPaper() {
        String test = "test";
        assertNull(record.getHasRecommendedForBestPaper());
        record.setHasRecommendedForBestPaper(test);
        assertEquals(test, record.getHasRecommendedForBestPaper());
    }
}