package com.alertTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to parse console output lines produced by `AlertManager.triggerAlert()`.
 *
 * Expected line format:
 *   ALERT: <condition> for patient <id> at <timestamp>
 */
public class OutputReader {
	private static final Pattern LINE_PATTERN = Pattern.compile(
			"^ALERT:\\s*(.+?)\\s+for patient\\s+(\\d+)\\s+at\\s+(\\d+)$");

	/**
	 * Parse console output (possibly multi-line) and return parsed alert entries.
	 * Lines that do not match the expected format are ignored.
	 *
	 * @param consoleOutput full console output text
	 * @return list of parsed OutputLine instances
	 */
	public static List<OutputLine> parse(String consoleOutput) {
		List<OutputLine> result = new ArrayList<>();
		if (consoleOutput == null || consoleOutput.isEmpty()) return result;

		String[] lines = consoleOutput.split("\\r?\\n");
		for (String line : lines) {
			String trimmed = line.trim();
			if (trimmed.isEmpty()) continue;
			Matcher m = LINE_PATTERN.matcher(trimmed);
			if (m.matches()) {
				String condition = m.group(1).trim();
				int patientId;
				long timestamp;
				try {
					patientId = Integer.parseInt(m.group(2));
					timestamp = Long.parseLong(m.group(3));
				} catch (NumberFormatException ex) {
					continue; // ignore malformed numeric values
				}
				result.add(new OutputLine(patientId, condition, timestamp));
			}
		}
		return result;
	}
}
