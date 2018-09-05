package org.interview.javaexercise.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.interview.javaexercise.config.Constants;

import java.util.Date;

/**
 * Author model.
 * Model used to parse the json stream of the Author of the twitter message.
 * @author luigi.corollo
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @JsonProperty(Constants.FIELD_USER_ID)
    private String userId;

    @JsonProperty(Constants.FIELD_CREATED_AT)
    private Date creationDate;

    @JsonProperty(Constants.FIELD_NAME)
    private String name;

    @JsonProperty(Constants.FIELD_SCREEN_NAME)
    private String screenName;

    public String getUserId() {
        return userId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Author[");
        sb.append("userId[").append(userId).append("]");
        sb.append(", creationDate[").append(creationDate).append("]");
        sb.append(", name[").append(name).append("]");
        sb.append(", screenName[").append(screenName).append("]");
        sb.append(']');
        return sb.toString();
    }
}
