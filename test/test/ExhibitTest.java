package test;

import static org.junit.Assert.assertEquals;
import model.AgoraObject;

import org.junit.Test;

public class ExhibitTest {

    @Test
    public void test() {
	String id = "altar-of-aphrodite-ourania";
	// Test constructor
	AgoraObject e = new AgoraObject(id, "");
	assertEquals(e.getName(), id);
    }

}
