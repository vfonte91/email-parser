/**
 * 
 */
package com.app.emailparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.app.emailparser.Email;

/**
 * @author Vince Fonte
 *
 */
public class EmailParser {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException {
		List<Email> emailFiles = getEmailFiles(args[0]);
		for (Email email : emailFiles) {
			System.out.println(getResult(email));
		}
	}

	public static List<Email> getEmailFiles(String input_file) throws IOException {
		List<Email> results = new ArrayList<>();
		TarArchiveInputStream fin = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(input_file)));
        TarArchiveEntry entry =  null;
        BufferedReader br = null;
        List<String> contents = null;
        while ((entry = fin.getNextTarEntry()) != null) {
        		if (entry.isDirectory())
        			continue;
        		contents = new ArrayList<String>();
            br = new BufferedReader(new InputStreamReader(fin));
            String line;
            while ((line = br.readLine()) != null) {
                contents.add(line);
            }
            results.add(new Email (contents, entry.getName()));
        }
       fin.close();
       return results;
	}

	public static String getResult(Email email) {
		return email.getFileName() + "|" + email.getDateSent() + "|" + email.getFromAddress();
	}
}
