package model;


/**
 * Κρατάει τα στοιχεία ενός εκθέματος και δεδομένα σχετικά με την περιγραφή και 
 * την πρόταση για την επόμενη επίσκεψη του χρήστη.
 * 
 */
public class TourStep {
	private final boolean start;
	private final String entity;
	private final String descr;
	private final String suggestion;

	/**
	 * Κρατάει τα στοιχεία ενός εκθέματος και δεδομένα σχετικά με την περιγραφή και 
	 * την πρόταση για την επόμενη επίσκεψη του χρήστη.
	 * 
	 * @param start
	 *            δηλώνει ότι είναι το εναρκτήριο έκθεμα
	 * @param name
	 *            το όνομα του αντικειμένου
	 * @param descr
	 *            περιγραφή
	 * @param next
	 *            πρόταση για επόμενο έκθεμα
	 */
	public TourStep(boolean start, String name, String descr, String next) {
		this.start = start;
		this.entity = name;
		this.descr = descr;
		suggestion = next;
	}

	/**
	 * 
	 * @return επιστρέφει το επόμενο προτεινόμενο έκθεμα
	 */
	public String getSuggestion() {
		return suggestion;
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
