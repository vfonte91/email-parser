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
			System.exit(1);
		}
		String output_file = args[1];
		// uncompress archive and create a list of email objects
		List<Email> emails = extractEmails(args[0]);
		// print the extracted data to a file
		printResults(emails, output_file);
	}

	/**
	 * Uncompresses archive and creates list of Email objects from text files.
	 * Also works when there are sub directories in archive
	 * @param input_file - input archive file containing plain text email files
	 * @return list of Email objects
	 */
	public static List<Email> extractEmails(String input_file) {
		List<Email> results = new ArrayList<>();
		TarArchiveInputStream input_stream = null;
        BufferedReader reader = null;
		try {
			input_stream = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(input_file)));
	        TarArchiveEntry entry =  null;
	        List<String> contents = null;
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
	            // create an email object out of the list of strings and add it to the results
	            results.add(new EmailSimple(contents, entry.getName()));
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
			} catch (IOException e) {
				System.err.println("ERROR: Failed to close input file:" + input_file);
				e.printStackTrace();
			}
		}
       return results;
	}

	/**
	 * Prints the results of parsed email to a files
	 * @param emails - list of Email objects
	 * @param output_file - the file to print results to
	 */
	public static void printResults(List<Email> emails, String output_file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(output_file));
			// print the extracted data from each email to the output file, line by line
			for (Email email : emails) {
				writer.write(getResult(email));
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("ERROR: Failed to write to ouput file: " + output_file);
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.err.println("ERROR: Failed to close output file:" + output_file);
				e.printStackTrace();
			}
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
}
