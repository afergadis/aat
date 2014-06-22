package view;

import gr.aueb.cs.nlg.Languages.Languages;
import gr.aueb.cs.nlg.NLGEngine.NLGEngine;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import model.HistoryFile;
import model.QueryProfilesOWL;
import model.TourStep;
import model.UserTourFile;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import controller.History;
import controller.QueryProfiles;
import controller.UserTour;

public class Aggregator {
	static QueryProfiles qp;
	static UserTour ut = null;
	static History h;
	static TourStep ts;
	static NLGEngine e;
	// http://stackoverflow.com/questions/5062458/font-settings-for-strings-in-java
	private final static String BOLD = "\033[0;1m";
	private final static String PLAIN = "\033[0;0m";
	private final static String STRIKE = "\033[0;9m";

	public static void main(String[] args) {
		Logger.getRootLogger().setLevel(Level.OFF);
		// Clear screen
		System.out.print("\033[H\033[2J");
		System.out.flush();

		String owlPath = "OwlTemp.owl";
		String NLResourcePath = "data";
		String up; // User Profile
		qp = new QueryProfilesOWL();
		List<String> profiles = qp.getProfiles();
		Scanner sc = new Scanner(System.in);

		// Εμφάνιση της εισαγωγής
		try {
			System.out.println(readFile("/data/intro.txt",
					StandardCharsets.UTF_8));
		} catch (IOException e2) {
			exit("File \"./data/intro.txt\" not found.", 1);
		}

		// Εμφάνιση των διαθέσιμων προφίλ
		for (int i = 0; i < profiles.size(); i++) {
			// i+1 για να μην εμφανίζουμε το μηδέν
			String profile = (i + 1) + ": " + profiles.get(i);
			System.out.println(profile);
		}
		int choice = readChoice(sc, "Choose Profile Number", 0, profiles.size());
		if (choice == -1) {
			exit("Bye", 0);
		}

		up = profiles.get(choice).toLowerCase();
		String filename = "/data/" + up + "_intro.txt";
		String legend = "Our suggestions are in " + BOLD + "bold" + PLAIN
				+ " and viewed items are " + STRIKE + "striked out" + PLAIN
				+ ".\n";

		switch (up) {
		case "basic":
			// Εμφάνιση του εισαγωγικού κειμένου για το προφίλ
			try {
				System.out.println(readFile(filename, StandardCharsets.UTF_8));
			} catch (IOException e1) {
				exit("Could not read file " + filename, 1);
			}
			System.out.println(legend);
			// Διάβασε την ξενάγηση
			ut = new UserTourFile(up);
			System.out.println("Start by viewing:");
			choice = readExhibit(sc, "\t", ut.getObjects(), ut.getStart()
					.getDescr(), null);
			if (choice == -1) {
				exit("Bye", 0);
			}
			break;
		case "advanced":
			try {
				System.out.println(readFile(filename, StandardCharsets.UTF_8));
			} catch (IOException e1) {
				exit("Could not read file " + filename, 1);
			}
			System.out.println(legend);
			ut = new UserTourFile(up);
			System.out.println("Start by viewing (suggestion in bold):");
			choice = readExhibit(sc, "\t", ut.getObjects(), ut.getStart()
					.getDescr(), null);
			if (choice == -1) {
				exit("Bye", 0);
			}
			break;
		default:
			System.out.println("An unknown profile selected!");
			System.exit(1);
		}

		// Δημιουργία του log file
		BigInteger rnd = new BigInteger(30, new Random(
				System.currentTimeMillis()));
		String userID = rnd.toString();
		h = new HistoryFile(up, userID);

		// Ctrl+C
		Runtime.getRuntime().addShutdownHook(new ShutDown());

		// Εκκίνηση του NLGEngine
		System.out.println("Please wait...");
		String UT = "http://www.aueb.gr/users/ion/owlnl/UserModelling#"
				+ up.toLowerCase();

		startNLG(owlPath, NLResourcePath, userID, UT);

		// Καταχώρηση της 1ης επιλογής στο log
		TourStep exhibit = ut.getExhibit(choice);
		if (exhibit == ut.getStart()) {
			h.addRecord(exhibit.getName(), 1);
		} else {
			h.addRecord(exhibit.getName(), 0);
		}

		// Loop ξενάγησης
		while (choice != -1) {
			String objectURI = "http://localhost/OwlTemp.owl#"
					+ exhibit.getName();
			boolean GenerateComparisons = false;
			int depth = 1;

			// Εμφάνιση κειμένου
			System.out.println(exhibit.getDescr() + "\n");
			String result[] = e.GenerateDescription(0, objectURI, UT, userID,
					depth, -1, GenerateComparisons, "");
			System.out.println(result[1] + "\n");

			// Εμφάνιση των επιλογών και ανάγνωση της επιλογής του χρήστη
			choice = readExhibit(sc, "\t", ut.getObjects(),
					exhibit.getSuggestions(), h.getHistory());
			if (choice == -1) {
				exit("Bye", 0);
			}

			// Καταχώρησε τις προτάσεις του προηγούμενου εκθέματος
			List<String> suggestions = exhibit.getSuggestions();

			// Βρες το νέο έκθεμα που διάλεξε ο χρήστης
			exhibit = ut.getExhibit(choice);

			// Καταχώρηση της επιλογής στο log file
			boolean suggested = false;
			for (String s : suggestions) {
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

	private static void startNLG(String owlPath, String NLResourcePath,
			String userID, String UT) {
		try {
			e = new NLGEngine(owlPath, NLResourcePath, Languages.ENGLISH, true,
					false, null, null, null, null, "", -1, "", "", "", -1);

			// initialize PServer
			e.initPServer();

			// initialize the statistical tree used
			// in the generation of comparisons
			e.initStatisticalTree();

			// create a new user in PServer
			e.getUMVisit().newUser(userID, UT);
		} catch (Exception ex) {
			System.out.println("Could not start NLGEngine.");
			ex.printStackTrace();
		}
	}

	// Διαβάζει ένα αρχείο και το επιστρέφει ως String
	private static String readFile(String path, Charset encoding)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(System
				.getProperty("user.dir") + path));
		return new String(encoded, encoding);
	}

	private static int readChoice(Scanner sc, String prompt, int low, int high) {
		System.out.println(PLAIN + "\n0: Exit");
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
			List<TourStep> exhibits, String s1, String s2) {
		int i = 1;
		for (TourStep o : exhibits) {
			if (o.getDescr().equals(s1) || o.getDescr().equals(s2))
				System.out.printf("%s%s%2d: %s\n", BOLD, prompt, i,
						o.getDescr());
			else
				System.out.printf("%s%s%2d: %s\n", PLAIN, prompt, i,
						o.getDescr());
			i++;
		}

		return readChoice(sc,
				"Choose a Number (suggestions in bold)", 0, i - 1);
	}

	private static int readExhibit(Scanner sc, String prompt,
			List<TourStep> exhibits, List<String> suggestions,
			Set<String> history) {
		int i = 1;
		boolean suggested = false; // Αν έχει εκτυπωθεί ως πρόταση
		for (TourStep o : exhibits) {
			for (String s : history) {
				// Δες αν το αντικείμενο είναι στη λίστα ιστορικού
				if (o.getName().equals(s)) {
					System.out.printf("%s%s%2d: %s\n", STRIKE, prompt, i,
							o.getDescr());
					suggested = true; // Είναι, το τυπώσαμε
					break;
				}
			}
			if (!suggested) {
				for (String s : suggestions) {
					// Δες αν το αντικείμενο είναι στη λίστα suggestions
					if (o.getName().equals(s)) {
						System.out.printf("%s%s%2d: %s\n", BOLD, prompt, i,
								o.getDescr());
						suggested = true; // Είναι, το τυπώσαμε
						break;
					}
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
		if (h != null) {
			h.commit();
		}
		System.out.println(msg);
		System.exit(value);
	}
	
	static class ShutDown extends Thread {
		@Override
		public void run() {
			h.commit();
			System.out.println("Bye");
		}
	}
}
