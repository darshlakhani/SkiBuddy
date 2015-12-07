package cmpe277.project.skibuddy.common;

/**
 * Created by eh on 12/2/2015.
 */
public interface EventRelation extends Event {
    void setParticipationStatus(ParticipationStatus status);
    ParticipationStatus getParticipationStatus();
}
