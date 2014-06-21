package model;

import org.junit.Test;

import controller.QueryProfiles;

public class QueryProfilesTest {

	@Test
	public void testGetProfiles() {
		QueryProfiles qp = new QueryProfilesOWL();

		assert (qp.getProfiles().size() > 0);
	}

}
