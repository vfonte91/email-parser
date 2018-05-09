package com.app.emailparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Email {
	private List<String> content;
	private String file_name = "";

	public Email(File input_file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input_file));
		content = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null)
			content.add(line);
		br.close();
	}

	public Email(List<String> content, String file_name) {
		this.content = content;
		this.file_name = file_name;
	}
	
	public String printOutput(String output) {
		return "Foo!";
	}

	public void printContents() {
		for (String line : content)
			System.out.println(line);
	}

	public String getFileName() {
		return this.file_name;
	}

	public String getFromAddress() {
		String results = "";
		for (String line  : content) {
			if (line.contains("From: ")) {
				line = line.replace("From: ", "").replaceAll(".*<", "").replaceAll(">", "");
				results = line;
				break;
			}
		}
		return results;
	}

	public String getDateSent() {
		String results = "";
		for (String line  : content) {
			if (line.contains("Date: ")) {
				line = line.replace("Date: ", "");
				results = line;
				break;
			}
		}
		return results;
	}
}
