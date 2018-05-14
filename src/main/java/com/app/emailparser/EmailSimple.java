package com.app.emailparser;

import java.util.List;

/**
 * A simple implementation of the Email interface
 *
 * @author Vince Fonte
 */
public class EmailSimple implements Email {
	private List<String> content;
	private String file_name = "";

	public EmailSimple(List<String> content, String file_name) {
		this.content = content;
		this.file_name = file_name;
	}

	@Override
	public String getFileName() {
		return this.file_name;
	}

	@Override
	public String getFromAddress() {
		String results = "";
		for (String line : content) {
			if (line.matches("^[Ff]rom:\\s+.*")) {
				// remove all characters except email address
				results = line.replaceAll("[Ff]rom:", "").replaceAll(".*<", "").replaceAll(">", "").replaceAll("\\s+","");
				break;
			}
		}
		return results;
	}

	@Override
	public String getDateSent() {
		String results = "";
		for (String line : content) {
			if (line.matches("^[Dd]ate:\\s+.*")) {
				// remove Date: keyword
				results = line.replaceAll("[Dd]ate:\\s+", "");
				break;
			}
		}
		return results;
	}

	@Override
	public String getSubject() {
		String results = "";
		int count = 0;
		for (String line : content) {
			if (line.matches("^[Ss]ubject:\\s+.*")) {
				// remove Subject: keyword
				line = line.replaceAll("[Ss]ubject: ", "");
				results = line;
				// Some subjects are multiple lines that begin with a space
				// only grabs the first line if it reaches the end of the file without finding the end of the subject
				try {
					String rest = "";
					while (content.get(count + 1).matches("^\\s+.*$")) {
						// convert multiple lines into single
						rest = rest + content.get(count + 1);
						count++;
					}
					results = results + rest;
					// replace and | with a !
					results = results.replace("|", "!");
				} catch (IndexOutOfBoundsException e) {
					System.err.println("WARNING: could not find end of subject for " + this.file_name);
					System.err.println("Only using the first line in the subject");
				}
				break;
			}
			count++;
		}
		return results;
	}
}
