package org.interview.javaexercise.service;

import org.interview.javaexercise.config.Constants;
import org.interview.javaexercise.model.Tweet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;


/**
 * Twitter Processor  Service Implementation.
 *
 * @author luigi.corollo
 */
public class TwitterProcessorServiceImpl implements TwitterProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(TwitterProcessorServiceImpl.class);

    /** Endpoint */
    public static final String ENDPOINT_STREAM_TWITTER = "https://stream.twitter.com/1.1/statuses/filter.json";

    private final HttpRequestFactory httpRequestFactory;

    /** */
    private final Preferences preferences;

    /**
     * Public Costructor
     * @param httpRequestFactory the factory for the https requests.
     * @param preferences the preference object used to store the histrory used for the statistics
     */
    public TwitterProcessorServiceImpl(HttpRequestFactory httpRequestFactory, Preferences preferences) {
        this.httpRequestFactory = httpRequestFactory;
        this.preferences = preferences;
    }


    /**
     * Retrieve the InputStream of messages from Twitter
     * @param filter used to filter messages
     * @return the stream (@see InputStream) of the messages
     * @throws IOException
     */
    InputStream getContent(String filter) throws IOException {
        logger.debug("getContent - start");
        HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(ENDPOINT_STREAM_TWITTER.concat("?track=").concat(filter)));
        HttpResponse response = request.execute();
        InputStream in = response.getContent();
        logger.debug("getContent - end");
        return in;
    }

    /**
     * Parse the InputStream in a List of Tweets
     * @param inputStream the stream from twitter
     * @return a list of tweet messages
     * @throws IOException
     */
    List<Tweet> parseInputStreamToTweetList(InputStream inputStream) throws IOException {
        logger.debug("parseInputStreamToTweetList - start");

        List<Tweet> tweetList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        mapper.setDateFormat(dateFormat);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();

        int countTweets = 0;
        long startTime = System.currentTimeMillis();

        while (line != null && countTweets < Constants.MAX_TWEET_SIZE &&
                (System.currentTimeMillis() - startTime < Constants.MAX_TIME_INTERVAL)) {

            // Parse tweet and add to the list
            tweetList.add(mapper.readValue(line, Tweet.class));
            logger.info("[{}] receiving data from service << line[{}]",countTweets,line);

            line = reader.readLine();
            countTweets++;
            logger.debug("Tweet-Counter[{}]",countTweets);
        }
        long endTime = System.currentTimeMillis();

        //
        inputStream.close();

        //statistics
        double deltaTimeInSec = (endTime - startTime) / 1000;
        double messagePerSecondsStat = (countTweets / deltaTimeInSec);

        logger.debug("[Stat] Tot-Num-Of-Tweet[{} message]", countTweets);
        logger.debug("[Stat] Tot-Elapsed-Time-In-Sec[{} sec]", deltaTimeInSec);
        logger.debug("[Stat] MessagePerSecondsStat[{} message/sec]", messagePerSecondsStat);

        double oldValue = this.preferences.getDouble(Constants.KEY_MESSAGE_PER_SECONDS_STAT,0.0);
        if(oldValue>0.0){
            messagePerSecondsStat = (messagePerSecondsStat + oldValue) / 2;
        }
        logger.debug("[Stat] MessagePerSecondsStat-Total[{} message/sec]", messagePerSecondsStat);
        this.preferences.putDouble(Constants.KEY_MESSAGE_PER_SECONDS_STAT,messagePerSecondsStat);

        logger.debug("parseInputStreamToTweetList - end");
        return tweetList;
    }

    /**
     * Gets the map of tweets group by Author
     * @param tweetList the tweet List.
     * @return A Map of tweet messages grouped by Author.
     */
    Map<String, List<Tweet>> getMapOfTweetsSortedByUserChronologycally(List<Tweet> tweetList) {
        logger.debug("getMapOfTweetsSortedByUserChronologycally - start");

        /** Map of tweets group by User */
        Map<String, List<Tweet>> tweetByUser = tweetList.stream()
                .sorted((p1,p2) -> p1.getAuthor().getCreationDate().compareTo(p2.getAuthor().getCreationDate()))
                .collect(Collectors.groupingBy(
                        p -> p.getAuthor().getUserId()));

        for (List<Tweet> userTweetList : tweetByUser.values()) {
            userTweetList.sort((p1, p2) -> p1.compareTo(p2));
        }

        logger.debug("getMapOfTweetsSortedByUserChronologycally - end");
        return tweetByUser;
    }


    @Override
    public Map<String, List<Tweet>> processingTweets(String filter) throws TwitterProcessingException {

        long start = System.currentTimeMillis();
        logger.info("processingTweets - start");
        try {
            return getMapOfTweetsSortedByUserChronologycally(parseInputStreamToTweetList(getContent(filter)));
        } catch (IOException e) {
            throw new TwitterProcessingException("Exception occurred in processing the tee".concat(e.getMessage()));
        } finally {
            long stop = System.currentTimeMillis();
            logger.info("processingTweets - end [elapsed: {}]",(stop-start));
        }
    }

    @Override
    public Double getMessagesPerSecondStatistics() {
        return this.preferences.getDouble(Constants.KEY_MESSAGE_PER_SECONDS_STAT,0.0);
    }
}
