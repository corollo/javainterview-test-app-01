package org.interview.javaexercise.service;

/**
 * Custom Exception used in TwitterProcessorService
 *
 * @author luigi.corollo
 */
public class TwitterProcessingException extends Exception {

    public TwitterProcessingException() {
        super();
    }

    public TwitterProcessingException(final String message) {
        super(message);
    }

    public TwitterProcessingException(final String message, final Throwable t) {
        super(message, t);
    }

    public TwitterProcessingException(final Throwable t) {
        super(t);
    }

}
