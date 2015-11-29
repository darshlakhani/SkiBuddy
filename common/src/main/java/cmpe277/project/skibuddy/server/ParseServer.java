package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;

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
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 11/28/2015.
 */
public class ParseServer implements Server {
    private final Context context;

    public ParseServer(Context context){
        this.context = context;
//        Parse.enableLocalDatastore(context);
//
        Parse.initialize(context, "QY0YiXoRaSmEDYBprKbQSgUMAPX2EgYaNF4spnLt", "c0CDe7W7J4aMeWJUpeuxMCP6vBalpS6oEnyOmWmC");
    }

    @Override
    public void authenticateUser(String authentication_token, final ServerCallback<User> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Handler mainHandler = new Handler(context.getMainLooper());
                User result = new User();
                result.setName("John Doe");
                callback.postResult(result);
                mainHandler.post(callback);
            }
        }).start();
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
            parseEvent.put("event", new Mapper().getMapper().writeValueAsString(event));
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
