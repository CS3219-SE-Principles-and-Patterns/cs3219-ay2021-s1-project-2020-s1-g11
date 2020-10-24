package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link AuthorRecord}
 */
class AuthorRecordTest {

    private AuthorRecord record;

    @BeforeEach
    void setUp() {
        record = new AuthorRecord();
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
    void get_set_FirstName() {
        String test = "test";
        assertNull(record.getFirstName());
        record.setFirstName(test);
        assertEquals(test, record.getFirstName());
    }

    @Test
    void get_set_LastName() {
        String test = "test";
        assertNull(record.getLastName());
        record.setLastName(test);
        assertEquals(test, record.getLastName());
    }

    @Test
    void get_set_Email() {
        String test = "test";
        assertNull(record.getEmail());
        record.setEmail(test);
        assertEquals(test, record.getEmail());
    }

    @Test
    void get_set_Country() {
        String test = "test";
        assertNull(record.getCountry());
        record.setFirstName(test);
        assertEquals(test, record.getFirstName());
    }

    @Test
    void get_set_Organisation() {
        String test = "test";
        assertNull(record.getOrganisation());
        record.setOrganisation(test);
        assertEquals(test, record.getOrganisation());
    }

    @Test
    void get_set_WebPage() {
        String test = "test";
        assertNull(record.getWebPage());
        record.setWebPage(test);
        assertEquals(test, record.getWebPage());
    }

    @Test
    void get_set_PersonId() {
        String test = "test";
        assertNull(record.getPersonId());
        record.setPersonId(test);
        assertEquals(test, record.getPersonId());
    }

    @Test
    void get_set_IsCorresponding() {
        String test = "test";
        assertNull(record.getIsCorresponding());
        record.setIsCorresponding(test);
        assertEquals(test, record.getIsCorresponding());
    }
}
