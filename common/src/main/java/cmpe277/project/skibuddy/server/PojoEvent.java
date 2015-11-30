package cmpe277.project.skibuddy.server;

import org.joda.time.DateTime;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.User;

class PojoEvent implements Event {
	private UUID eventID;
	private String name;
	private String description;
	private DateTime start;
	private DateTime end;
	private User host;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PojoEvent event = (PojoEvent) o;

		if (eventID != null ? !eventID.equals(event.eventID) : event.eventID != null) return false;
		if (name != null ? !name.equals(event.name) : event.name != null) return false;
		if (description != null ? !description.equals(event.description) : event.description != null)
			return false;
		if (start != null ? !start.equals(event.start) : event.start != null) return false;
		if (end != null ? !end.equals(event.end) : event.end != null) return false;
		return !(host != null ? !host.equals(event.host) : event.host != null);

	}

	@Override
	public int hashCode() {
		int result = eventID != null ? eventID.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (host != null ? host.hashCode() : 0);
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
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
	public User getHost() {
		return host;
	}

	@Override
	public void setHost(User host) {
		this.host = host;
	}

	@Override
	public UUID getEventID() {
		return eventID;
	}

	@Override
	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}
}