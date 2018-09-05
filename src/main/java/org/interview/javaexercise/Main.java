package org.interview.javaexercise;

import org.interview.javaexercise.authentication.TwitterAuthenticationException;
import org.interview.javaexercise.authentication.TwitterAuthenticator;
import com.google.api.client.http.HttpRequestFactory;
import org.interview.javaexercise.model.Tweet;
import org.interview.javaexercise.service.TwitterProcessorService;
import org.interview.javaexercise.service.TwitterProcessorServiceImpl;
import org.interview.javaexercise.service.TwitterProcessingException;
import org.interview.javaexercise.utils.CvsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Exercise Main class.
 * @author luigi.corollo
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * This is the access point of the application, consists in the following steps:
     * 1. the authentication process for the twitter app
     * 2. print pre-run statistics (retrieves from app history)
     * 3. process the tweet messages
     * 4. print the list of tweets
     * 5. example of exporting the tweet list in CVS format
     * 6. print post-run statistics (saved in the app history for later use)
     * @param args
     */
    public static void main(String[] args){

        String consumerKey = "RLSrphihyR4G2UxvA0XBkLAdl";
        String consumerSecret = "FTz2KcP1y3pcLw0XXMX5Jy3GTobqUweITIFy4QefullmpPnKm4";
        String filter = "Bieber";

        long start = System.currentTimeMillis();
        logger.info("### Main - start");
        try {

            //1.authentication process
            TwitterAuthenticator authenticator = new TwitterAuthenticator(System.out, consumerKey, consumerSecret);
            HttpRequestFactory httpRequestFactory = authenticator.getAuthorizedHttpRequestFactory();

            TwitterProcessorService twitterProcessorService = new TwitterProcessorServiceImpl(httpRequestFactory,Preferences.userRoot());

            //2.print the statistics pre-execution
            logger.info(">> Get Pre-Execution Statistics: MessagesPerSecondStatistics[{} message/sec]",twitterProcessorService.getMessagesPerSecondStatistics());

            //3.processing tweets
            Map<String, List<Tweet>> tweetByUser = twitterProcessorService.processingTweets(filter);

            //4.print tweet list
            logger.info("--------- Tweet list ---------");
            for (List<Tweet> userTweetList : tweetByUser.values()) {
                for(Tweet tweet: userTweetList) {
                    logger.info(tweet.toString());
                }
            }
            logger.info("-------------------------------");

            /**
             * Here is an example of data export in CSV format useful for an automatic parsing process
             */
            //5.print tweet list in csv
            StringBuffer exportFormat = new StringBuffer();
            exportFormat
                    .append("Author-Id").append(",")  //AuthorId
                    .append("Author-CreationDate").append(",")
                    .append("Author-Name").append(",")
                    .append("Author-ScreenName").append(",")
                    .append("Message-Id").append(",") //Message
                    .append("Message-CreationDate").append(",")
                    .append("Message-Text")
                    .append("\n");
            for (List<Tweet> userTweetList : tweetByUser.values()) {
                for(Tweet tweet: userTweetList) {
                    exportFormat
                            .append(CvsUtils.addDoubleQuote(tweet.getAuthor().getUserId())).append(",")  //AuthorId
                            .append(CvsUtils.addDoubleQuote(tweet.getAuthor().getCreationDate())).append(",")
                            .append(CvsUtils.addDoubleQuote(tweet.getAuthor().getName())).append(",")
                            .append(CvsUtils.addDoubleQuote(tweet.getAuthor().getScreenName())).append(",")
                            .append(CvsUtils.addDoubleQuote(tweet.getMessageId())).append(",") //Message
                            .append(CvsUtils.addDoubleQuote(tweet.getCreationDate())).append(",")
                            .append(CvsUtils.addDoubleQuote(tweet.getMessageText()))
                            .append("\n");
                }
            }
            logger.info("--------- Tweet list (export format CVS) ---------");
            logger.info("EXPORT-CSV: \n{}",exportFormat.toString());
            logger.info("-------------------------------");

            //6.example of tweet list export in CSV format
            logger.info(">> Get Post-Execution Statistics: MessagesPerSecondStatistics[{} message/sec]",twitterProcessorService.getMessagesPerSecondStatistics());


        } catch (TwitterAuthenticationException | TwitterProcessingException e) {
            logger.error("Error: {}", e.getMessage(), e);
        } finally {
            long stop = System.currentTimeMillis();
            logger.info("### Main - end [elapsed: {}]",(stop-start));
        }
    }

}
