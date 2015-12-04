package cmpe277.project.skibuddy;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.User;
import cmpe277.project.skibuddy.server.Server;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class RunActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Button toggleRecordButton;
    private TextView runStatus;
    private Run currentRun;
    private UUID eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

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
            String eventUUID = bundle.getString(BundleKeys.EVENTID_KEY);
            if (eventUUID != null)
                eventId = UUID.fromString(eventUUID);
        }

        // Handle button clicks
        final Context context = this;

        toggleRecordButton = (Button)findViewById(R.id.toggle_record_button);
        runStatus = (TextView)findViewById(R.id.record_run_status);
        toggleRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRun == null){
                    // Not recording, so start recording
                    currentRun = ServerSingleton.createRun();
                    toggleRecordButton.setText("STOP");

                    // Call updateStatus to change the status to '0m 0km/h'
                    updateStatus(currentRun);
                } else {
                    // Done with run, store it
                    Run runToStore = currentRun;
                    currentRun = null;

                    // Update status to set it to 'not recording'
                    updateStatus(null);

                    // Return button label to 'REC'
                    toggleRecordButton.setText("REC");

                    // Show user quick stats of run
                    Toast t = Toast.makeText(context, String.format("Run distance: %.2f m", runToStore.getDistance()), Toast.LENGTH_LONG);
                    t.show();

                    // Store run
                    storeRun(runToStore);
                }
            }
        });
    }

    private void storeRun(Run run){
        Server s = new ServerSingleton().getServerInstance(this);
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
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentRun != null) {
            SkiBuddyLocation newLocation = ServerSingleton.createLocation();
            newLocation.setElevation(location.getAltitude());
            newLocation.setLatitude(location.getLatitude());
            newLocation.setLongitude(location.getLongitude());
            currentRun.extendTrack(newLocation);

            drawRun(currentRun);
            updateStatus(currentRun);
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
