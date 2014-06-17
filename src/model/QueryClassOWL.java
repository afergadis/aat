package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import controller.QueryClass;

/**
 * Η κλάση διαβάζει την προτεινόμενη διαδρομή για ένα προφίλ.
 * Το κάθε βήμα της διαδρομής αποθηκεύεται σε ένα αντικείμενο τύπου AgoraObject
 * το οποίο έχει πληροφορίες όπως οι προτεινόμενοι σύνδεσμοι και τα εκθέματα.
 * 
 * @author team1
 * 
 */
public class QueryClassOWL implements QueryClass {
	String userProfile; // Καθορίζει το προφίλ του χρήστη για το οποίο θα
						// διαβάσει την ξενάγηση
	private List<AgoraObject> results;

	/**
	 * Αρχικοποιεί την κλάση με την προτεινόμενη διαδρομή για ένα συγκεκριμένο προφίλ
	 * 
	 * @param userProfile
	 *            το προφίλ για το οποίο θα φτιάξει τη ξενάγηση
	 */
	public QueryClassOWL(String userProfile) {
		super();
		this.userProfile = userProfile;
		results = new ArrayList<>();
		Path filename = Paths.get(System.getProperty("user.dir"), "data", userProfile + "_tour.csv");
		try (BufferedReader br = Files.newBufferedReader(filename, StandardCharsets.UTF_8)) {
			String line = br.readLine(); // Αυτό είναι το header. Δε μας ενδιαφέρει.
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",", 7);
				boolean start = Boolean.parseBoolean(data[0]);
				AgoraObject TourStep = new AgoraObject(start, data[1], data[2], data[3], data[4], data[5], data[6]);
				results.add(TourStep);
			}
			
			br.close();
		} catch (IOException e) {
			System.out.println("Could not read \"" + filename + "\"");
			System.exit(1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.QueryClass#getDescriptions()
	 */
	@Override
	public List<String> getDescriptions() {
		List<String> descriptions = new ArrayList<>();
		for (AgoraObject o: results) {
			descriptions.add(o.getDescr());
		}
		
		return descriptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.QueryClass#getExhibit(String)
	 */
	@Override
	public AgoraObject getExhibit(String name) {
		for (AgoraObject o: results) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.QueryClass#getExhibit(int)
	 */
	@Override
	public AgoraObject getExhibit(int n) {
		return results.get(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.QueryClass#getStart()
	 */
	@Override
	public AgoraObject getStart() {
		for (AgoraObject o: results) {
			if (o.isStart())
				return o;
		}
		
		return null;
	}

	@Override
	public List<AgoraObject> getObjects() {
		return results;
	}

}
