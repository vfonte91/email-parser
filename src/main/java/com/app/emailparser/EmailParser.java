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

	public static void main(String[] args) throws IOException, FileNotFoundException {
		String output_file = args[1];
		List<Email> emails = getEmailFiles(args[0]);
		printResults(emails, output_file);
	}

	/**
	 * Uncompresses archive and creates list of Email objects from text files
	 * @param input_file - input archive file containing plain text email files
	 * @return list of Email objects
	 */
	public static List<Email> getEmailFiles(String input_file) throws IOException {
		List<Email> results = new ArrayList<>();
		TarArchiveInputStream input_stream = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(input_file)));
        TarArchiveEntry entry =  null;
        BufferedReader reader = null;
        List<String> contents = null;
        while ((entry = input_stream.getNextTarEntry()) != null) {
        		if (entry.isDirectory())
        			continue;
        		contents = new ArrayList<String>();
        		reader = new BufferedReader(new InputStreamReader(input_stream));
            String line;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }
            results.add(new EmailSimple(contents, entry.getName()));
        }
       input_stream.close();
       return results;
	}

	/**
	 * Prints the results of parsed emails to a files
	 * @param emails - list of Email objects
	 * @param output_file - the file to print results to
	 */
	public static void printResults(List<Email> emails, String output_file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output_file));
		for (Email email : emails) {
			writer.write(getResult(email));
			writer.newLine();
		}
		writer.close();
	}

	public static String getResult(Email email) {
		return email.getFileName() + "|" + email.getDateSent() + "|" + email.getFromAddress() + "|" + email.getSubject();
	}
}
