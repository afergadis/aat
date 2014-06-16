package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AMObjectTest {
    private final AgoraObject amo = new AgoraObject("altar-of-aphrodite-ourania", "");

    @Test
    public void testGetLinks() {
	String links[] = { "Link 1", "Link 2" };
	// Test query results
	for (int i = 0; i < amo.getLinks().size(); i++) {
	    assertEquals(links[i], amo.getLinks().get(i));
	}
    }

    @Test
    public void testGetSuggestions() {
	String s[] = { "Exhibit 1", "Exhibit 2" };
	// Test number of query results
	assertEquals(2, amo.getSuggestions().size());
	// Test query results
	for (int i = 0; i < amo.getLinks().size(); i++) {
	    assertEquals(s[i], amo.getSuggestions().get(i));
	}
    }

    @Test
    public void testGetName() {
	assertEquals("altar-of-aphrodite-ourania", amo.getName());
    }

}
