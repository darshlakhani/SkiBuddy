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

    UUID getUserId();

    void setUserId(UUID user);

    UUID getEventId();

    void setEventId(UUID event);

    double getDistance();

    double getTopSpeed();

    Duration getTotalTime();
}
