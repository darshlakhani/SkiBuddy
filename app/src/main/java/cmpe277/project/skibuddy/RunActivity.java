package cmpe277.project.skibuddy;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import cmpe277.project.skibuddy.common.Run;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.server.ServerSingleton;

public class RunActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Button toggleRecordButton;
    private Run currentRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Toast t = Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG);
            t.show();
        }
        mapFragment.getMapAsync(this);

        final Context context = this;

        toggleRecordButton = (Button)findViewById(R.id.toggle_record_button);
        toggleRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRun == null){
                    // Not recording, so start recording
                    currentRun = ServerSingleton.createRun();
                    toggleRecordButton.setText("STOP");
                } else {
                    Run runToStore = currentRun;
                    currentRun = null;
                    toggleRecordButton.setText("REC");
                    Toast t = Toast.makeText(context, String.format("Run distance: %.2f m", runToStore.getDistance()), Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
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
