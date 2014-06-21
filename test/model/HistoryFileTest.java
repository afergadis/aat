package model;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

import controller.History;

public class HistoryFileTest {
	String filename = "basic-test";
	String fileid = "928314512";
	private final History h = new HistoryFile(filename, fileid);

	@Test
	public void testGetHistory() {
		String testKeys[] = { "test 1", "test 2", "test 3" };
		int testValues[] = { 1, 0, 0 };
		for (int i = 0; i < testKeys.length; i++) {
			h.addRecord(testKeys[i], testValues[i]);
		}

		Set<String> history = h.getHistory();
		equals(history.contains(testKeys));
	}

	@Test
	public void testAddRecord() {
		String testKeys[] = { "test 1", "test 2", "test 3" };
		int testValues[] = { 1, 0, 0 };
		String file = filename + "-" + fileid + ".log";
		System.out.println("Writing test values to file " + file);
		for (int i = 0; i < testKeys.length; i++) {
			h.addRecord(testKeys[i], testValues[i]);
			System.out.println(testKeys[i] + "," + testValues[i]);
		}
		h.commit();

		System.out.println("\nReading test values from file " + filename + "-"
				+ fileid + ".log");
		Path filename = Paths.get(System.getProperty("user.dir"), "log", file);
		try (BufferedReader br = Files.newBufferedReader(filename,
				StandardCharsets.UTF_8)) {
			String line;
			int lineno = 0;

			while ((line = br.readLine()) != null && lineno < testKeys.length) {
				System.out.println(line);
				String[] data = line.split(",", 3);
				assertEquals(testKeys[lineno], data[0]);
				assertEquals(testValues[lineno], Integer.parseInt(data[1]));
				lineno++;
			}

			br.close();
		} catch (IOException e) {
			System.out.println("Could not read \"" + filename + "\"");
			System.exit(1);
		}
		
		try {
			Files.deleteIfExists(filename);
			System.out.println(filename + " deleted.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
