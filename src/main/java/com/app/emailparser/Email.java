/**
 * 
 */
package com.app.emailparser;

/**
 * An object that represents an email. Includes functions for extracting data
 * @author Vince Fonte
 */
public interface Email {

	/**
	 * Returns the from address
	 *
	 * @return string
	 */
	String getFromAddress();

	/**
	 * Returns the date the email was sent
	 *
	 * @return string
	 */
	String getDateSent();

	/**
	 * Returns the subject. Converts mutlilined subjects into one line
	 *
	 * @return string
	 */
	String getSubject();

	/**
	 * Returns the name of the file
	 *
	 * @return string
	 */
	String getFileName();
}
