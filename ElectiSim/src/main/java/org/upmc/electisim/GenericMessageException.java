package org.upmc.electisim;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * A class of exception which take a message at construction, which can be then used when the user wants to retrieve informations.
 * <p>The full message will be the message, with the stacktrace appender. The original message can be retrieved using the {@link #getMessage()}
 * method.</p>
 */
public class GenericMessageException extends Exception {
	
        /**
         * Generated UID
         */
        private static final long serialVersionUID = 2084939011328497522L;


        /**
         * Construct a new exception from the desired message.
         * @param msg the message to store inside the exception.
         */

        public GenericMessageException(String msg) {
        	super(msg);

        }        

        /**
         * Construct a new exception, concatenating the new message with the other exception message.
         * @param msg the message to which the other exception message will be appended.
         * @param other the other exception.
         */
        public GenericMessageException(String msg, Throwable other) {
        	super(msg + " (" + other.getMessage() + ")", other);
        }

        /**
         * @return The full exception message.
         */
        public String getFullMessage() {
            StringWriter sw = new StringWriter();

            printStackTrace(new PrintWriter(sw));
            
            return this.getClass().getName() + " : " + sw.toString();
        }

        
        /**
         * @return The full exception message.
         */
        @Override
        public String toString() {
        	return getMessage();
        }

        
        /**
         * @return The simple exception message.
         */
        @Override
        public String getMessage() {
        	return super.getMessage();
        }

}
