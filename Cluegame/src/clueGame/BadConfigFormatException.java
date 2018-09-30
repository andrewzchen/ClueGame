/**
 * Authors:
 * Andrew Chen
 * Jordyn McGrath
 */
package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception {
	/**
	 *  an exception that will be thrown in the case of not knowing exactly what has gone wrong
	 */
	BadConfigFormatException() {
		super("Error: Board config is not set up correctly");	
	}
	/**
	 * An exception that will be thrown with a message detailing why the exception was thrown
	 * Also writes the thrown exception to a logFile
	 * @param message
	 * @throws FileNotFoundException
	 */
	BadConfigFormatException(String message) throws FileNotFoundException {
		super("Error: " + message);
		PrintWriter out = new PrintWriter("logFile.txt");
		out.println("Error: " + message);
		out.close();
	}
}
