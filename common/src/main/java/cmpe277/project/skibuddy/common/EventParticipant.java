package cmpe277.project.skibuddy.common;

/**
 * Created by eh on 11/29/2015.
 */
public interface EventParticipant extends User {
    void setParticipationStatus(ParticipationStatus status);
    ParticipationStatus getParticipationStatus();
}
