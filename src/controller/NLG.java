package controller;

import gr.aueb.cs.nlg.Languages.Languages;
import gr.aueb.cs.nlg.NLGEngine.NLGEngine;
import gr.demokritos.iit.PServer.UMException;

public class NLG {
	private final String owlPath = System.getProperty("user.dir")
			+ "/data/OwlTemp.owl";
	private final String NLResourcePath = System.getProperty("user.dir")
			+ "/data";
	private String objectURI;
	private String UT;
	private String UserID;
	private NLGEngine engine;

	public NLG(String userType, String userID) throws UMException {
		System.out.println(owlPath);
		System.out.println(NLResourcePath);
		UT = "http://www.aueb.gr/users/ion/owlnl/UserModelling#" + userType;
		UserID = userID;

		engine = new NLGEngine(owlPath, NLResourcePath, Languages.ENGLISH,
				true, // Use NaturalOWL's emulator of the Pers Server.
						// A false value would signal that the real Pers. server
						// is to be used
				false, // load the databases of PServer
				null, // Objects representing the lexicon, user modelling,
						// microplans, and
				null, // ontology. If already available, they
				null, // may be passed to the engine;
				null, // otherwise null values are passed.
				"", // navigation server IP
				-1, // navigation server port (-1 means to use the default port
					// 53000)
				"", // PServer's database username
				"", // PServer's database password
				"", // PServer's IP
				-1); // PServer's port

		// initialize PServer
		engine.initPServer();

		// initialize the statistical tree used
		// in the generation of comparisons
		engine.initStatisticalTree();

		// create a new user in PServer
		engine.getUMVisit().newUser(userID, userType);

	}

	/**
	 * 
	 * @return the produced text
	 */
	public String[] getText(String id, boolean GenerateComparisons, int depth) {
		objectURI = "http://localhost/OwlTemp.owl#" + id;

		// Generate a new text ...
		String result[] = engine.GenerateDescription(0, // 0 means we are
														// describing
														// aninstance; 1 is for
														// classes.
				objectURI, // The URI of the instance or class to describe.
				UT, // String specifying the user type.
				UserID, // String specifying the userID of the user.
				depth, // The depth in the RDF graph of the ontology we
						// are allowed to go in content selection when
						// describing instances.
				-1, // Maximum number of facts per sentence (how long
					// an aggregated sentence can be); only used when
					// userType is null.
				GenerateComparisons,
				// A Boolean specifying whether or not we want comparisons
				""// this is usefull only the communicates with Pers. Server if
					// not use "" as default value
		);

		return result;
	}
}
