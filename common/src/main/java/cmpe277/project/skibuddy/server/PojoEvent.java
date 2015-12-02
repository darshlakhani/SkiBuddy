package cmpe277.project.skibuddy.server;

import org.joda.time.DateTime;

import java.util.UUID;

import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.ParticipationStatus;

public class PojoEvent implements EventRelation {
	private UUID eventID;
	private String name;
	private String description;
	//private String edate;
	private DateTime start;
	private DateTime end;
	private UUID hostId;
	private ParticipationStatus participationStatus;

	@Override
	public ParticipationStatus getParticipationStatus() {
		return participationStatus;
	}

	@Override
	public void setParticipationStatus(ParticipationStatus participationStatus) {
		this.participationStatus = participationStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PojoEvent pojoEvent = (PojoEvent) o;

		if (eventID != null ? !eventID.equals(pojoEvent.eventID) : pojoEvent.eventID != null)
			return false;
		if (name != null ? !name.equals(pojoEvent.name) : pojoEvent.name != null) return false;
		if (description != null ? !description.equals(pojoEvent.description) : pojoEvent.description != null)
			return false;
		if (start != null ? !start.equals(pojoEvent.start) : pojoEvent.start != null) return false;
		if (end != null ? !end.equals(pojoEvent.end) : pojoEvent.end != null) return false;
		return !(hostId != null ? !hostId.equals(pojoEvent.hostId) : pojoEvent.hostId != null);

	}

	@Override
	public int hashCode() {
		int result = eventID != null ? eventID.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (start != null ? start.hashCode() : 0);
		result = 31 * result + (end != null ? end.hashCode() : 0);
		result = 31 * result + (hostId != null ? hostId.hashCode() : 0);
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
	public UUID getHostId() {
		return hostId;
	}

	@Override
	public void setHostId(UUID hostId) {
		this.hostId = hostId;
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