package model;

import static org.junit.Assert.assertEquals;
import model.QueryProfilesOWL;

import org.junit.Test;

import controller.QueryProfiles;

public class QueryProfilesTest {

    @Test
    public void testGetProfiles() {
	QueryProfiles qp = new QueryProfilesOWL();
	String profiles[] = { "child", "adult", "expert" };
	// Test number of query results
	assertEquals(3, qp.getProfiles().size());
	// Test query results
	for (int i = 0; i < qp.getProfiles().size(); i++) {
	    assertEquals(profiles[i], qp.getProfiles().get(i));
	}
    }

}
