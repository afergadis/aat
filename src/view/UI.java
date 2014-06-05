package view;

import gr.aueb.cs.nlg.Languages.Languages;
import gr.aueb.cs.nlg.NLGEngine.NLGEngine;

import java.util.List;
import java.util.Scanner;

import model.AMObject;
import model.HistoryFile;
import model.QueryClassOWL;
import model.QueryProfilesOWL;
import controller.History;
import controller.NLG;
import controller.QueryClass;
import controller.QueryProfiles;

public class UI {
    static QueryProfiles qp = new QueryProfilesOWL();
    static QueryClass qe = new QueryClassOWL("");
    static History h;
    static AMObject amo;
    static NLG e;

    public static void main(String[] args) {
	String owlPath = System.getProperty("user.dir") + "/data/OwlTemp.owl";
	String NLResourcePath = System.getProperty("user.dir") + "/data";
	String up; // User Profile
	Scanner sc = new Scanner(System.in);
	List<String> profiles = qp.getProfiles();

	for (String p : profiles) {
	    System.out.println(p);
	}
	System.out.println("Επιλογή προφίλ:");
	sc.nextLine(); // up = sc.nextLine();
	up = "Adult";

	switch (up) {
	case "Child":
	    System.out.println("Εισαγωγικό κείμενο για το προφίλ «Παιδί».");
	    System.out.println("Εκθέματα εκίνησης:");
	    System.out.println("\tΈκθεμα 1");
	    System.out.println("\tΈκθεμα 2");
	    break;
	case "Adult":
	    System.out.println("Εισαγωγικό κείμενο για το προφίλ «Ενήλικας».");
	    System.out.println("Εκθέματα εκίνησης:");
	    System.out.println("\tΈκθεμα 1");
	    System.out.println("\tΈκθεμα 2");
	    break;
	case "Expert":
	    System.out.println("Εισαγωγικό κείμενο για το προφίλ «Ειδικός».");
	    System.out.println("Εκθέματα εκίνησης:");
	    System.out.println("\tΈκθεμα 1");
	    System.out.println("\tΈκθεμα 2");
	    break;
	default:
	    System.out.println("Εισαγωγικό κείμενο.");
	    System.out.println("Εκθέματα εκίνησης:");
	    System.out.println("\tΈκθεμα 1");
	    System.out.println("\tΈκθεμα 2");
	    break;
	}
	System.out.println("\tΈξοδος");
	String choise = sc.nextLine();

	// Δημιουργία του log file
	String userID = Integer.toString((int) Math.random() * 1000000000);
	h = new HistoryFile(userID, up);

	while (!choise.equals("Έξοδος")) {
	    System.out.println("Παρακαλώ περιμένετε...");
	    String exhibit = qe.getResults().get(0);
	    String UT = "http://www.aueb.gr/users/ion/owlnl/UserModelling#"
		    + up;
	    String objectURI = "http://localhost/OwlTemp.owl#" + exhibit;

	    // e = new NLG(up, userID);
	    try {
		NLGEngine myEngine = new NLGEngine(owlPath, NLResourcePath,
			Languages.ENGLISH, // Set
					   // language
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

		boolean GenerateComparisons = false;
		int depth = 2;

		// create a new user in PServer
		myEngine.getUMVisit().newUser(userID, UT);
		// boolean userExists =
		// myEngine.getUMVisit().checkUserExists(userID);

		// generate a new text ...
		String result[] = myEngine.GenerateDescription(0,
		// 0 means we are describing aninstance; 1 is for classes.
			objectURI, // The URI of the instance or class to
				   // describe.
			UT, // String specifying the user type.
			userID, // String specifying the userID of the user.
			depth, // The depth in the RDF graph of the ontology we
			       // are allowed to go in content selection when
			       // describing instances. A depth of 1 will
			       // produce
			       // a text conveying only properties of the
			       // instance
			       // // being described. Larger depth values will
			       // produce texts conveying also properties of
			       // other related instances (e.g., �This lekythos
			       // was created in the classical period. The
			       // classical period was��).
			-1, // Maximum number of facts per sentence (how long
			    // an aggregated sentence can be); only used when
			    // userType is null.
			GenerateComparisons, // A Boolean specifying whether or
			// not
			// we want comparisons
			""); // this is usefull only the communicates
			     // with Pers. Server
			     // if not use "" as default value

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

		// Δώσε τα στοιχεία στην κλάση AMObject η οποία θα εξάγει τα
		// ενδιαφέροντα χαρακτηριστικά.
		amo = new AMObject(exhibit, result[0]);
		// Προσθήκη στο ιστορικό
		// TODO να εισάγεται η τιμή για τον αν ήταν πρόταση του
		// συστήματος ή επιλογή του χρήστη
		String record = exhibit + ", 1";
		h.addRecord(exhibit);
		// Τα παρακάτω στοιχεία θα προκύπτουν απ' την κλάση amo
		// TODO ανάγνωση από την κλάση amo
		System.out.println("Ενδιαφέροντα εκθέματα:");
		System.out.println("\tΈκθεμα 1"); // amo.getSugestions()
		System.out.println("\tΈκθεμα 2");
		System.out.println("\tΣύνδεσμοι"); // amo.getLinks()
		System.out.println("\tΙστορικό");
		System.out.println("\tΈξοδος");
		choise = sc.nextLine();
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
	sc.close();
	System.out.println("Bye");
    }
}
