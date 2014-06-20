package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private Path file = null;
	private List<LogRecord> records;

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
		records = new ArrayList<>();
		String filename = userType + "-" + userID + ".log";
		file = Paths.get(System.getProperty("user.dir"), "log", filename);

	}

	@Override
	public Set<String> getHistory() {
		Set<String> logRecords = new HashSet<>();
		for (LogRecord r : records) {
			logRecords.add(r.getValues().split(",")[0]);
		}

		return logRecords;
	}

	@Override
	public void addRecord(String key, int value) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date resultDate = new Date(System.currentTimeMillis());

		LogRecord r = new LogRecord(key, value, sdf.format(resultDate));
		records.add(r);
	}

	@Override
	public void commit() {
		if (records.size() > 0) {
			// Φτιάξε μια εγγραφή για την ώρα εξόδου
			addRecord("", 0);
			// Γράψε τις εγγραφές στο αρχείο
			Path parentDir = file.getParent();
			if (!Files.exists(parentDir)) {
				try {
					Files.createDirectories(parentDir);
				} catch (IOException e) {
					System.out.println("ERROR: Unable to create path "
							+ parentDir);
					System.exit(1);
				}
			}
			try (BufferedWriter bw = Files.newBufferedWriter(file,
					StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
				for (LogRecord r : records) {
					bw.write(r.getValues() + "\n");
				}
			} catch (IOException e) {
				System.out.println("ERROR. Unable to create file: "
						+ file.toString());
			}
		}
	}

}
