package com.app.emailparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.app.emailparser.Email;

/**
 * A program for parsing email from an archive and extracting data
 *
 * @author Vince Fonte
 */
public class EmailParser {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("ERROR: expecting input archive file and output file as parameters");
			System.err.println(help());
			System.exit(1);
		}
		String output_file_name = args[1];
		FileWriter output_file = null;
		try {
			output_file = new FileWriter(output_file_name);
		} catch (IOException e) {
			System.err.println("ERROR: failed to open output file for writing: " +  output_file_name);
			e.printStackTrace();
		}
		extractEmails(args[0], output_file);
		try {
			output_file.close();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to close output file: " + output_file);
			e.printStackTrace();
		}
	}

	/**
	 * Uncompresses archive and creates list of Email objects from text files.
	 * Also works when there are sub directories in archive
	 * @param input_file - input archive file containing plain text email files
	 * @return list of Email objects
	 */
	public static List<Email> extractEmails(String input_file, FileWriter output_file) {
		List<Email> results = new ArrayList<>();
		TarArchiveInputStream input_stream = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			input_stream = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(input_file)));
			TarArchiveEntry entry =  null;
			List<String> contents = null;
			writer = new BufferedWriter(output_file);
			// iterate over each entry in the compressed file
			while ((entry = input_stream.getNextTarEntry()) != null) {
				// skip if entry is a directory
				if (entry.isDirectory())
					continue;
				contents = new ArrayList<String>();
				reader = new BufferedReader(new InputStreamReader(input_stream));
				String line;
				// read contents line by line into a list of strings
				while ((line = reader.readLine()) != null) {
					contents.add(line);
				}
				Email email = new EmailSimple(contents, entry.getName());
				printResults(email, writer);
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find input file: " + input_file);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("ERROR: failed to read from input file: " + input_file);
			e.printStackTrace();
		} finally {
			try {
				input_stream.close();
				reader.close();
				writer.close();
			} catch (IOException e) {
				System.err.println("ERROR: Failed to close input file: " + input_file);
				e.printStackTrace();
			}
		}
		return results;
	}

	/**
	 * Prints the results of parsed email to a files
	 * @param emails - list of Email objects
	 * @param writer - file to write output to
	 */
	public static void printResults(Email email, BufferedWriter writer) {
		try {
			// print the extracted data from email to the output file
			String results = getResult(email);
			writer.write(results);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Failed to write to ouput file");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the data extracted from an email, | delimited
	 * Any value that can't be extracted is converted to empty string
	 * @param email
	 * @return | delineated string
	 */
	public static String getResult(Email email) {
		return email.getFileName() + "|" + email.getDateSent() + "|" + email.getFromAddress() + "|" + email.getSubject();
	}

	/**
	 * Returns help statement
	 */
	public static String help() {
		return "Usage: java -jar email-parser.jar INPUT_DATA OUTPUT_FILE\n" +
				"INPUT_DATA - gzipped tar file containing plain text email files\n" +
				"OUTPUT_FILE - file to print the extracted data";
	}
}
