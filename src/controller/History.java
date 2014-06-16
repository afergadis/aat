package controller;

import java.util.List;

/**
 * Το interface αυτό καθορίζει τις μεθόδους που πρέπει να υλοποιούν οι κλάσεις
 * που θα χειρίζονται το ιστορικό καταγραφής.
 * 
 * @author team1
 * 
 */
public interface History {

    /**
     * Η συνάρτηση διαβάζει το ιστορικό που βρίσκεται σε μια βάση, ένα rdf
     * αρχείο, αρχείο κειμένου ή ότι έχει επιλέξει η υλοποίηση και επιστρέφει
     * μια λίστα από String. Κάθε String αντιπροσωπεύει μια εγγραφή με τα
     * στοιχεία της χωρισμένα με κόμμα.
     * 
     * @return λίστα με String. Κάθε String αντιστοιχεί σε μια εγγραφή.
     */
    public abstract List<String> getHistory();

    /**
     * Εισάγει μία εγγραφή (comma separated) στη δομή.
     * 
     * @return true εάν η εγγραφή έγινε, false διαφορετικά.
     */
    public abstract boolean addRecord(String data);

}