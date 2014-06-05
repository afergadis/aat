package model;

import java.util.List;

import controller.History;

/**
 * Καταγράφει το ιστορικό περιήγησης σε ένα αρχείο. Το όνομα του αρχείου έχει το
 * προφίλ του χρήστη και τον τυχαίο αριθμό του. Κάθε γραμμή του αρχείου
 * αντιστοιχεί σε μία "επίσκεψη".
 * 
 * @author team1
 * 
 */
public class HistoryFile implements History {

    private final String filename;

    /**
     * Δημιουργεί ένα αρχείο καταγραφής με όνομα το userType, το userID και
     * κατάληξη log: userType + "-" + userID + ".log"
     * 
     * @param userType
     *            το προφίλ του χρήστη
     * @param userID
     *            ένας αριθμός που αντιπροσωπεύει το χρήστη
     */
    public HistoryFile(String userType, String userID) {
	this.filename = userType + "-" + userID + ".log";
	// TODO Δημιουργία του αρχείου
    }

    @Override
    public List<String> getHistory() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean addRecord(String data) {
	// TODO Auto-generated method stub
	return true;
    }

}
