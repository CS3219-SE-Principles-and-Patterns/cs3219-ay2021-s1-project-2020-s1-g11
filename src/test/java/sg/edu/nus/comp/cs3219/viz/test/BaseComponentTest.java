package sg.edu.nus.comp.cs3219.viz.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class BaseComponentTest {
    protected static final GaeSimulation gaeSimulation = GaeSimulation.inst();

    protected JSONObject loadDataBundle(String jsonFileName) {
        try {
            String pathToJsonFile = getTestDataFolder() + jsonFileName;
            String jsonString = FileHelper.readFile(pathToJsonFile);
            return new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTestDataFolder() {
        return "src/test/resources/data/";
    }

    @BeforeAll
    public static void setUpGae() {
        gaeSimulation.setUp();
    }

    @AfterAll
    public static void tearDownGae() {
        gaeSimulation.tearDown();
    }
}
