package cmpe277.project.skibuddy.server;

import android.content.Context;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;

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

	public static Event createEvent(){
		return new PojoEvent();
	}

	public static Location createLocation(){
		return new PojoLocation();
	}

	public static Run createRun(){
		return new PojoRun();
	}

	public static User createUser(){
		return new PojoUser();
	}

}