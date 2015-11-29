package cmpe277.project.skibuddy.common;

import org.joda.time.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class User {
	private String name;
	private String tagline;
	private String profilePictureURL;
	private UUID id;
	private Location position;
	private List<Run> runs = new LinkedList<Run>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Location getPosition() {
		return position;
	}

	public void setPosition(Location position) {
		this.position = position;
	}

	public List<Run> getRuns() {
		return runs;
	}

	public int getTotalDistance() {
		throw new UnsupportedOperationException();
	}

	public Duration getTotalTime() {
		throw new UnsupportedOperationException();
	}

	public int getTopSpeed() {
		throw new UnsupportedOperationException();
	}
}