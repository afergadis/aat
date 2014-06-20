package model;

public class LogRecord {
	private String key;
	private int value;
	private String time;
	
	public LogRecord(String s, int i, String t) {
		key = s;
		value = i;
		time = t;
	}
	
	public String getValues() {
		return key + "," + value + "," + time;
	}
}
