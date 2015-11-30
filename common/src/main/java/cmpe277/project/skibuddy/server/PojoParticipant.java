package cmpe277.project.skibuddy.server;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.ParticipationStatus;

/**
 * Created by eh on 11/29/2015.
 */
class PojoParticipant extends PojoUser implements EventParticipant {
    private ParticipationStatus participationStatus;

    @Override
    public ParticipationStatus getParticipationStatus() {
        return participationStatus;
    }

    @Override
    public void setParticipationStatus(ParticipationStatus participationStatus) {
        this.participationStatus = participationStatus;
    }
}
