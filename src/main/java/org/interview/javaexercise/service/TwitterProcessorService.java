package org.interview.javaexercise.service;

import org.interview.javaexercise.model.Tweet;

import java.util.List;
import java.util.Map;

/**
 * Interface of twitter processor service.
 *
 * @author luigi.corollo
 */
public interface TwitterProcessorService {

    /**
     * Processing the tweets filtered by the input parameter 'filter'
     * @param filter: the filter used in the process
     * @return a map containing the messages grouped by user
     * @throws TwitterProcessingException
     */
    Map<String, List<Tweet>> processingTweets(String filter) throws TwitterProcessingException;

    /**
     * Returns the statistics on the messages per second (average value across multiple runs of the application)
     * @return the value of MessagesPerSecond Statistic
     */
    Double getMessagesPerSecondStatistics();
}
