package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.UUID;

import bolts.Continuation;
import bolts.Task;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.LocationListener;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.parseobjects.ParseEvent;

/**
 * Created by eh on 11/29/2015.
 */
public class ParseServer implements Server {
    final Context context;

    public ParseServer(Context context){
        this.context = context;

        ParseObject.registerSubclass(ParseEvent.class);

        Parse.initialize(context, "REO5YRRyjUfaHVNB4dplAfCRxTr8rJndVTxIOP0Q", "0yAKP0rwx6Ske9TUA4gmRXrmCUjXyUcmtFYv9ENq");
    }

    private void invokeCallback(ServerCallback callback){
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(callback);
    }

    @Override
    public void authenticateUser(String authentication_token, ServerCallback<User> callback) {

    }

    @Override
    public User getAuthenticatedUser() throws NotAuthenticatedException {
        return null;
    }

    @Override
    public void getUser(UUID userID, ServerCallback<User> callback) {

    }

    @Override
    public void storeUser(User user) {

    }

    @Override
    public void getRuns(UUID eventID, ServerCallback<List<Run>> callback) {

    }

    @Override
    public void getUserRuns(UUID userID, ServerCallback<List<Run>> callback) {

    }

    @Override
    public void storeRun(Run run) {

    }

    @Override
    public void getEvent(UUID eventID, ServerCallback<Event> callback) {

    }

    @Override
    public void getEvents(ServerCallback<List<Event>> callback) {

    }

    @Override
    public void getEventParticipants(UUID eventID, ServerCallback<List<EventParticipant>> callback) {

    }

    @Override
    public void storeEvent(Event event, ServerCallback<UUID> callback) {
        ParseEvent parseEvent = (ParseEvent)event;
        parseEvent.saveInBackground();

        callback.postResult(parseEvent.getEventID());
        invokeCallback(callback);
    }

    @Override
    public void inviteUser(User userID, Event event) {

    }

    @Override
    public void acceptInvitation(Event event) {

    }

    @Override
    public void rejectInvitation(Event event) {

    }

    @Override
    public void updateLocation(Location location) {

    }

    @Override
    public void registerLocationListener(LocationListener listener, UUID eventID) {

    }

    @Override
    public void unregisterLocationListener(LocationListener listener) {

    }
}
