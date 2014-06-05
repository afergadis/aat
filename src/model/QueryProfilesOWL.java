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
	profiles.add("Child");
	profiles.add("Adult");
	profiles.add("Expert");
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
}
