package cmpe277.project.skibuddy.server.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;

/**
 * Created by eh on 11/29/2015.
 */
@ParseClassName("Event")
public class ParseEvent extends ParseObject implements Event {
    public static final String NAME_FIELD = "NAME";
    public static final String DESCRIPTION_FIELD = "DESCRIPTION";
    public static final String STARTDATE_FIELD = "STARTDATE";
    public static final String ENDDATE_FIELD = "ENDDATE";
    public static final String HOSTID_FIELD = "HOSTID";
    public static final String EVENTID_FIELD = "EVENTID";

    @Override
    public String getName() {
        return getString(NAME_FIELD);
    }

    @Override
    public void setName(String name) {
        put(NAME_FIELD, name);
    }

    @Override
    public String getDescription() {
        return getString(DESCRIPTION_FIELD);
    }

    @Override
    public void setDescription(String description) {
        put(DESCRIPTION_FIELD, description);
    }

    @Override
    public DateTime getStart() {
        return new DateTime(getDate(STARTDATE_FIELD));
    }

    @Override
    public void setStart(DateTime start) {
        put(STARTDATE_FIELD, start.toDate());
    }

    @Override
    public DateTime getEnd() {
        return new DateTime(getDate(ENDDATE_FIELD));
    }

    @Override
    public void setEnd(DateTime end) {
        put(ENDDATE_FIELD, end.toDate());
    }

    @Override
    public UUID getHostId() {
        return UUID.fromString(getString(HOSTID_FIELD));
    }

    @Override
    public void setHostId(UUID host) {
        put(HOSTID_FIELD, host.toString());
    }

    @Override
    public UUID getEventID() {
        return UUID.fromString(getString(EVENTID_FIELD));
    }

    @Override
    public void setEventID(UUID eventID) {
        put(EVENTID_FIELD, eventID.toString());
    }
}
