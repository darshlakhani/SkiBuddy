package cmpe277.project.skibuddy.server.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.UUID;

import cmpe277.project.skibuddy.common.ParticipationStatus;

/**
 * Created by eh on 12/2/2015.
 */
@ParseClassName("Participation")
public class ParseParticipation extends ParseObject {
    public static final String EVENT_FIELD = "EVENT";
    public static final String EVENTID_FIELD = "EVENT_ID";
    public static final String USERID_FIELD = "USER_ID";
    public static final String USER_FIELD = "USER";
    public static final String PARTICIPATION_FIELD = "PARTICIPATION";

    public ParseEvent getEvent(){
        return(ParseEvent) getParseObject(EVENT_FIELD);
    }

    public void setEvent(ParseEvent event){
        put(EVENT_FIELD, event);
        put(EVENTID_FIELD, event.getEventID().toString());
    }

    public ParseUser getUser(){
        return (ParseUser)getParseObject(USER_FIELD);
    }

    public void setUser(ParseUser user){
        put(USER_FIELD, user);
        put(USERID_FIELD, user.getId().toString());
    }

    public ParticipationStatus getParticipationStatus(){
        return ParticipationStatus.valueOf(getString(PARTICIPATION_FIELD));
    }

    public void setParticipationStatus (ParticipationStatus status){
        put(PARTICIPATION_FIELD, status.toString());
    }
}
