package controller;

import java.util.List;

/**
 * Αναζήτηση σε δομή (ΒΔ, RDF, αρχείο, ...) για μια συγκεκριμένη κατηγορία.
 * 
 * @author team1
 * 
 */
public interface QueryClass {

    /**
     * Η συνάρτηση αυτή χρησιμοποιείται για να επιστρέψει μία λίστα με όλα τα
     * αντικείμενα μιας συγκεκριμένης κατηγορίας. Παράδειγμα κατηγοριών είναι:
     * <ul>
     * <li>Altar</li>
     * <li>Temple</li>
     * <li>Statue, κ.α.</li>
     * </ul>
     * 
     * @return λίστα με String
     */
    public abstract List<String> getResults();

}