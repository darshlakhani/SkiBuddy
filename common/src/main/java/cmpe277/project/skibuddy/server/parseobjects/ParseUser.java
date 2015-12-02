package cmpe277.project.skibuddy.server.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.Duration;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 12/1/2015.
 */
@ParseClassName("User")
public class ParseUser extends ParseObject implements User {
    public static final String NAME_FIELD = "NAME";
    public static final String TAGLINE_FIELD = "TAGLINE";
    public static final String PROFILEPIC_FIELD = "PROFILE_PIC";
    public static final String ID_FIELD = "USERID";
    public static final String AUTHTOKEN_FIELD = "AUTHTOKEN";

    @Override
    public String getName() {
        return getString(NAME_FIELD);
    }

    @Override
    public void setName(String name) {
        put(NAME_FIELD, name);
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
    public Location getPosition() {
        return null;
    }

    @Override
    public void setPosition(Location position) {

    }

    @Override
    public double getTotalDistance() {
        return 0;
    }

    @Override
    public Duration getTotalTime() {
        return null;
    }

    @Override
    public double getTopSpeed() {
        return 0;
    }

    public void getAuthToken(){
        getString(AUTHTOKEN_FIELD);
    }

    public void setAuthToken(String authToken){
        put(AUTHTOKEN_FIELD, authToken);
    }
}
