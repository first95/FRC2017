package org.usfirst.frc.team95.robot;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.TreeMap;
import java.util.Map;

public class VariableStore {
	private static final String storeName = "/home/lvuser/store";
	private Map<String, String> storedValues;
	
	public VariableStore() {
		storedValues = new TreeMap<String, String>();
		readStoredValues();
	}
	
	public double GetDouble(String variableName, double defaultValue) {
		double value = defaultValue;
		if (storedValues.containsKey(variableName)) {
			value = Double.parseDouble(storedValues.get(variableName));
		}
		return value;
	}
	
	public Object GetValue(String variableName, Object defaultValue) {
		Object value = defaultValue;
		if (storedValues.containsKey(variableName)) {
			value = storedValues.get(variableName);
		}
		return value;
	}
	
	public void StoreValue(String variableName, Object value) {
		storedValues.put(variableName, value.toString());
		writeStoredValues();
	}
	
	private void readStoredValues() {
		try (BufferedReader reader = new BufferedReader(new FileReader(storeName))) {
			while (reader.ready()) {
				String variableName = reader.readLine();
				String variableData = reader.readLine();
				storedValues.put(variableName, variableData);
			}
		}
		catch (FileNotFoundException e) {
			// Not really a failure. Storage doesn't exist yet.
		}
		catch (IOException e) {
			// System.out.println(e);
		}
	}
	
	private void writeStoredValues() {
		try (PrintWriter writer = new PrintWriter(storeName)) {
			for (Map.Entry<String, String> entry : storedValues.entrySet()) {
				writer.println(entry.getKey());
				writer.println(entry.getValue());
			}
		}
		catch (IOException e) {
			// System.out.println(e);
		}
		
	}
}
