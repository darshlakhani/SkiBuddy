package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;

import com.parse.Parse;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.LocationListener;
import cmpe277.project.skibuddy.common.ParticipationStatus;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 11/28/2015.
 */
public class MockServer implements Server {
    final private Context context;
    Random random = new Random();

    public MockServer(Context context){
        this.context = context;
        Parse.initialize(context, "QY0YiXoRaSmEDYBprKbQSgUMAPX2EgYaNF4spnLt", "c0CDe7W7J4aMeWJUpeuxMCP6vBalpS6oEnyOmWmC");
    }

    @SuppressWarnings("unchecked")
    private void invokeCallback(ServerCallback callback){
        if (random.nextInt(3) < 1)
            callback.postResult(null);

        Handler mainHandler = new Handler(context.getMainLooper());
        mainHandler.post(callback);
    }

    private void doAfterRandomTimeout(final Runnable callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500 + (int)(1000 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callback.run();
            }
        }).start();
    }

    private void returnRandomUser(final ServerCallback<User> callback){
        doAfterRandomTimeout(new Runnable() {
            @Override
            public void run() {
                callback.postResult(getRandomUser());
                invokeCallback(callback);
            }
        });
    }

    private User getRandomUser(){
        User randomUser = new PojoUser();
        String[] names = {
                "John Doe",
                "Daffy Duck",
                "Joe Average",
                "Akanksha Nagpal",
                "Rajat Nagpal",
                "Darshit Lakhani",
                "Ernst Haagsman",
                "Nergal Issaie"
        };
        randomUser.setName(names[random.nextInt(names.length)]);

        String[] taglines = {
                "I suck at skiing",
                "I'm the KING!",
                "I drink more than any of y'all",
                "Race you down the mountain!",
                "Und ich flieg, flieg, flieg wie ein Flieger",
                "Training for the winter olympics",
                "On the internet nobody knows I'm a dog",
                "Say hi to your mother for me"
        };
        randomUser.setTagline(taglines[random.nextInt(taglines.length)]);
        return randomUser;
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
    public List<Run> getRuns(UUID eventID) {
        return null;
    }

    @Override
    public List<Run> getUserRuns(UUID userID) {
        return null;
    }

    @Override
    public void authenticateUser(String authentication_token, ServerCallback<User> callback) {
        returnRandomUser(callback);
    }

    @Override
    public void getUser(UUID userID, ServerCallback<User> callback) {
        returnRandomUser(callback);
    }

    @Override
    public void storeUser(String authToken, User user) {

    }

    private void returnRandomRunList(final ServerCallback<List<Run>> callback){
        doAfterRandomTimeout(new Runnable() {
            @Override
            public void run() {
                List<Run> runs = new LinkedList<Run>();
                Run run1 = new PojoRun();
                run1.setStart(new DateTime(2015,10,23,19,43));
                run1.setEnd(new DateTime(2015, 10, 23, 19, 54));
                run1.setUser(getRandomUser());
                runs.add(run1);
                callback.postResult(runs);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void getRuns(UUID eventID, ServerCallback<List<Run>> callback) {
        returnRandomRunList(callback);
    }

    @Override
    public void getUserRuns(UUID userID, ServerCallback<List<Run>> callback) {
        returnRandomRunList(callback);
    }

    @Override
    public void storeRun(Run run) {

    }

    @Override
    public void getEvents(final ServerCallback<List<Event>> callback) {
        doAfterRandomTimeout(new Runnable() {
            @Override
            public void run() {
                Event someEvent = new PojoEvent();
                someEvent.setName("Go Skiing");
                someEvent.setStart(new DateTime(2016, 1, 2, 10, 0, 0));
                someEvent.setEnd(new DateTime(2016, 1, 2, 19, 0, 0));
                someEvent.setDescription("Let's go skiing in Tahoe!");
                someEvent.setHost(getRandomUser());
                List<Event> events = new LinkedList<Event>();
                events.add(someEvent);
                callback.postResult(events);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void getEventParticipants(UUID eventID, final ServerCallback<List<EventParticipant>> callback) {
        doAfterRandomTimeout(new Runnable() {
            @Override
            public void run() {
                int userCount = random.nextInt(5);
                List<EventParticipant> users = new LinkedList<>();
                for (int i = 0; i < userCount; i++) {
                    EventParticipant toAdd = (EventParticipant)getRandomUser();
                    if (i == 0){
                        toAdd.setParticipationStatus(ParticipationStatus.HOST);
                    } else {
                        if(random.nextBoolean()){
                            toAdd.setParticipationStatus(ParticipationStatus.PARTICIPANT);
                        } else {
                            toAdd.setParticipationStatus(ParticipationStatus.INVITEE);
                        }
                    }

                    users.add(toAdd);
                }
                callback.postResult(users);
                invokeCallback(callback);
            }
        });
    }

    @Override
    public void storeEvent(Event event, final ServerCallback<UUID> callback) {
        /*doAfterRandomTimeout(new Runnable() {
            @Override
            public void run() {
                callback.postResult(UUID.randomUUID());
                invokeCallback(callback);
            }
        });*/
        ParseObject eventObject = new ParseObject("Event");
        eventObject.put("eventName", event.getName());
        eventObject.put("eventDescription", event.getDescription());
        eventObject.put("startTime", event.getStart());
        eventObject.put("endTime", event.getEnd());
        eventObject.saveInBackground();


    }

    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Override
    public List<User> getEventParticipants(UUID eventID) {
        return null;
    }

    @Override
    public UUID storeEvent(Event event) {
        return null;
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
