package com.app.emailparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class EmailParserTests {
	private final String output_file = "out";

    @Test
    void multilineSubject() {
    	List<String> test_email = new ArrayList<String>();
    	test_email.add("Subject: Woah, this subject");
    	test_email.add(" is on multiple");
    	test_email.add(" lines!");
    	test_email.add("Date: Fri,  1 Apr 2011 08:12:00 -0600 (MDT)");
    	Email email = new EmailSimple(test_email, output_file);
    	assertEquals(email.getSubject(), "Woah, this subject is on multiple lines!");
    }

    @Test
    void fromAddress() {
    	List<String> test_email = new ArrayList<String>();
    	test_email.add("Subject: Hey!");
    	test_email.add("From: Some guy <someguy@google.com>");
    	test_email.add("Date: Fri,  1 Apr 2011 08:12:00 -0600 (MDT)");
    	Email email = new EmailSimple(test_email, output_file);
    	assertEquals(email.getFromAddress(), "someguy@google.com");
    }

    @Test
    void dateSent() {
    	List<String> test_email = new ArrayList<String>();
    	test_email.add("Subject: Hey!");
    	test_email.add("From: Some guy <someguy@google.com>");
    	test_email.add("Date: Fri,  1 Apr 2011 08:12:00 -0600 (MDT)");
    	Email email = new EmailSimple(test_email, output_file);
    	assertEquals(email.getDateSent(), "Fri,  1 Apr 2011 08:12:00 -0600 (MDT)");
    }

    @Test
    void fileName() {
    	List<String> test_email = new ArrayList<String>();
    	test_email.add("From: Some guy <someguy@google.com>");
    	Email email = new EmailSimple(test_email, output_file);
    	assertEquals(email.getFileName(), output_file);
    }

    @Test
    void noData(){
    	List<String> test_email = new ArrayList<String>();
    	test_email.add("Content-Type: text/html; charset=UTF-8");
    	test_email.add("To: Some guy <someguy@google.com>");
    	test_email.add("MIME-Version: 1.0");
    	Email email = new EmailSimple(test_email, output_file);
    	assertEquals(email.getDateSent(), "");
    	assertEquals(email.getFromAddress(), "");
    	assertEquals(email.getSubject(), "");
    }

}