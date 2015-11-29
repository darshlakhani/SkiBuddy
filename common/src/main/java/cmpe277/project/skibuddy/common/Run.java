package cmpe277.project.skibuddy.common;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.LinkedList;
import java.util.List;

public class Run {
	private List<Location> track = new LinkedList<Location>();
	private DateTime start;
	private DateTime end;
	private User user;
	private Event event;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Run run = (Run) o;

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

	public List<Location> getTrack() {
		return track;
	}

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getDistance() {
		throw new UnsupportedOperationException();
	}

	public int getTopSpeed() {
		throw new UnsupportedOperationException();
	}

	public Duration getTotalTime() {
		throw new UnsupportedOperationException();
	}
}