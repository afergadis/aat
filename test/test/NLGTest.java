package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import gr.demokritos.iit.PServer.UMException;

import org.junit.Test;

import controller.NLG;

public class NLGTest {

	String userType = "Adult";
	String userID = "1";
	NLG e;

	@Test
	public void testGetText() {
		try {
			e = new NLG("Adult", "0");
			String text[] = e.getText("altar-of-aphrodite-ourania",
					false, 1);
			assertFalse(text[1].equals(""));
		} catch (UMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
