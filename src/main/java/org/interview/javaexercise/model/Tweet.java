package org.interview.javaexercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.interview.javaexercise.config.Constants;

import java.util.Date;

/**
 * Tweet model.
 * Model used to parse the json stream of the Twitter message.
 * @author luigi.corollo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet implements Comparable {

    @JsonProperty(Constants.FIELD_MESSAGE_ID)
    private String messageId;

    @JsonProperty(Constants.FIELD_CREATED_AT)
    private Date creationDate;

    @JsonProperty(Constants.FIELD_TEXT)
    private String messageText;

    @JsonProperty(Constants.FIELD_AUTHOR)
    private Author author;

    public String getMessageId() {
        return messageId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public Author getAuthor() {
        return author;
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        final int EQUAL = 0;
        int result;

        Tweet oTweet = (Tweet) o;
        if(this == oTweet)
            result = EQUAL;
        else
            result = this.getCreationDate().compareTo(oTweet.getCreationDate());

        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Tweet[");
        sb.append("messageId[").append(messageId).append("]");
        sb.append(", creationDate[").append(creationDate).append("]");
        sb.append(", messageText[").append(messageText).append("]");
        sb.append(", ").append(author);
        sb.append(']');
        return sb.toString();
    }
}
