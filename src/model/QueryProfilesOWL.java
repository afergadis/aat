package model;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

import controller.QueryProfiles;

/**
 * Επιλογή από την οντολογία των διαθέσιμων προφίλ
 * 
 */
public class QueryProfilesOWL implements QueryProfiles {
	List<String> profiles = new ArrayList<>();

	public QueryProfilesOWL() {
		OntModel model = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		try {
			File file = new File("data/UserModelling.rdf");
			FileReader reader = new FileReader(file);
			model.read(reader, null);

			String sql = ""
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
					+ "PREFIX owlnl: <http://www.aueb.gr/users/ion/owlnl#>\n"
					+ "SELECT ?user WHERE\n"
					+ "{ ?user rdf:type owlnl:UserType; }\n";

			Query query = QueryFactory.create(sql);
			QueryExecution qe = QueryExecutionFactory.create(query, model);
			ResultSet results = qe.execSelect();
			List<String> vars = results.getResultVars();

			while (results.hasNext()) {
				QuerySolution qs = results.nextSolution();
				for (int i = 0; i < vars.size(); i++) {
					String var = vars.get(i).toString();
					RDFNode node = qs.get(var);
					profiles.add(toTitleCase(node.toString().split("#")[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.QueryProfiles#getProfiles()
	 */
	@Override
	public List<String> getProfiles() {
		return profiles;
	}

	private String toTitleCase(String s) {
		char c[] = s.toCharArray();
		c[0] = Character.toUpperCase(c[0]);

		return new String(c);
	}
}