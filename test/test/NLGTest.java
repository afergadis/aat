package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

import controller.NLG;

public class NLGTest {

    String userType = "Adult";
    String userID = "1";
    NLG e = new NLG();

    @Test
    public void testGetText() {
	String text[] = e.getText("Adult", "1", "altar-of-aphrodite-ourania",
		false, 1);
	assertFalse(text[1].equals(""));
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
