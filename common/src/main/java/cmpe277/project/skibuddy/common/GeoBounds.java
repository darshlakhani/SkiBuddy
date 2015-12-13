package cmpe277.project.skibuddy.common;

import java.util.List;

import cmpe277.project.skibuddy.server.Haversine;
import cmpe277.project.skibuddy.server.PojoLocation;

/**
 * Created by eh on 12/5/2015.
 */
public class GeoBounds {
    // These values are out of the valid range, and will always be overwritten by any
    // valid value in the constructor
    private double west = 200;
    private double east = -200;
    private double north = -100;
    private double south = 100;

    /**
     * Create a GeoBounds object for a track
     *
     * @param track The track
     * @throws IllegalArgumentException if track is null or doesn't contain any points
     */
    public GeoBounds(List<SkiBuddyLocation> track) {
        if (track == null || track.size() < 1)
            throw new IllegalArgumentException("Please make sure the track has at least one point");

        for (SkiBuddyLocation location : track) {
            double lat = location.getLatitude(),
                    lon = location.getLongitude();
            if (lon < west) west = lon;
            if (lon > east) east = lon;
            if (lat > north) north = lat;
            if (lat < south) south = lat;
        }
    }

    public double getWidth() {
        return Haversine.distance(north, west, north, east);
    }

    public double getHeight() {
        return Haversine.distance(north, west, south, west);
    }

    public SkiBuddyLocation getCenter() {
        SkiBuddyLocation location = new PojoLocation();
        location.setLongitude((west + east) / 2);
        location.setLatitude((north + south) / 2);
        return location;
    }

    public double getBoundingRadius() {
        SkiBuddyLocation center = getCenter();
        return Haversine.distance(north, west, center.getLatitude(), center.getLongitude());
    }

    public double getWest() {
        return west;
    }

    public double getEast() {
        return east;
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    /**
     * Get the smallest Google Maps zoom level that will fit the entire path
     *
     * http://stackoverflow.com/questions/6002563/android-how-do-i-set-the-zoom-level-of-map-view-to-1-km-radius-around-my-curren
     *
     * @return Maps zoom level
     */
    public int getZoomLevel() {
        double radius = getBoundingRadius();
        double scale = radius / 500;
        return (int) (16 - Math.log(scale) / Math.log(2));
    }
}