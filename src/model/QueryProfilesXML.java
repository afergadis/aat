package model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import controller.QueryProfiles;

/**
 * Επιλογή από την οντολογία των διαθέσιμων προφίλ
 * 
 */
public class QueryProfilesXML implements QueryProfiles {
	List<String> profiles;

	public QueryProfilesXML() {
		InputSource ns1xml = new InputSource("data/UserModelling.rdf");
		NamespaceContext context = new NamespaceContextMap("owlnl",
				"http://www.aueb.gr/users/ion/owlnl#", "rdf",
				"http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(context);
		XPathExpression expression;
		try {
			expression = xpath.compile("//owlnl:UserType/@rdf:ID");
			NodeList list = (NodeList) expression.evaluate(ns1xml,
					XPathConstants.NODESET);
			profiles = new ArrayList<>();
			for (int i = 0; i < list.getLength(); i++) {
				profiles.add(toTitleCase(list.item(i).getNodeValue().toString()));
			}
		} catch (XPathExpressionException e) {
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