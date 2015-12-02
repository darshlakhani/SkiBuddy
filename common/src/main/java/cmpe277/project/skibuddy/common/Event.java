package cmpe277.project.skibuddy.common;

import android.util.Log;

import com.parse.ParseObject;

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

	/*String getDate();

	void setDate(String date);
*/
	DateTime getStart();

	void setStart(DateTime start);

	DateTime getEnd();

	void setEnd(DateTime end);

	User getHost();

	void setHost(User host);

	UUID getEventID();

	void setEventID(UUID eventID);
}
	//<<<<<<< HEAD
	/*public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public User getHost() {
		return host;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public UUID getEventID() {
		return eventID;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public void setEvent()
	{
		ParseObject testObject = new ParseObject("Event");
		Log.i("Name Tag",this.name);
		System.out.print(this.name);
		testObject.put("eventName", this.name);
		testObject.saveInBackground();
	}
}
//=======
    void setEventID(UUID eventID);
}
//>>>>>>> 1b2746c41940e8edcb18e05e69c77fd3773aa087
*/