package model;

import java.util.ArrayList;
import java.util.List;

import controller.QueryProfiles;

/**
 * Επιλογή από την οντολογία των διαθέσιμων προφίλ
 *
 */
public class QueryProfilesOWL implements QueryProfiles {
	List<String> profiles;
	
	public QueryProfilesOWL() {
		profiles = new ArrayList<>();
		profiles.add("Παιδί");
		profiles.add("Ενήλικας");
		profiles.add("Ειδικός");
	}
	
	/* (non-Javadoc)
     * @see main.QueryProfiles#getProfiles()
     */
	@Override
    public List<String> getProfiles() {
		return profiles;
	}
}
