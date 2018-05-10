package com.app.emailparser;

import java.util.List;

/**
 * @author Vince Fonte
 */
public class EmailSimple implements Email {
	private List<String> content;
	private String file_name = "";

	public EmailSimple(List<String> content, String file_name) {
		this.content = content;
		this.file_name = file_name;
	}

	public String getFileName() {
		return this.file_name;
	}

	public String getFromAddress() {
		String results = "";
		for (String line : content) {
			if (line.matches("^From:\\s+.*")) {
				line = line.replaceAll("From:", "").replaceAll(".*<", "").replaceAll(">", "").replaceAll("\\s+","");
				results = line;
				break;
			}
		}
		return results;
	}

	public String getDateSent() {
		String results = "";
		for (String line : content) {
			if (line.matches("^Date:\\s+.*")) {
				line = line.replaceAll("Date:\\s+", "");
				results = line;
				break;
			}
		}
		return results;
	}

	public String getSubject() {
		String results = "";
		int count = 0;
		for (String line : content) {
			if (line.matches("^Subject:\\s+.*")) {
				line = line.replace("Subject: ", "");
				results = line;
				// Some subjects are multiple lines that begin with a space
				try {
				String rest = "";
				while (content.get(count + 1).matches("^\\s+.*$")) {
					// convert multiple lines into single
					rest = rest + content.get(count + 1);
					count++;
				}
					results = results + rest;
				} catch (IndexOutOfBoundsException e) {}
				break;
			}
			count++;
		}
		return results;
	}
}
