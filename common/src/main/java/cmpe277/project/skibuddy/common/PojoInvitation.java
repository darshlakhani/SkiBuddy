package cmpe277.project.skibuddy.common;

public class PojoInvitation implements Invitation {
	private User invitee;
	private Event event;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PojoInvitation that = (PojoInvitation) o;

		if (invitee != null ? !invitee.equals(that.invitee) : that.invitee != null) return false;
		return !(event != null ? !event.equals(that.event) : that.event != null);

	}

	@Override
	public int hashCode() {
		int result = invitee != null ? invitee.hashCode() : 0;
		result = 31 * result + (event != null ? event.hashCode() : 0);
		return result;
	}

	@Override
	public User getInvitee() {

		return invitee;
	}

	@Override
	public void setInvitee(User invitee) {
		this.invitee = invitee;
	}

	@Override
	public Event getEvent() {
		return event;
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}
}