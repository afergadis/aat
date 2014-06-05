package test;

import static org.junit.Assert.assertEquals;
import model.QueryClassOWL;

import org.junit.Test;

import controller.QueryClass;

public class QueryExhibitsTest {

    @Test
    public void testGetExhibits() {
        QueryClass qe = new QueryClassOWL("");
        String exhibits[] = { "altar-of-aphrodite-ourania" };
        // Test number of query results
        assertEquals(1, qe.getResults().size());
        // Test query results
        for (int i = 0; i < qe.getResults().size(); i++) {
            assertEquals(exhibits[i], qe.getResults().get(i));
        }
    }

}
