package controller;

import java.util.List;

import model.AgoraObject;

/**
 * Αναζήτηση σε δομή (ΒΔ, RDF, αρχείο, ...) για μια συγκεκριμένη ξενάγηση.
 * 
 * @author team1
 * 
 */
public interface QueryClass {

    /**
     * Η συνάρτηση αυτή χρησιμοποιείται για να επιστρέψει μία λίστα με τις 
     * περιγραφές για όλα τα αντικείμενα μιας συγκεκριμένης ξενάγησης.
     * 
     * @return λίστα από String με τις περιγραφές των αντικειμένων
     */
    public abstract List<String> getDescriptions();
    
    /**
     * Για ένα συγκεκριμένο κωδικό αντικειμένου, επιστρέφει τις πληροφορίες
     * που το αφορούν (προτεινόμενοι σύνδεσμοι και αντικείμενα).
     * 
     * @param name
     * 		ο κωδικός του αντικειμένου
     * @return
     * 		ένα αντικείμενο τύπου AgoraObject
     */
    public abstract AgoraObject getExhibit(String name);
    
    /**
     * Για ένα συγκεκριμένο αριθμό αντικειμένου από τη σειρά του στην ξενάγηση
     * επιστρέφει τις πληροφορίες που το αφορούν (προτεινόμενοι σύνδεσμοι και αντικείμενα).
     * 
     * @param n
     * 		ο αύξων αριθμός του αντικειμένου
     * @return
     * 		ένα αντικείμενο τύπου AgoraObject
     */
    public abstract AgoraObject getExhibit(int n);
    
    /**
     * Επιστρέφει το αντικείμενο από το οποίο ξεκινάει η ξενάγηση.
     * 
     * @return
     * 		ένα αντικείμενο τύπου AgoraObject
     */
    public abstract AgoraObject getStart();
    
    public abstract List<AgoraObject> getObjects();
}