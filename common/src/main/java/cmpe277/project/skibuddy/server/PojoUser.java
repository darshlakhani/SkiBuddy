package cmpe277.project.skibuddy.server;

import org.joda.time.Duration;

import java.util.UUID;

import cmpe277.project.skibuddy.common.Location;
import cmpe277.project.skibuddy.common.User;

class PojoUser implements User {
	private String name;
	private String tagline;
	private String profilePictureURL;
	private UUID id;
	private Location position;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getTagline() {
		return tagline;
	}

	@Override
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	@Override
	public String getProfilePictureURL() {
		return profilePictureURL;
	}

	@Override
	public void setProfilePictureURL(String profilePictureURL) {
		this.profilePictureURL = profilePictureURL;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
	}

	@Override
	public Location getPosition() {
		return position;
	}

	@Override
	public void setPosition(Location position) {
		this.position = position;
	}

	@Override
	public double getTotalDistance() {
		return 41500.00;
	}

	@Override
	public Duration getTotalTime() {
		return Duration.standardMinutes(1523);
	}

	@Override
	public double getTopSpeed() {
		return 74.2;
	}
}