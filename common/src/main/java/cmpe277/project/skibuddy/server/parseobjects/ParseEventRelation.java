package cmpe277.project.skibuddy.server.parseobjects;

import org.joda.time.DateTime;

import java.util.UUID;

import cmpe277.project.skibuddy.common.EventRelation;
import cmpe277.project.skibuddy.common.ParticipationStatus;

/**
 * Created by eh on 12/2/2015.
 */
public class ParseEventRelation implements EventRelation {
    private ParseParticipation participation;

    public ParseEventRelation(ParseParticipation participation){
        this.participation = participation;
    }

    @Override
    public void setParticipationStatus(ParticipationStatus status) {
        participation.setParticipationStatus(status);
    }

    @Override
    public ParticipationStatus getParticipationStatus() {
        return participation.getParticipationStatus();
    }

    @Override
    public String getName() {
        return participation.getEvent().getName();
    }

    @Override
    public void setName(String name) {
        participation.getEvent().setName(name);
    }

    @Override
    public String getDescription() {
        return participation.getEvent().getDescription();
    }

    @Override
    public void setDescription(String description) {
        participation.getEvent().setDescription(description);
    }

    @Override
    public DateTime getStart() {
        return participation.getEvent().getStart();
    }

    @Override
    public void setStart(DateTime start) {
        participation.getEvent().setStart(start);
    }

    @Override
    public DateTime getEnd() {
        return participation.getEvent().getEnd();
    }

    @Override
    public void setEnd(DateTime end) {
        participation.getEvent().setEnd(end);
    }

    @Override
    public UUID getHostId() {
        return participation.getEvent().getHostId();
    }

    @Override
    public void setHostId(UUID host) {
        participation.getEvent().setHostId(host);
    }

    @Override
    public UUID getEventID() {
        return participation.getEvent().getEventID();
    }

    @Override
    public void setEventID(UUID eventID) {
        participation.getEvent().setEventID(eventID);
    }
}
