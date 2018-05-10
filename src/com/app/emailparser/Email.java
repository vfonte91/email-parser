/**
 * 
 */
package com.app.emailparser;

/**
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
	 * Returns the subject
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
