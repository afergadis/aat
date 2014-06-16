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
	private final static String boldText = "\033[0;1m";
	private final static String plainText = "\033[0;0m";

	public static void main(String[] args) {
		String owlPath = System.getProperty("user.dir") + "/data/OwlTemp.owl";
		String NLResourcePath = System.getProperty("user.dir") + "/data";
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
			System.out.println("Bye");
			System.exit(0);
		}

		ut = profiles.get(choice);

		switch (ut.toLowerCase()) {
		case "child":
			try {
				System.out.println(readFile("/data/child_intro.txt",
						StandardCharsets.UTF_8));
			} catch (IOException e1) {
				System.out.println("Could not read file /data/child_intro.txt");
				System.exit(1);
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
				System.out.println("Could not read file /data/adult_intro.txt");
				System.exit(1);
			}
			// Διάβασε την ξενάγηση
			qe = new QueryClassOWL("adult");
			choice = readExhibit(sc, "\t", qe.getObjects(), qe.getStart()
					.getDescr(), null);
			if (choice == -1) {
				System.out.println("Bye");
				System.exit(0);
			}
			break;
		case "expert":
			try {
				System.out.println(readFile("/data/expert_intro.txt",
						StandardCharsets.UTF_8));
			} catch (IOException e1) {
				System.out
						.println("Could not read file /data/expert_intro.txt");
				System.exit(1);
			}
			// Διάβασε την ξενάγηση
			qe = new QueryClassOWL("expert");
			int i = 0;
			for (String s : qe.getDescriptions()) {
				System.out.printf("%2d: %s", i + 1, s);
			}

			break;
		default:
			System.out.println("An unkown profile selected!");
			System.exit(1);
			break;
		}

		// Δημιουργία του log file
		String userID = Integer.toString((int) Math.random() * 1000);
		h = new HistoryFile(userID, ut);

		System.out.println("Please wait...");
		AgoraObject exhibit = qe.getExhibit(choice);
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
		while (choice != -1) {
			String objectURI = "http://localhost/OwlTemp.owl#" + exhibit.getName();
			boolean GenerateComparisons = false;
			int depth = 2;

			// generate a new text ...
			String result[] = myEngine.GenerateDescription(0,
			// 0 means we are describing an instance; 1 is for classes.
					objectURI, // The URI of the instance or class to
					// describe.
					UT, // String specifying the user type.
					userID, // String specifying the userID of the user.
					depth, // The depth in the RDF graph of the ontology we
							// are allowed to go in content selection when
							// describing instances. A depth of 1 will
							// produce a text conveying only properties of
							// the instance being described. Larger depth
							// values will produce texts conveying also
							// properties of other related instances (e.g.,
							// "This lekythos was created in the classical
							// period. The classical period was..".
					-1, // Maximum number of facts per sentence (how long
						// an aggregated sentence can be); only used when
						// userType is null.
					GenerateComparisons, // A Boolean specifying whether or
					// not we want comparisons
					""); // this is usefull only the communicates with Pers
							// Server if not use "" as default value

			// System.out.println("---------------------------------------------------");
			// the result array consists of 3 strings
			// the first string contains the produced text along with
			// the outputs of the intermediate stages of the NLG engine
			// System.out.println(result[0]);

			// System.out.println("---------------------------------------------------");
			// the second string contains only the produced text
			System.out.println(result[1] + "\n");

			// System.out.println("---------------------------------------------------");
			// also the engine produces semantic-linguistic annotations
			// of the produced text ...
			// System.out.println(result[2]);

			switch (ut.toLowerCase()) {
			case "child":

				qe = new QueryClassOWL("child");
				System.out.println("Start with:");
				System.out.println(qe.getStart().getDescr());
				break;
			case "adult":
				choice = readExhibit(sc, "\t", qe.getObjects(),
						exhibit.getSuggestions());
				if (choice == -1) {
					System.out.println("Bye");
					System.exit(0);
				}
				break;
			case "expert":
				try {
					System.out.println(readFile("/data/expert_intro.txt",
							StandardCharsets.UTF_8));
				} catch (IOException e1) {
					System.out
							.println("Could not read file /data/expert_intro.txt");
					System.exit(1);
				}
				// Διάβασε την ξενάγηση
				qe = new QueryClassOWL("expert");
				int i = 0;
				for (String s : qe.getDescriptions()) {
					System.out.printf("%2d: %s", i + 1, s);
				}

				break;
			default:
				System.out.println("An unkown profile selected!");
				System.exit(1);
				break;
			}
			// TODO Καταχώρηση εκθέματος στο History
			exhibit = qe.getExhibit(choice);
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
				System.out.printf("%s%2d: *%s*\n",  prompt, i, o.getDescr());
			else
				System.out.printf("%s%2d: %s\n", prompt, i, o.getDescr());
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
			for (String s : suggestions) { // Δες αν το ο είναι στη λίστα suggestions
				if (o.getDescr().equals(s)) {
					System.out.printf("%s%2d: *%s*\n", prompt, i, o.getDescr());
					suggested = true; // Είναι, το τυπώσαμε
					break;
				}
			}
			if (!suggested) // Αν δεν έχει τυπωθεί, τύπωσέ το
				System.out.printf("%s%2d: %s\n", prompt, i, o.getDescr());
			i++;
			suggested = false; // reset
		}

		return readChoice(sc,
				"Choose an Exhibit Number (suggestions with stars)", 0, i - 1);
	}
}
