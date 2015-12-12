package cmpe277.project.skibuddy.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

import cmpe277.project.skibuddy.common.SkiBuddyLocation;

/**
 * Created by eh on 12/11/2015.
 */
public class LocationUpdate {
    private UUID eventID;
    private UUID userID;
    private String initials;
    private double latitude;
    private double longitude;
    private double elevation;

    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @JsonIgnore
    public SkiBuddyLocation getLocation(){
        SkiBuddyLocation loc = new PojoLocation();
        loc.setElevation(this.getElevation());
        loc.setLongitude(this.getLongitude());
        loc.setLatitude(this.getLatitude());
        return loc;
    }
}
