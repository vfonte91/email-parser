package com.app.emailparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Email {
	private File email_file = new File();

	public Email(File input_File) throws FileNotFoundException {
		Scanner s = new Scanner(input_File);
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		byte[] encoded = Files.readAllBytes(Paths.get(input_File));
		return new String(encoded, encoding);
	}
	
	public String printOutput(String output) {
		return "Foo!";
	}
}
