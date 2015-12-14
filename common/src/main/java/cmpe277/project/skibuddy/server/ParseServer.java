package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import bolts.Continuation;
import bolts.Task;
import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.ParticipationStatus;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.parseobjects.ParseEvent;
import cmpe277.project.skibuddy.server.parseobjects.ParseEventParticipant;
import cmpe277.project.skibuddy.server.parseobjects.ParseEventRelation;
import cmpe277.project.skibuddy.server.parseobjects.ParseParticipation;
import cmpe277.project.skibuddy.server.parseobjects.ParseRun;
import cmpe277.project.skibuddy.server.parseobjects.ParseUser;

/**
 * Created by eh on 11/29/2015.
 */
public class ParseServer implements Server {
    final Context context;
    private ParseUser user;
    private UpdateClient updateClient;

    public ParseServer(Context context){
        this.context = context;
        this.updateClient = new UpdateClient(context, this);

        ParseObject.registerSubclass(ParseEvent.class);
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(ParseParticipation.class);
        ParseObject.registerSubclass(ParseRun.class);

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
                if (e == null) {
                    if (objects.size() > 0) {
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
                if (e == null) {
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
    public void getUsersByName(String nameStartsWith, final ServerCallback<List<User>> callback) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereContains(ParseUser.NAME_LOWERCASE_FIELD, nameStartsWith.toLowerCase());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null){
                    if (objects.size() > 0)
                        callback.postResult(new LinkedList<User>(objects));
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

    private void runQuery(ParseQuery<ParseRun> query, final ServerCallback<List<Run>> callback){
        query.findInBackground(new FindCallback<ParseRun>() {
            @Override
            public void done(List<ParseRun> objects, ParseException e) {
                List<Run> runs = new LinkedList<Run>();
                for (ParseRun run : objects) {
                    try {
                        runs.add(run.get());
                    } catch (IOException io) {
                        Log.w(ParseServer.class.getName(), "Couldn't decode run: " + io.getMessage());
                    }
                }
                callback.postResult(runs);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void getRuns(UUID eventID, final ServerCallback<List<Run>> callback) {
        ParseQuery<ParseRun> query = ParseQuery.getQuery(ParseRun.class);
        query.whereEqualTo(ParseRun.EVENTID_FIELD, eventID.toString());
        runQuery(query, callback);
    }

    @Override
    public void getUserRuns(UUID userID, ServerCallback<List<Run>> callback) {
        ParseQuery<ParseRun> query = ParseQuery.getQuery(ParseRun.class);
        query.whereEqualTo(ParseRun.USERID_FIELD, userID.toString());
        runQuery(query, callback);
    }

    @Override
    public void storeRun(Run run) {
        ParseRun newRun = new ParseRun();
        newRun.store(run);
        newRun.saveInBackground();

        // After adding the run, we should update the user's stats
        user.incrementTotalDistance(run.getDistance());
        user.incrementTotalTime(run.getTotalTime());
        if(run.getTopSpeed() > user.getTopSpeed())
            user.setTopSpeed(run.getTopSpeed());

        user.saveInBackground();
    }

    @Override
    public void getRun(final UUID runId, final ServerCallback<Run> callback) {
        ParseQuery<ParseRun> query = ParseQuery.getQuery(ParseRun.class);
        query.whereEqualTo(ParseRun.RUNID_FIELD, runId.toString());
        query.findInBackground(new FindCallback<ParseRun>() {
            @Override
            public void done(List<ParseRun> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0)
                        try {
                            callback.postResult(objects.get(0).get());
                        } catch (IOException e1) {
                            Log.w(ParseServer.class.getName(), "Couldn't decode run");
                        }
                    else {
                        Log.w(ParseServer.class.getName(), "Couldn't find run for ID " + runId.toString());
                    }
                } else {
                    Log.w(ParseServer.class.getName(), "Parse exception: " + e.getMessage());
                }
            }
        });
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
    public void getEvents(final ServerCallback<List<EventRelation>> callback) {
        // No exception is defined on this function, so we can only ignore a call if
        // no user is logged in.
        if (user == null) {
            Log.w(ParseServer.class.getName(), "User not logged in, exception should be thrown here really...");
            return;
        }

        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.USERID_FIELD, user.getId().toString());
        query.include(ParseParticipation.EVENT_FIELD);
        query.findInBackground(new FindCallback<ParseParticipation>() {
            @Override
            public void done(List<ParseParticipation> objects, ParseException e) {
                List<EventRelation> result = new ArrayList<>(objects.size());
                for (ParseParticipation participation : objects)
                    result.add(new ParseEventRelation(participation));
                callback.postResult(result);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void getEventParticipants(UUID eventID, final ServerCallback<List<EventParticipant>> callback) {
        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.EVENTID_FIELD, eventID.toString());
        query.include(ParseParticipation.USER_FIELD);
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
    public void inviteUser(final User user, final Event event) {
        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.EVENTID_FIELD, event.getEventID().toString());
        query.whereEqualTo(ParseParticipation.USERID_FIELD, user.getId().toString());
        query.findInBackground(new FindCallback<ParseParticipation>() {
            @Override
            public void done(List<ParseParticipation> objects, ParseException e) {
                if(objects.size() == 0){
                    ParseParticipation participation = new ParseParticipation();
                    participation.setUser((ParseUser)user);
                    participation.setEvent((ParseEvent)event);
                    participation.setParticipationStatus(ParticipationStatus.INVITEE);
                    participation.saveInBackground();
                }
            }
        });
    }

    @Override
    public void acceptInvitation(Event event) {
        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.EVENTID_FIELD, event.getEventID().toString());
        query.whereEqualTo(ParseParticipation.USERID_FIELD, user.getId().toString());
        query.findInBackground(new FindCallback<ParseParticipation>() {
            @Override
            public void done(List<ParseParticipation> objects, ParseException e) {
                if(e == null && objects.size() == 1){
                    ParseParticipation invitationToAccept = objects.get(0);
                    invitationToAccept.setParticipationStatus(ParticipationStatus.PARTICIPANT);
                    invitationToAccept.saveInBackground();
                }
            }
        });
    }

    @Override
    public void rejectInvitation(Event event) {
        ParseQuery<ParseParticipation> query = ParseQuery.getQuery(ParseParticipation.class);
        query.whereEqualTo(ParseParticipation.EVENTID_FIELD, event.getEventID().toString());
        query.whereEqualTo(ParseParticipation.USERID_FIELD, user.getId().toString());
        query.findInBackground(new FindCallback<ParseParticipation>() {
            @Override
            public void done(List<ParseParticipation> objects, ParseException e) {
                if(e == null && objects.size() == 1){
                    ParseParticipation invitationToAccept = objects.get(0);
                    invitationToAccept.setParticipationStatus(ParticipationStatus.PARTICIPANT);
                    invitationToAccept.deleteInBackground();
                }
            }
        });
    }

    @Override
    public void updateLocation(SkiBuddyLocation location, UUID eventID) {
        updateClient.updateLocation(location, eventID);
    }

    @Override
    public void registerLocationListener(SkiBuddyLocationListener listener) {
        updateClient.registerLocationListener(listener);
    }

    @Override
    public void unregisterLocationListener(SkiBuddyLocationListener listener) {
        updateClient.unregisterLocationListener(listener);
    }
}
