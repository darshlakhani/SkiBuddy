package cmpe277.project.skibuddy;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.GeoBounds;
import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerCallback;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class RunActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Button toggleRecordButton;
    private TextView runStatus;
    private Run currentRun;
    private UUID eventId;
    private UUID runId;
    private RunUserMarkerManager markerManager = null;
    private final Server s = new ServerSingleton().getServerInstance(this);

    private boolean moveCameraOnInitialGpsLock = true;
    private final int DEFAULT_ZOOM_LEVEL = 12;

    private SkiBuddyLocationListener eventUserLocationListener;

    private class EventUserLocationListener extends SkiBuddyLocationListener {
        @Override
        public void getLocationUpdate(UUID user, String initials, SkiBuddyLocation location) {
            if(markerManager != null)
                markerManager.updateLocation(user,initials,location);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.eventUserLocationListener = new EventUserLocationListener();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        toggleRecordButton = (Button)findViewById(R.id.toggle_record_button);
        runStatus = (TextView)findViewById(R.id.record_run_status);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Start the location updates
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Toast t = Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG);
            t.show();
        }
        mapFragment.getMapAsync(this);

        // Handle incoming bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String runUUID = bundle.getString(BundleKeys.RUNID_KEY);
            if (runUUID != null) {
                runId = UUID.fromString(runUUID);

                // If we have a run to display, we should also hide the 'record button', and
                // draw the run.
                moveCameraOnInitialGpsLock = false;

                toggleRecordButton.setVisibility(View.INVISIBLE);
                s.getRun(runId, new ServerCallback<Run>() {
                    @Override
                    public void handleResult(Run result) {
                        currentRun = result;
                        drawRun(result);
                        updateStatus(result);
                        centerRun(result);
                    }
                });

            }
            String eventUUID = bundle.getString(BundleKeys.EVENTID_KEY);
            if (eventUUID != null) {
                Log.d(RunActivity.class.getName(), "Starting run activity for event " + eventUUID);
                eventId = UUID.fromString(eventUUID);
                s.registerLocationListener(eventUserLocationListener);
            }
        }

        // Handle button clicks
        final Context context = this;

        toggleRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRun == null){
                    // Not recording, so start recording
                    currentRun = ServerSingleton.createRun();

                    currentRun.setStart(DateTime.now());
                    toggleRecordButton.setText("STOP");

                    // Call updateStatus to change the status to '0m 0km/h'
                    updateStatus(currentRun);
                } else {
                    // Done with run, store it
                    Run runToStore = currentRun;
                    currentRun = null;

                    // Set end time
                    runToStore.setEnd(DateTime.now());

                    // Update status to set it to 'not recording'
                    updateStatus(null);

                    // Return button label to 'REC'
                    toggleRecordButton.setText("REC");

                    // Show user quick stats of run
                    centerRun(runToStore);
                    Toast t = Toast.makeText(context, String.format("Run distance: %.2f m", runToStore.getDistance()), Toast.LENGTH_LONG);
                    t.show();

                    // Store run
                    storeRun(runToStore);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        s.unregisterLocationListener(eventUserLocationListener);
    }

    private void storeRun(Run run){
        try {
            User currentUser = s.getAuthenticatedUser();

            // EventID will be null if none was supplied in the bundle
            run.setEventId(eventId);

            run.setUserId(currentUser.getId());
            s.storeRun(run);
        } catch (NotAuthenticatedException ex){
            Toast t = Toast.makeText(this, "User is not authenticated", Toast.LENGTH_LONG);
            t.show();

            //TODO: send user to login activity
        }
    }

    private void centerRun(Run run){
        try {
            GeoBounds bounds = new GeoBounds(run.getTrack());
            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(bounds.getSouth(), bounds.getWest()),
                                                         new LatLng(bounds.getNorth(), bounds.getEast()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 150));

        } catch (IllegalArgumentException e){
            Log.w(RunActivity.class.getName(), "Couldn't center run: " + e.getMessage());
        }
    }

    private void updateStatus(Run run){
        if(run == null){
            runStatus.setText("Not recording");
        } else {
            runStatus.setText(String.format("Distance: %.2f m, Top Speed: %.2f km/h", run.getDistance(), run.getTopSpeed()));
        }
    }

    private Polyline runLine;
    private void drawRun(Run toDraw){
        // If toDraw is null, set visibility to false
        if(toDraw == null){
            runLine.setVisible(false);
            return;
        }

        // Draw the run
        List<LatLng> points = runToLatLngList(toDraw);
        if (runLine == null) {
            PolylineOptions options = new PolylineOptions();
            options.addAll(points);
            options.color(Color.BLUE);
            runLine = mMap.addPolyline(options);
        } else {
            runLine.setPoints(points);
        }
    }

    private List<LatLng> runToLatLngList(Run input){
        LinkedList<LatLng> toReturn = new LinkedList<>();
        for (SkiBuddyLocation loc : input.getTrack())
            toReturn.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
        return toReturn;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        markerManager = new RunUserMarkerManager(mMap);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Move camera to our position if this is the first position we got
        if(moveCameraOnInitialGpsLock){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM_LEVEL));
            moveCameraOnInitialGpsLock = false;
        }

        SkiBuddyLocation newLocation = ServerSingleton.createLocation();
        newLocation.setElevation(location.getAltitude());
        newLocation.setLatitude(location.getLatitude());
        newLocation.setLongitude(location.getLongitude());

        // Extend the run, if applicable
        if (currentRun != null) {
            currentRun.extendTrack(newLocation);

            drawRun(currentRun);
            updateStatus(currentRun);
        }

        // Share the user's location, if applicable
        if (eventId != null){
            s.updateLocation(newLocation, eventId);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
