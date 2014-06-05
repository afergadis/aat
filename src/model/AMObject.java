package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Διαβάζει τα στοιχεία ενός αντικειμένου και εξάγει δεδομένα σχετικά με τους
 * προτεινόμενους συνδέσμους για επιπλέον πληροφορίες και για τις προτάσεις για
 * την επόμενη επίσκεψη του χρήστη.
 * 
 */
public class AMObject {
    private final List<String> links;
    private final List<String> suggestions;
    private final String name;
    private final String xml;
    private int interest;

    /**
     * Δημιουργεί ένα αντικείμενο της αρχαίας αγοράς με το όνομα και το xml
     * αρχείο που το περιγράφει έτσι όπως έχει παραχθεί από το NaturalOWL. Στη
     * συνέχεια εξάγει πληροφορίες χρήσιμες στο σύστημα, όπως οι σχετικοί
     * σύνδεσμοι με το αντικείμενο και τις προτάσεις του συστήματος.
     * 
     * @param name
     *            το όνομα του αντικειμένου
     * @param xml
     *            η δομή που παρήγαγε το NaturalOWL για το αντικείμενο
     */
    public AMObject(String name, String xml) {
	links = new ArrayList<>();
	suggestions = new ArrayList<>();
	this.name = name;
	this.xml = xml;
	// TODO parse xml
	suggestions.add("Exhibit 1");
	suggestions.add("Exhibit 2");
	links.add("Link 1");
	links.add("Link 2");
    }

    /**
     * Δίνει σύνδεσμους σχετικούς με το αντικείμενο.
     * 
     * @return λίστα με τα ονόματα που αντιστοιχούν σε αντικείμενα της δομής
     */
    public List<String> getLinks() {
	return links;
    }

    /**
     * Δίνει σύνδεσμους προτάσεις για το επόμενο έκθεμα.
     * 
     * @return λίστα με τα ονόματα που αντιστοιχούν σε αντικείμενα της δομής
     */
    public List<String> getSuggestions() {
	return suggestions;
    }

    /**
     * 
     * @return το όνομα του αντικειμένου
     */
    public String getName() {
	return name;
    }
}
