package cmpe277.project.skibuddy.common;

import java.util.UUID;

public interface LocationListener {
	void getLocationUpdate(UUID user, SkiBuddyLocation location);
}