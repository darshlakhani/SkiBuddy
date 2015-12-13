package cmpe277.project.skibuddy;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.UUID;

import cmpe277.project.skibuddy.common.SkiBuddyLocation;

/**
 * This class maintains the markers for the positions of other users in the RunActivity
 */
public class RunUserMarkerManager {
    private GoogleMap mMap;
    private HashMap<UUID, Marker> markers;

    public RunUserMarkerManager(GoogleMap mMap){
        markers = new HashMap<>();
        this.mMap = mMap;
    }

    public void updateLocation(UUID user, String initials, SkiBuddyLocation location){
        if(markers.containsKey(user)){
            // Update location
            Marker marker = markers.get(user);
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            // Draw new marker
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
            markers.put(user, marker);
        }
    }
}
