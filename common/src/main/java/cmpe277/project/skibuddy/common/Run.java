package cmpe277.project.skibuddy.common;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;
import java.util.UUID;

/**
 * Created by eh on 11/29/2015.
 */
public interface Run {
    UUID getRunId();

    void setRunId(UUID runId);

    List<Location> getTrack();

    DateTime getStart();

    void setStart(DateTime start);

    DateTime getEnd();

    void setEnd(DateTime end);

    User getUser();

    void setUser(User user);

    Event getEvent();

    void setEvent(Event event);

    int getDistance();

    int getTopSpeed();

    Duration getTotalTime();
}
