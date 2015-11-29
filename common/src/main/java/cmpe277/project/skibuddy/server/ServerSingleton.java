package cmpe277.project.skibuddy.server;

import android.content.Context;

public class ServerSingleton {

	private Server server;

	/**
	 * Returns the server singleton
	 */
	public Server getServerInstance(Context context) {
		if (server == null){
			server = new MockServer(context);
		}
		return server;
	}
}