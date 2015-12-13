package cmpe277.project.skibuddy.common;

import java.util.UUID;

public abstract class SkiBuddyLocationListener implements Runnable {
	private UUID user;
	private String initials;
	private SkiBuddyLocation location;

	public abstract void getLocationUpdate(UUID user, String initials, SkiBuddyLocation location);

	public void postUpdate(UUID user, String initials, SkiBuddyLocation location){
		this.user = user;
		this.initials = initials;
		this.location = location;
	}

	@Override
	public void run() {
		getLocationUpdate(user,initials,location);
	}
}