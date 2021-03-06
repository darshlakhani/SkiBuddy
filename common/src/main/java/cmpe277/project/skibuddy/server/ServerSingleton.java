package cmpe277.project.skibuddy.server;

import android.content.Context;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Event;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.parseobjects.ParseEvent;
import cmpe277.project.skibuddy.server.parseobjects.ParseUser;

public class ServerSingleton {

	private static Server server;

	/**
	 * Returns the server singleton
	 */
	public Server getServerInstance(Context context) {
		if (server == null){
			server = new ParseServer(context);
		}
		return server;
	}

	public static Event createEvent(){
		Event toReturn = new ParseEvent();
		toReturn.setEventID(UUID.randomUUID());
		return toReturn;
	}

	public static SkiBuddyLocation createLocation(){
		return new PojoLocation();
	}

	public static Run createRun(){
		return new PojoRun();
	}

	public static User createUser(){
		ParseUser parseUser = new ParseUser();
		parseUser.setId(UUID.randomUUID());
		return parseUser;
	}

}