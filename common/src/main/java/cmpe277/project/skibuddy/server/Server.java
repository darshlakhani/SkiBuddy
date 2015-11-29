package cmpe277.project.skibuddy.server;

import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.LocationListener;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;

public interface Server {

	/**
	 * Returns the user object of the user that is logging in (if successful). If unsuccessful returns null.
	 */
	void authenticateUser(String authentication_token, ServerCallback<User> callback);

	/**
	 * Returns the requested user
	 */
	User getUser(UUID userID);

	/**
	 * Returns all the runs associated with the event that is identified with the specified ID. Throws NotAuthenticatedException if the user cannot access that event.
	 */
	List<Run> getRuns(UUID eventID) throws NotAuthenticatedException;

	/**
	 * Returns all the runs for the specified user
	 */
	List<Run> getUserRuns(UUID userID);

	/**
	 * Persists the run on the server. Throws NotAuthenticatedException if no user is currently logged in.
	 */
	void storeRun(Run run) throws NotAuthenticatedException;

	/**
	 * Returns all events that the current user is involved with (either as a participant, host, or invitee). Throws NotAuthenticatedException if no user is currently logged in.
	 */
	List<Event> getEvents() throws NotAuthenticatedException;

	/**
	 * Returns a list of all users that participate in the specified event. Throws NotAuthenticatedException if the user isn't involved in the specified event.
	 */
	List<User> getEventParticipants(UUID eventID) throws NotAuthenticatedException;

	/**
	 * Creates or updates the specified event on the server. Throws NotAuthenticatedException if the currently logged in user isn't the host of the event.
	 */
	UUID storeEvent(Event event) throws NotAuthenticatedException;

	/**
	 * Invites the specified user to the specified events. Silently ignores the call if that user is already invited or participating in the event. Throws NotAuthenticatedException if the currently logged in user isn't the host of the event.
	 */
	void inviteUser(User userID, Event event) throws NotAuthenticatedException;

	/**
	 * Accepts the invitation for the specified event. Throws NotAuthenticatedException if the currently logged in user wasn't invited for the specified event.
	 */
	void acceptInvitation(Event event) throws NotAuthenticatedException;

	/**
	 * Rejects the invitation for the specified event. Throws NotAuthenticatedException if the currently logged in user wasn't invited for the specified event.
	 */
	void rejectInvitation(Event event) throws NotAuthenticatedException;

	/**
	 * Sends a location update for the current user. Should be used while skiing in an event. Throws NotAuthenticatedException if no user is currently logged in.
	 */
	void updateLocation(Location location) throws NotAuthenticatedException;

	/**
	 * Registers a location listener for the specified event, the location listener will receive updates for all users in that event. Throws NotAuthenticatedException if the currently logged in user isn't part of the event.
	 */
	void registerLocationListener(LocationListener listener, UUID eventID) throws NotAuthenticatedException;

	/**
	 * Unregisters the specified location listener
	 */
	void unregisterLocationListener(LocationListener listener);
}