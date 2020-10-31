package sg.edu.nus.comp.cs3219.viz.common.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SUT: {@link PresentationSection}
 */
class PresentationSectionTest {

    private PresentationSection entity;

    @BeforeEach
    void setUp() {
        entity = new PresentationSection();
    }

    @Test
    void get_set_Id() {
        Long id = 1L;
        assertNull(entity.getId());
        entity.setId(id);
        assertEquals(id, entity.getId());
    }

    @Test
    void get_set_Presentation() {
        Presentation test = new Presentation();
        assertNull(entity.getPresentation());
        entity.setPresentation(test);
        assertEquals(test, entity.getPresentation());
    }

    @Test
    void get_set_Title() {
        String test = "test";
        assertNull(entity.getTitle());
        entity.setTitle(test);
        assertEquals(test, entity.getTitle());
    }

    @Test
    void get_set_Description() {
        String test = "test";
        assertNull(entity.getDescription());
        entity.setDescription(test);
        assertEquals(test, entity.getDescription());
    }

    @Test
    void get_set_Type() {
        String test = "test";
        assertNull(entity.getType());
        entity.setType(test);
        assertEquals(test, entity.getType());
    }

    @Test
    void get_set_DataSet() {
        String test = "test";
        assertNull(entity.getDataSet());
        entity.setDataSet(test);
        assertEquals(test, entity.getDataSet());
    }

    @Test
    void getSelections() {
    }

    @Test
    void setSelections() {
    }

    @Test
    void getInvolvedRecords() {
    }

    @Test
    void setInvolvedRecords() {
    }

    @Test
    void getFilters() {
    }

    @Test
    void setFilters() {
    }

    @Test
    void getJoiners() {
    }

    @Test
    void setJoiners() {
    }

    @Test
    void getGroupers() {
    }

    @Test
    void setGroupers() {
    }

    @Test
    void getSorters() {
    }

    @Test
    void setSorters() {
    }

    @Test
    void getExtraData() {
    }

    @Test
    void setExtraData() {
    }
}