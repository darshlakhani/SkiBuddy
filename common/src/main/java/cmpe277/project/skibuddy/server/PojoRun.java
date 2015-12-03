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
	private User user;
	private Event event;

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

		PojoRun run = (PojoRun) o;

		if (track != null ? !track.equals(run.track) : run.track != null) return false;
		if (start != null ? !start.equals(run.start) : run.start != null) return false;
		if (end != null ? !end.equals(run.end) : run.end != null) return false;
		if (user != null ? !user.equals(run.user) : run.user != null) return false;
		return !(event != null ? !event.equals(run.event) : run.event != null);

	}

	@Override
	public int hashCode() {
		int result = track != null ? track.hashCode() : 0;
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (user != null ? user.hashCode() : 0);
		result = 31 * result + (event != null ? event.hashCode() : 0);
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
	public User getUser() {
		return user;
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public int getDistance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getTopSpeed() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Duration getTotalTime() {
		throw new UnsupportedOperationException();
	}
}