package view;

import gr.aueb.cs.nlg.Languages.Languages;
import gr.aueb.cs.nlg.NLGEngine.NLGEngine;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import model.AgoraObject;
import model.HistoryFile;
import model.QueryClassOWL;
import model.QueryProfilesOWL;
import controller.History;
import controller.NLG;
import controller.QueryClass;
import controller.QueryProfiles;

public class Aggregator {
	static QueryProfiles qp;
	static QueryClass qe = null;
	static History h;
	static AgoraObject amo;
	static NLG e;
	static NLGEngine myEngine;
	// http://stackoverflow.com/questions/5062458/font-settings-for-strings-in-java
	private final static String BOLD = "\033[0;1m";
	private final static String PLAIN = "\033[0;0m";
	private final static String DIM = "\033[0;2m";
	private final static String REVERSE = "\033[0;7m";
	private final static String STRIKE = "\033[0;9m";

	public static void main(String[] args) {
		String owlPath = "OwlTemp.owl";
		String NLResourcePath = "data";
		String ut; // User Type (profile)
		qp = new QueryProfilesOWL();
		List<String> profiles = qp.getProfiles();
		Scanner sc = new Scanner(System.in);

		for (int i = 0; i < profiles.size(); i++) {
			// i+1 για να μην εμφανίζουμε το μηδέν
			String profile = (i + 1) + ": " + profiles.get(i);
			System.out.println(profile);
		}
		int choice = readChoice(sc, "Choose Profile Number", 0, profiles.size());
		if (choice == -1) {
			exit("Bye", 0);
		}

		ut = profiles.get(choice);

		switch (ut.toLowerCase()) {
		case "child":
			try {
				System.out.println(readFile("/data/child_intro.txt",
						StandardCharsets.UTF_8));
			} catch (IOException e1) {
				exit("Could not read file /data/child_intro.txt", 1);
			}
			// Διάβασε την ξενάγηση
			qe = new QueryClassOWL("child");
			System.out.println("Start with:");
			System.out.println(qe.getStart().getDescr());
			break;
		case "adult":
			try {
				System.out.println(readFile("/data/adult_intro.txt",
						StandardCharsets.UTF_8));
			} catch (IOException e1) {
				exit("Could not read file /data/adult_intro.txt", 1);
			}
			// Διάβασε την ξενάγηση
			qe = new QueryClassOWL("adult");
			choice = readExhibit(sc, "\t", qe.getObjects(), qe.getStart()
					.getDescr(), null);
			if (choice == -1) {
				exit("Bye", 0);
			}
			break;
		case "expert":
			try {
				System.out.println(readFile("/data/expert_intro.txt",
						StandardCharsets.UTF_8));
			} catch (IOException e1) {
				exit("Could not read file /data/expert_intro.txt", 1);
			}
			// Διάβασε την ξενάγηση
			qe = new QueryClassOWL("expert");
			int i = 0;
			for (String s : qe.getDescriptions()) {
				System.out.printf("%2d: %s", i + 1, s);
			}

			break;
		default:
			exit("An unknown profile selected!", 1);
			break;
		}

		// Δημιουργία του log file
		String userID = Integer.toString((int) Math.random() * 1000);
		h = new HistoryFile(ut, userID);
		
		// Σε περίπτωση κλεισίματος με Ctrl+C γράψε τα δεδομένα του ιστορικού στο αρχείο
		Thread shutDown = new Thread() {
            @Override
            public void run() {
                h.commit();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutDown);

		System.out.println("Please wait...");
		String UT = "http://www.aueb.gr/users/ion/owlnl/UserModelling#"
				+ ut.toLowerCase();

		try {
			myEngine = new NLGEngine(owlPath, NLResourcePath,
					Languages.ENGLISH, // Set language
					true, // Use NaturalOWL's emulator of the Pers.
							// Server. A false value would signal that
							// the real Pers. server is to be used
					false, // load the databases of PServer
					null, // Objects representing the lexicon, user
							// modelling, microplans, and
					null, // ontology. If already available, they
					null, // may be passed to the engine;
					null, // otherwise null values are passed.
					"", // navigation server IP
					-1, // navigation server port (-1
						// means to use the default port
						// 53000)
					"", // PServer's database username
					"", // PServer's database password
					"", // PServer's IP
					-1); // PServer's port

			// initialize PServer
			myEngine.initPServer();

			// initialize the statistical tree used
			// in the generation of comparisons
			myEngine.initStatisticalTree();

			// create a new user in PServer
			myEngine.getUMVisit().newUser(userID, UT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Καταχώρηση της 1ης επιλογής στο log
		AgoraObject exhibit = qe.getExhibit(choice);
		if (exhibit == qe.getStart()) {
			h.addRecord(exhibit.getName(), 1);
		} else {
			h.addRecord(exhibit.getName(), 0);
		}
		// Loop ξενάγησης
		while (choice != -1) {
			String objectURI = "http://localhost/OwlTemp.owl#"
					+ exhibit.getName();
			boolean GenerateComparisons = false;
			int depth = 2;

			// generate a new text ...
			String result[] = myEngine.GenerateDescription(0, objectURI, UT,
					userID, depth, -1, GenerateComparisons, "");

			System.out.println(result[1]);

			switch (ut.toLowerCase()) {
			case "child":
				break;
			case "adult":
				choice = readExhibit(sc, "\t", qe.getObjects(),
						exhibit.getSuggestions());
				if (choice == -1) {
					exit("Bye", 0);
				}
				break;
			case "expert":
				break;
			default:
				exit("An unknown profile selected!", 1);
			}
			// Καταχώρησε τις προτάσεις του προηγούμενου εκθέματος
			List<String> suggestions = exhibit.getSuggestions();
			
			// Βρες το νέο έκθεμα που διάλεξε ο χρήστης
			exhibit = qe.getExhibit(choice);
			
			// Καταχώρηση της επιλογής στο log file
			boolean suggested = false;
			for (String s: suggestions) {
				// Αν το όνομα του επιλεγμένου ήταν στις προτάσεις
				if (exhibit.getName().equals(s)) {
					h.addRecord(exhibit.getName(), 1);
					suggested = true;
					break;
				}
			}
			if (!suggested) {
				h.addRecord(exhibit.getName(), 0);
			}
				
			
		}
		sc.close();
		System.out.println("Bye");
	}

	// Διαβάζει ένα αρχείο και το επιστρέφει ως String
	private static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(System
				.getProperty("user.dir") + path));
		return new String(encoded, encoding);
	}

	private static int readChoice(Scanner sc, String prompt, int low, int high) {
		System.out.println("\n0: Exit");
		// Scanner sc = new Scanner(System.in);
		String choice;
		int c; // The choice number

		while (true) {
			System.out.println(prompt + " [" + (low + 1) + ".." + high + "]:");
			choice = sc.nextLine();
			c = Integer.valueOf(choice);
			if (c >= low && c <= high) {
				break;
			}
		}
		// sc.close();
		return c - 1;
	}

	private static int readExhibit(Scanner sc, String prompt,
			List<AgoraObject> exhibits, String s1, String s2) {
		int i = 1;
		for (AgoraObject o : exhibits) {
			if (o.getDescr().equals(s1) || o.getDescr().equals(s2))
				System.out.printf("%s%s%2d: %s\n", BOLD, prompt, i,
						o.getDescr());
			else
				System.out.printf("%s%s%2d: %s\n", PLAIN, prompt, i,
						o.getDescr());
			i++;
		}

		return readChoice(sc,
				"Choose an Exhibit Number (suggestions with stars)", 0, i - 1);
	}

	// TODO: Χρειάζεται και το history για να μην εμφανίζει όσα επισκέφτηκε
	private static int readExhibit(Scanner sc, String prompt,
			List<AgoraObject> exhibits, List<String> suggestions) {
		int i = 1;
		boolean suggested = false; // Αν έχει εκτυπωθεί ως πρόταση
		for (AgoraObject o : exhibits) {
			for (String s : suggestions) { // Δες αν το ο είναι στη λίστα
											// suggestions
				if (o.getDescr().equals(s)) {
					System.out.printf("%s%s%2d: %s\n", BOLD, prompt, i,
							o.getDescr());
					suggested = true; // Είναι, το τυπώσαμε
					break;
				}
			}
			if (!suggested) // Αν δεν έχει τυπωθεί, τύπωσέ το
				System.out.printf("%s%s%2d: %s\n", PLAIN, prompt, i,
						o.getDescr());
			i++;
			suggested = false; // reset
		}

		return readChoice(sc,
				"Choose an Exhibit Number (suggestions with stars)", 0, i - 1);
	}
	
	private static void exit(String msg, int value) {
		// Αποθήκευση του ιστορικού
		h.commit();
		System.out.println(msg);
		System.exit(value);
	}
}
