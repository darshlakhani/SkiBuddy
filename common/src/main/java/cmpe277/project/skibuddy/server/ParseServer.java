package cmpe277.project.skibuddy.server;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.LocationListener;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.Server;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 11/28/2015.
 */
public class ParseServer implements Server {
    public ParseServer(Context context){
        Parse.enableLocalDatastore(context);

        Parse.initialize(context, "QY0YiXoRaSmEDYBprKbQSgUMAPX2EgYaNF4spnLt", "c0CDe7W7J4aMeWJUpeuxMCP6vBalpS6oEnyOmWmC");
    }

    @Override
    public User authenticateUser(String authentication_token) {
        return null;
    }

    @Override
    public User getUser(UUID userID) {
        return null;
    }

    @Override
    public List<Run> getRuns(UUID eventID) throws NotAuthenticatedException {
        return null;
    }

    @Override
    public List<Run> getUserRuns(UUID userID) {
        return null;
    }

    @Override
    public void storeRun(Run run) throws NotAuthenticatedException {

    }

    @Override
    public List<Event> getEvents() throws NotAuthenticatedException {
        return null;
    }

    @Override
    public List<User> getEventParticipants(UUID eventID) throws NotAuthenticatedException {
        return null;
    }

    @Override
    public UUID storeEvent(Event event) throws NotAuthenticatedException {
        if(event.getEventID() == null)
            event.setEventID(UUID.randomUUID());

        ParseObject parseEvent = new ParseObject("Event");
        try {
            parseEvent.put("eventName", new Mapper().getMapper().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        parseEvent.saveInBackground();
        return event.getEventID();
    }

    @Override
    public void inviteUser(User userID, Event event) throws NotAuthenticatedException {

    }

    @Override
    public void acceptInvitation(Event event) throws NotAuthenticatedException {

    }

    @Override
    public void rejectInvitation(Event event) throws NotAuthenticatedException {

    }

    @Override
    public void updateLocation(Location location) throws NotAuthenticatedException {

    }

    @Override
    public void registerLocationListener(LocationListener listener, UUID eventID) throws NotAuthenticatedException {

    }

    @Override
    public void unregisterLocationListener(LocationListener listener) {

    }
}
