package cmpe277.project.skibuddy.common;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class Event {
	private String name;
	private String description;
	private DateTime start;
	private DateTime end;
	private User host;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Event event = (Event) o;

		if (name != null ? !name.equals(event.name) : event.name != null) return false;
		if (description != null ? !description.equals(event.description) : event.description != null)
			return false;
		if (start != null ? !start.equals(event.start) : event.start != null) return false;
		if (end != null ? !end.equals(event.end) : event.end != null) return false;
		return !(host != null ? !host.equals(event.host) : event.host != null);

	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (host != null ? host.hashCode() : 0);
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}
}