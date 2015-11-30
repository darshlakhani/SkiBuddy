package cmpe277.project.skibuddy.server.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.server.Mapper;

/**
 * Created by eh on 11/29/2015.
 */
@ParseClassName("Event")
public class ParseEvent extends ParseObject implements Event {
    private final String NAME_FIELD = "NAME";
    private final String DESCRIPTION_FIELD = "DESCRIPTION";
    private final String STARTDATE_FIELD = "STARTDATE";
    private final String ENDDATE_FIELD = "ENDDATE";
    private final String HOSTID_FIELD = "HOSTID";
    private final String EVENTID_FIELD = "EVENTID";

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
        try {
            return new Mapper().getMapper().readValue(getString(STARTDATE_FIELD), DateTime.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void setStart(DateTime start) {
        try {
            put(STARTDATE_FIELD, new Mapper().getMapper().writeValueAsString(start));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public DateTime getEnd() {
        try {
            return new Mapper().getMapper().readValue(getString(ENDDATE_FIELD), DateTime.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void setEnd(DateTime end) {
        try {
            put(ENDDATE_FIELD, new Mapper().getMapper().writeValueAsString(end));
        } catch (Exception e){
            e.printStackTrace();
        }
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
