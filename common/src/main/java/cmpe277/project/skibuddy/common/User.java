package cmpe277.project.skibuddy.common;

import org.joda.time.Duration;

import java.util.List;
import java.util.UUID;

/**
 * Created by eh on 11/29/2015.
 */
public interface User {
    String getName();

    void setName(String name);

    String getTagline();

    void setTagline(String tagline);

    String getProfilePictureURL();

    void setProfilePictureURL(String profilePictureURL);

    UUID getId();

    void setId(UUID id);

    Location getPosition();

    void setPosition(Location position);

    List<Run> getRuns();

    double getTotalDistance();

    Duration getTotalTime();

    double getTopSpeed();
}
