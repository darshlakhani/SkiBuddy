package cmpe277.project.skibuddy.server.parseobjects;

import org.joda.time.Duration;

import java.util.UUID;

import cmpe277.project.skibuddy.common.EventParticipant;
import cmpe277.project.skibuddy.common.ParticipationStatus;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;

/**
 * Created by eh on 12/2/2015.
 */
public class ParseEventParticipant implements EventParticipant {

    private ParseParticipation participation;

    public ParseEventParticipant(ParseParticipation participation){
        this.participation = participation;
    }

    @Override
    public String getName() {
        return participation.getUser().getName();
    }

    @Override
    public void setName(String name) {
        participation.getUser().setName(name);
    }

    @Override
    public String getTagline() {
        return participation.getUser().getTagline();
    }

    @Override
    public void setTagline(String tagline) {
        participation.getUser().setTagline(tagline);
    }

    @Override
    public String getProfilePictureURL() {
        return participation.getUser().getProfilePictureURL();
    }

    @Override
    public void setProfilePictureURL(String profilePictureURL) {
        participation.getUser().setProfilePictureURL(profilePictureURL);
    }

    @Override
    public UUID getId() {
        return participation.getUser().getId();
    }

    @Override
    public void setId(UUID id) {
        participation.getUser().setId(id);
    }

    @Override
    public SkiBuddyLocation getPosition() {
        return participation.getUser().getPosition();
    }

    @Override
    public void setPosition(SkiBuddyLocation position) {
        participation.getUser().setPosition(position);
    }

    @Override
    public double getTotalDistance() {
        return participation.getUser().getTotalDistance();
    }

    @Override
    public Duration getTotalTime() {
        return participation.getUser().getTotalTime();
    }

    @Override
    public double getTopSpeed() {
        return participation.getUser().getTopSpeed();
    }

    @Override
    public void setParticipationStatus(ParticipationStatus status) {
        participation.setParticipationStatus(status);
    }

    @Override
    public ParticipationStatus getParticipationStatus() {
        return participation.getParticipationStatus();
    }
}
