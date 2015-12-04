package cmpe277.project.skibuddy.server;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;

public class PojoRun implements Run {
	UUID runId;
	private LinkedList<Location> track = new LinkedList<Location>();
	private DateTime start;
	private DateTime end;
	private UUID userId;
	private UUID eventId;
	private DateTime lastLocationUpdate;
	private double totalDistance = 0.0;
	private double topSpeed;

	private final double METERS_PER_SECOND_TO_KM_PER_HR = 3.6;

	/**
	 * For new runs
	 */
	public PojoRun(){
		runId = UUID.randomUUID();
	}

	/**
	 * For existing runs
	 */
	public PojoRun(List<Location> track){
		this.track = new LinkedList<>(track);
	}

	@Override
	public UUID getRunId() {
		return runId;
	}

	@Override
	public void setRunId(UUID runId) {
		this.runId = runId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PojoRun pojoRun = (PojoRun) o;

		if (track != null ? !track.equals(pojoRun.track) : pojoRun.track != null) return false;
		if (start != null ? !start.equals(pojoRun.start) : pojoRun.start != null) return false;
		if (end != null ? !end.equals(pojoRun.end) : pojoRun.end != null) return false;
		if (userId != null ? !userId.equals(pojoRun.userId) : pojoRun.userId != null) return false;
		return !(eventId != null ? !eventId.equals(pojoRun.eventId) : pojoRun.eventId != null);

	}

	@Override
	public int hashCode() {
		int result = track != null ? track.hashCode() : 0;
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (userId != null ? userId.hashCode() : 0);
		result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
		return result;
	}

	@Override
	public List<Location> getTrack() {
		return track;
	}

	@Override
	public void extendTrack(Location newLocation) {
		// Get previous location to be able to establish distance and speed
		Location previousLocation = null;
		if (track.size() > 0)
			previousLocation = track.getLast();

		// Add our new location to the list
		track.add(newLocation);

		DateTime now = DateTime.now();

		// Calculate how far we traveled since our last update, if we had a previous location
		if (previousLocation != null) {
			double distance_traveled = distance(previousLocation, newLocation);
			totalDistance += distance_traveled;

			// If we also had a previous time, calculate our speed to see if we have a new top speed
			if (lastLocationUpdate != null) {
				double seconds = new Duration(lastLocationUpdate, now).getMillis() / 1000;
				double speed = (distance_traveled / seconds) * METERS_PER_SECOND_TO_KM_PER_HR;
				if (speed > topSpeed) topSpeed = speed;
			}
		}

		// Store the current time so we can calculate speed later
		lastLocationUpdate = now;
	}

	/**
	 * Distance in meters between locations A and B
	 */
	private static double distance(Location a, Location b){
		return Haversine.distance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
	}

	@Override
	public DateTime getStart() {
		return start;
	}

	@Override
	public void setStart(DateTime start) {
		this.start = start;
	}

	@Override
	public DateTime getEnd() {
		return end;
	}

	@Override
	public void setEnd(DateTime end) {
		this.end = end;
	}

	@Override
	public UUID getUserId() {
		return userId;
	}

	@Override
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	@Override
	public UUID getEventId() {
		return eventId;
	}

	@Override
	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public void setDistance(double distance){
		totalDistance = distance;
	}

	public void setTopSpeed(double speed){
		topSpeed = speed;
	}

	@Override
	public double getDistance() {
		return totalDistance;
	}

	@Override
	public double getTopSpeed() {
		return topSpeed;
	}

	@Override
	public Duration getTotalTime() {
		if (start == null)
			return Duration.ZERO;
		else if (end == null)
			return new Duration(start, DateTime.now());
		else
			return new Duration(start, end);
	}
}