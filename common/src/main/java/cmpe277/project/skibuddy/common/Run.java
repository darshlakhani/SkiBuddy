package cmpe277.project.skibuddy.common;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;

/**
 * Created by eh on 11/29/2015.
 */
public interface Run {
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
