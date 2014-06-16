package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Κρατάει τα στοιχεία ενός αντικειμένου και εξάγει δεδομένα σχετικά με τους
 * προτεινόμενους συνδέσμους για επιπλέον πληροφορίες και για τις προτάσεις για
 * την επόμενη επίσκεψη του χρήστη.
 * 
 */
public class AgoraObject {
	private final boolean start;
	private final String entity;
	private final String descr;
	private final List<String> links;
	private final List<String> suggestions;

	/**
	 * Για ένα αντικείμενο της αρχαίας αγοράς κρατάει πληροφορίες σχετικές με
	 * τους προτεινόμενους συνδέσμους και τα προτεινόμενα επόμενα αντικείμενα.
	 * 
	 * @param start
	 *            δηλώνει ότι είναι το εναρκτήριο αντικείμενο
	 * @param name
	 *            το όνομα του αντικειμένου
	 * @param descr
	 *            περιγραφή
	 * @param s1
	 *            πρώτη πρόταση για επόμενο αντικείμενο
	 * @param s2
	 *            δεύτερη πρόταση για επόμενο αντικείμενο
	 * @param l1
	 *            πρώτος σύνδεσμος για επιπλέον πληροφορίες
	 * @param l2
	 *            δεύτερος σύνδεσμος για επιπλέον πληροφορίες
	 */
	public AgoraObject(boolean start, String name, String descr, String s1,
			String s2, String l1, String l2) {
		this.start = start;
		this.entity = name;
		this.descr = descr;
		links = new ArrayList<>();
		suggestions = new ArrayList<>();

		suggestions.add(s1);
		suggestions.add(s2);
		links.add(l1);
		links.add(l2);
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
		return entity;
	}

	public String getDescr() {
		return descr;
	}

	public boolean isStart() {
		return start;
	}
}
