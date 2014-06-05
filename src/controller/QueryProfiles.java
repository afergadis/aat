package controller;

import java.util.List;

/**
 * Αναζήτηση σε δομή (ΒΔ, RDF, αρχείο, ...) για τα προφίλ χρηστών.
 * 
 * @author team1
 * 
 */
public interface QueryProfiles {

    /**
     * Η συνάρτηση επιστρέφει τα διαθέσιμα προφίλ χρηστών από τη δομή δεδομένων.
     * 
     * @return λίστα με String
     */
    public abstract List<String> getProfiles();

}