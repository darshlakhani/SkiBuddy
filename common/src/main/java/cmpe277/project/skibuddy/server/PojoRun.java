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

class PojoRun implements Run {
	UUID runId;
	private List<Location> track = new LinkedList<Location>();
	private DateTime start;
	private DateTime end;
	private UUID userId;
	private UUID eventId;

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

	@Override
	public double getDistance() {
		return 1423.14;
	}

	@Override
	public double getTopSpeed() {
		return 52.12;
	}

	@Override
	public Duration getTotalTime() {
		return Duration.standardSeconds(512);
	}
}