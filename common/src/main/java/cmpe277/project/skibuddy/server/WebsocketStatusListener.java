package cmpe277.project.skibuddy.server;

/**
 * Created by eh on 12/11/2015.
 */
public interface WebsocketStatusListener {
    void onConnectionOpened();
    void onConnectionClosed(int code, String reason, boolean remote);
}
