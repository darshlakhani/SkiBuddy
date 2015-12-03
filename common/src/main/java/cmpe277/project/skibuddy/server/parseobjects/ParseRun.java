package cmpe277.project.skibuddy.server.parseobjects;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.server.Mapper;
import cmpe277.project.skibuddy.server.PojoLocation;
import cmpe277.project.skibuddy.server.PojoRun;

/**
 * Created by eh on 12/2/2015.
 */
@ParseClassName("Run")
public class ParseRun extends ParseObject {
    public static final String START_FIELD = "START";
    public static final String END_FIELD = "END";
    public static final String RUNID_FIELD = "RUNID";
    public static final String USERID_FIELD = "USERID";
    public static final String EVENTID_FIELD = "EVENTID";
    public static final String TRACK_FIELD = "TRACK";
    public static final String TOPSPEED_FIELD = "TOP_SPEED";
    public static final String DISTANCE_FIELD = "DISTANCE";

    private static ObjectMapper mapper = new Mapper().getMapper();

    public void store(Run run){
        put(START_FIELD, run.getStart().toDate());
        put(END_FIELD, run.getEnd().toDate());
        put(USERID_FIELD, run.getUserId().toString());
        put(RUNID_FIELD, run.getRunId().toString());
        put(TOPSPEED_FIELD, run.getTopSpeed());
        put(DISTANCE_FIELD, run.getDistance());

        if(run.getEventId() != null)
            put(EVENTID_FIELD, run.getEventId().toString());

        try{
            String track_json = mapper.writeValueAsString(run.getTrack());
            put(TRACK_FIELD, track_json);
        } catch (Exception e) {
            Log.w(ParseRun.class.getName(), e.getMessage());
        }
    }

    public Run get() throws IOException {
        List<Location> track = mapper.readValue(getString(TRACK_FIELD),
                new TypeReference<List<PojoLocation>>(){});

        PojoRun run = new PojoRun(track);

        String eventIdString = getString(EVENTID_FIELD);
        if(eventIdString != null && !eventIdString.isEmpty())
            run.setEventId(UUID.fromString(eventIdString));

        run.setStart(new DateTime(getDate(START_FIELD)));
        run.setEnd(new DateTime(getDate(END_FIELD)));
        run.setTopSpeed(getDouble(TOPSPEED_FIELD));
        run.setDistance(getDouble(DISTANCE_FIELD));
        run.setUserId(UUID.fromString(getString(USERID_FIELD)));
        run.setRunId(UUID.fromString(getString(RUNID_FIELD)));

        return run;
    }
}
