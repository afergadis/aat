package test;

import model.AMObject;

import org.junit.Test;

public class ExhibitTest {

    @Test
    public void test() {
        String id = "altar-of-aphrodite-ourania";
        // Test constructor
        AMObject e = new AMObject(id, "");
        equals(id.equals(e.getName()));
    }

}
