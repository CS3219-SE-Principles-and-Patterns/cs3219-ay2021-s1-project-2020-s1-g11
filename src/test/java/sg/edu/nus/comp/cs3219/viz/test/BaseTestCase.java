package sg.edu.nus.comp.cs3219.viz.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseTestCase {
    protected static final GaeSimulation gaeSimulation = GaeSimulation.inst();

    @BeforeClass
    public static void setUpGae() {
        gaeSimulation.setUp();
    }

    @AfterClass
    public static void tearDownGae() {
        gaeSimulation.tearDown();
    }
}
