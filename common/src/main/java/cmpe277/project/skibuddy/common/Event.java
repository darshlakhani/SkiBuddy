package cmpe277.project.skibuddy.common;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by eh on 11/29/2015.
 */
public interface Event {
    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    DateTime getStart();

    void setStart(DateTime start);

    DateTime getEnd();

    void setEnd(DateTime end);

    User getHost();

    void setHost(User host);

    UUID getEventID();

    void setEventID(UUID eventID);
}
