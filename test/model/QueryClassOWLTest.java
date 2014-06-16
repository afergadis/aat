package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import controller.QueryClass;

public class QueryClassOWLTest {

    private final QueryClass qc = new QueryClassOWL("Altar");

    @Test
    public void testGetResults() {
	String results[] = { "altar-of-aphrodite-ourania" };
	// Test number of query results
	assertEquals(1, qc.getDescriptions().size());
	// Test query results
	for (int i = 0; i < qc.getDescriptions().size(); i++) {
	    assertEquals(results[i], qc.getDescriptions().get(i));
	}
    }

}
