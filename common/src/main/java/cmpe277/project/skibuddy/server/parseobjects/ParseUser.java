package cmpe277.project.skibuddy.server.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.Duration;

import java.util.UUID;

import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 12/1/2015.
 */
@ParseClassName("User")
public class ParseUser extends ParseObject implements User {
    public static final String NAME_FIELD = "NAME";
    public static final String NAME_LOWERCASE_FIELD = "NAME_TOLOWER";
    public static final String TAGLINE_FIELD = "TAGLINE";
    public static final String PROFILEPIC_FIELD = "PROFILE_PIC";
    public static final String ID_FIELD = "USERID";
    public static final String AUTHTOKEN_FIELD = "AUTHTOKEN";
    public static final String TOTALDISTANCE_FIELD = "TOTALDISTANCE";
    public static final String TOTALTIME_FIELD = "TOTALTIME";
    public static final String TOPSPEED_FIELD = "TOPSPEED";

    @Override
    public String getName() {
        return getString(NAME_FIELD);
    }

    @Override
    public void setName(String name) {
        put(NAME_FIELD, name);
        put(NAME_LOWERCASE_FIELD, name.toLowerCase());
    }

    @Override
    public String getTagline() {
        return getString(TAGLINE_FIELD);
    }

    @Override
    public void setTagline(String tagline) {
        put(TAGLINE_FIELD, tagline);
    }

    @Override
    public String getProfilePictureURL() {
        return getString(PROFILEPIC_FIELD);
    }

    @Override
    public void setProfilePictureURL(String profilePictureURL) {
        put(PROFILEPIC_FIELD, profilePictureURL);
    }

    @Override
    public UUID getId() {
        return UUID.fromString(getString(ID_FIELD));
    }

    @Override
    public void setId(UUID id) {
        put(ID_FIELD, id.toString());
    }

    @Override
    public SkiBuddyLocation getPosition() {
        return null;
    }

    @Override
    public void setPosition(SkiBuddyLocation position) {

    }

    public void incrementTotalDistance(double distance){
        increment(TOTALDISTANCE_FIELD, distance);
    }

    @Override
    public double getTotalDistance() {
        return getDouble(TOTALDISTANCE_FIELD);
    }

    public void incrementTotalTime(Duration totalTime){
        increment(TOTALTIME_FIELD, totalTime.getMillis());
    }

    @Override
    public Duration getTotalTime() {
        return new Duration(getLong(TOTALTIME_FIELD));
    }

    public void setTopSpeed(double topSpeed){
        put(TOPSPEED_FIELD, topSpeed);
    }

    @Override
    public double getTopSpeed() {
        return getDouble(TOPSPEED_FIELD);
    }

    public void getAuthToken(){
        getString(AUTHTOKEN_FIELD);
    }

    public void setAuthToken(String authToken){
        put(AUTHTOKEN_FIELD, authToken);
    }
}
