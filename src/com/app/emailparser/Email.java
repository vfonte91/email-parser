package com.app.emailparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Email {
	private List<String> text = new ArrayList<String>();

	public Email(File input_File) throws FileNotFoundException {
		Scanner s = new Scanner(input_File);
		ArrayList<String> list = new ArrayList<String>();
		while (s.hasNextLine()){
		    list.add(s.nextLine());
		}
		s.close();
		text = list;
	}

	public String printOutput(String output) {
		return "Foo!";
	}
}
