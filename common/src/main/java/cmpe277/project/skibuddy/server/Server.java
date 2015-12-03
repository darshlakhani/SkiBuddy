package cmpe277.project.skibuddy.server;

import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;

public interface Server {

	/**
	 * Returns the user object of the user that is logging in (if successful). If unsuccessful returns null.
	 */
	void authenticateUser(String authentication_token, ServerCallback<User> callback);

	/**
	 * Returns the currently logged in user.
	 * @throws cmpe277.project.skibuddy.common.NotAuthenticatedException If no user currently logged in
	 */
	User getAuthenticatedUser() throws NotAuthenticatedException;

	/**
	 * Returns the requested user. If unsuccessful returns null.
	 */
	void getUser(UUID userID, ServerCallback<User> callback);

	/**
	 * Returns a list of users whose names start with the specified string. Returns
	 * empty list when no users are found.
	 */
	void getUsersByName(String nameStartsWith, ServerCallback<List<User>> callback);

	/**
	 * Stores a user. Set UUID to 'UUID.randomUUID' to register a new user
	 */
	void storeUser(String authentication_token, User user);

	/**
	 * Returns all the runs associated with the event that is identified with the specified ID.
	 * Returns null if the user cannot access that event.
	 */
	void getRuns(UUID eventID, ServerCallback<List<Run>> callback);

	/**
	 * Returns all the runs for the specified user. If unsuccessful returns null.
	 */
	void getUserRuns(UUID userID, ServerCallback<List<Run>> callback);

	/**
	 * Persists the run on the server.
	 */
	void storeRun(Run run);

	/**
	 * Fetches the specified event. Returns null if the event couldn't be found.
	 */
	void getEvent(UUID eventID, ServerCallback<Event> callback);

	/**
	 * Returns all events that the current user is involved with (either as a participant, host, or invitee).
	 * If unauthorized, or unsuccessful, returns null.
	 */
	void getEvents(ServerCallback<List<EventRelation>> callback);

	/**
	 * Returns a list of all users that participate in the specified event.
	 * Returns null if the user isn't involved in the specified event.
	 */
	void getEventParticipants(UUID eventID, ServerCallback<List<EventParticipant>> callback);

	/**
	 * Creates or updates the specified event on the server.
	 *
	 * Returns null if the user can't update the specified event, UUID after successfully creating/updating the event
	 */
	void storeEvent(Event event, ServerCallback<UUID> callback);

	/**
	 * Invites the specified user to the specified events. Silently ignores the call if that user is already invited or participating in the event.
	 */
	void inviteUser(User userID, Event event);

	/**
	 * Accepts the invitation for the specified event.
	 */
	void acceptInvitation(Event event);

	/**
	 * Rejects the invitation for the specified event.
	 */
	void rejectInvitation(Event event);

	/**
	 * Sends a location update for the current user. Should be used while skiing in an event.
	 */
	void updateLocation(SkiBuddyLocation location);

	/**
	 * Registers a location listener for the specified event, the location listener will receive updates for all users in that event.
	 */
	void registerLocationListener(SkiBuddyLocationListener listener, UUID eventID);

	/**
	 * Unregisters the specified location listener
	 */
	void unregisterLocationListener(SkiBuddyLocationListener listener);
}