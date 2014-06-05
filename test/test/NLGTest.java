package test;

import static org.junit.Assert.fail;

import org.junit.Test;

import controller.NLG;

public class NLGTest {

    private final String owlPath = System.getProperty("user.dir")
	    + "/data/OwlTemp.owl";
    private final String NLResourcePath = System.getProperty("user.dir")
	    + "/data";
    String userType = "Adult";
    String userID = "1";
    NLG e = new NLG(userType, userID);

    @Test
    public void testNLG() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetText() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetNLGSteps() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetSLA() {
	fail("Not yet implemented");
    }

}
