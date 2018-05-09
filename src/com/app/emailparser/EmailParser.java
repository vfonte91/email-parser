/**
 * 
 */
package com.app.emailparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		List<File> emailFiles = getEmailFiles(args[0]);
		emailFiles.forEach(email -> {
			try {
				new Email(email);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	public static List<File> getEmailFiles(String input_file) throws IOException {
		List<File> results = new ArrayList<>();
		TarArchiveInputStream fin = new TarArchiveInputStream(new GZIPInputStream(new FileInputStream(input_file)));
        TarArchiveEntry entry;
        while ((entry = fin.getNextTarEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            File file = entry.getFile();
            results.add(file);
        }
       fin.close();
       return results;
	}
}
