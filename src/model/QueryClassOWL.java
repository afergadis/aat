package model;

import java.util.ArrayList;
import java.util.List;

import controller.QueryClass;

/**
 * Η κλάση υλοποιεί την αναζήτηση κλάσης σε μία οντολολογία OWL.
 * 
 * @author team1
 * 
 */
public class QueryClassOWL implements QueryClass {
    String       objectClass; // Καθορίζει τον τύπο της κλάσης αναζήτησης, π.χ.
                              // Altar
    List<String> results;

    /**
     * Αρχικοποιεί την κλάση με τον τύπο των αντικειμένων (κλάση) για τα οποία
     * αναζητεί στην οντολογία.
     * 
     * @param objectClass
     *            ο τύπος της κλάσης της οντολογίας, π.χ. Altar
     */
    public QueryClassOWL(String objectClass) {
        super();
        this.objectClass = objectClass;
        results = new ArrayList<>();
        results.add("altar-of-aphrodite-ourania");
    }

    /*
     * (non-Javadoc)
     * 
     * @see controller.QueryClass#getResults()
     */
    @Override
    public List<String> getResults() {
        return results;
    }

}
