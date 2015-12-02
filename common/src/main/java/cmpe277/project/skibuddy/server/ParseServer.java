package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import bolts.Continuation;
import bolts.Task;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.LocationListener;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.ParticipationStatus;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.parseobjects.ParseEvent;
import cmpe277.project.skibuddy.server.parseobjects.ParseEventParticipant;
import cmpe277.project.skibuddy.server.parseobjects.ParseParticipation;
import cmpe277.project.skibuddy.server.parseobjects.ParseUser;

/**
 * Created by eh on 11/29/2015.
 */
public class ParseServer implements Server {
    final Context context;
    private ParseUser user;

    public ParseServer(Context context){
        this.context = context;

        ParseObject.registerSubclass(ParseEvent.class);
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(ParseParticipation.class);

        Parse.initialize(context, "REO5YRRyjUfaHVNB4dplAfCRxTr8rJndVTxIOP0Q", "0yAKP0rwx6Ske9TUA4gmRXrmCUjXyUcmtFYv9ENq");
    }

    private void invokeCallback(ServerCallback callback){
        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(callback);
    }

    @Override
    public void authenticateUser(String authentication_token, final ServerCallback<User> callback) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo(ParseUser.AUTHTOKEN_FIELD, authentication_token);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if (objects.size() > 0){
                        callback.postResult(objects.get(0));
                        user = objects.get(0);
                    }
                } else {
                    Log.w("ParseServer", e.getMessage());
                }
                invokeCallback(callback);
            }
        });
    }

    @Override
    public User getAuthenticatedUser() throws NotAuthenticatedException {
        if (user == null)
            throw new NotAuthenticatedException();

        return user;
    }

    @Override
    public void getUser(UUID userID, final ServerCallback<User> callback) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereEqualTo(ParseUser.ID_FIELD, userID.toString());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if (objects.size() > 0)
                        callback.postResult(objects.get(0));
                } else {
                    Log.w("ParseServer", e.getMessage());
                }
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void storeUser(String authentication_token, User user) {
        ParseUser parseUser = (ParseUser)user;
        parseUser.setAuthToken(authentication_token);
        this.user = parseUser;
        parseUser.saveInBackground();
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
    public void getEvent(UUID eventID, final ServerCallback<Event> callback) {
        ParseQuery<ParseEvent> query = ParseQuery.getQuery(ParseEvent.class);
        query.whereEqualTo(ParseEvent.EVENTID_FIELD, eventID.toString());
        query.findInBackground(new FindCallback<ParseEvent>() {
            @Override
            public void done(List<ParseEvent> objects, ParseException e) {
                if(e == null){
                    if (objects.size() > 0)
                        callback.postResult(objects.get(0));
                } else {
                    Log.w("ParseServer", e.getMessage());
                }
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void getEvents(ServerCallback<List<EventRelation>> callback) {

    }

    @Override
    public void getEventParticipants(UUID eventID, final ServerCallback<List<EventParticipant>> callback) {
        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.EVENTID_FIELD, eventID.toString());
        query.include(ParseParticipation.EVENT_FIELD);
        query.findInBackground(new FindCallback<ParseParticipation>() {
            @Override
            public void done(List<ParseParticipation> objects, ParseException e) {
                List<EventParticipant> result = new ArrayList<>(objects.size());
                for (ParseParticipation participation : objects)
                    result.add(new ParseEventParticipant(participation));
                callback.postResult(result);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void storeEvent(Event event, final ServerCallback<UUID> callback) {
        final ParseEvent parseEvent = (ParseEvent)event;
        parseEvent.saveInBackground().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                ParseParticipation hostRelationship = new ParseParticipation();
                hostRelationship.setUser(user);
                hostRelationship.setEvent(parseEvent);
                hostRelationship.setParticipationStatus(ParticipationStatus.HOST);
                hostRelationship.saveInBackground();
                callback.postResult(parseEvent.getEventID());
                invokeCallback(callback);
                return null;
            }
        });
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
