package org.interview.javaexercise.service;


import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.interview.javaexercise.config.Constants;
import org.interview.javaexercise.model.Tweet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Unit Test used to test the twitter processing methods.
 * @author luigi.corollo
 */
public class TwitterProcessorServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(TwitterProcessorServiceImpl.class);

    private HttpRequestFactory httpRequestFactoryMock;
    private Preferences preferencesMock;

    @Before
    public void before() throws Exception {
        this.preferencesMock = Mockito.mock(Preferences.class);
        Mockito.when(preferencesMock.getDouble(Constants.KEY_MESSAGE_PER_SECONDS_STAT,0.0)).thenReturn(0.0);
    }


    @Test
    public void processingTweets() throws Exception {

        //get the static json, the input of the process
        FileInputStream fileInputStream = new FileInputStream(new File("src/test/resources/test-content.json"));


        TwitterProcessorService twitterProcessorService = new TwitterProcessorServiceImpl(httpRequestFactoryMock,preferencesMock);

        //pre test
        Assert.assertTrue(twitterProcessorService.getMessagesPerSecondStatistics()==0.0);


        List<Tweet> tweetList = ((TwitterProcessorServiceImpl) twitterProcessorService).parseInputStreamToTweetList(fileInputStream);
        Assert.assertNotNull(tweetList);
        Assert.assertEquals(10, tweetList.size());

        Map<String, List<Tweet>> tweetByUser =  ((TwitterProcessorServiceImpl) twitterProcessorService).getMapOfTweetsSortedByUserChronologycally(tweetList);
        Assert.assertEquals(10, tweetByUser.keySet().size());
        Assert.assertEquals(10, tweetByUser.entrySet().size());

        //print tweet list (debug purpose)
        for (List<Tweet> userTweetList : tweetByUser.values()) {
            for(Tweet tweet: userTweetList) {
                logger.info(tweet.toString());
            }
        }

    }

}
