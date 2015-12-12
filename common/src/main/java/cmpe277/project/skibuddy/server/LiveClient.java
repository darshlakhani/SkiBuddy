package cmpe277.project.skibuddy.server;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;

/**
 * Created by eh on 12/11/2015.
 */
public class LiveClient extends WebSocketClient {
    private SkiBuddyLocationListener locationListener;
    private ConnectionClosedListener connectionClosedListener;

    public LiveClient(URI serverURI,
                      SkiBuddyLocationListener locationListener,
                      ConnectionClosedListener connectionClosedListener){
        super(serverURI);
        this.locationListener = locationListener;
        this.connectionClosedListener = connectionClosedListener;
        connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(LiveClient.class.getName(), "Connected to websocket server");
    }

    @Override
    public void onMessage(String message) {
        ObjectMapper mapper = new Mapper().getMapper();
        try {
            LocationUpdate update = mapper.readValue(message, LocationUpdate.class);
            locationListener.getLocationUpdate(update.getUserID(), update.getInitials(), update.getLocation());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdate(UUID userID, UUID eventID, SkiBuddyLocation location, String initials){
        // Create object
        LocationUpdate update = new LocationUpdate();
        update.setLatitude(location.getLatitude());
        update.setLongitude(location.getLongitude());
        update.setElevation(location.getElevation());
        update.setEventID(eventID);
        update.setUserID(userID);
        update.setInitials(initials);

        // Serialize & send
        ObjectMapper mapper = new Mapper().getMapper();
        try {
            String json = mapper.writeValueAsString(update);
            Log.d(LiveClient.class.getName(), "Sent " + json);
            send(json);
        } catch (JsonProcessingException | WebsocketNotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        connectionClosedListener.onConnectionClosed(code, reason, remote);
    }

    @Override
    public void onError(Exception ex) {

    }
}
