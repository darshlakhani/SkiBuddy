package cmpe277.project.skibuddy.common;

/**
 * Created by eh on 11/29/2015.
 */
public interface Invitation {
    User getInvitee();

    void setInvitee(User invitee);

    Event getEvent();

    void setEvent(Event event);
}
