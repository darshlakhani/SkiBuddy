package cmpe277.project.skibuddy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    private final Bitmap.Config bmpConfig = Bitmap.Config.ARGB_8888;
    private final int circleRadius = 30;

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
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(createIcon(initials)))
                    .anchor(0.5f, 0.5f));
            markers.put(user, marker);
        }
    }


    private Bitmap createIcon(String initials){
        int size = circleRadius * 2;
        Bitmap bmp = Bitmap.createBitmap(size, size, bmpConfig);
        Canvas canvas = new Canvas(bmp);

        // As the full bitmap contains the circle, the circle's origin is at
        // (radius, radius)
        Paint circlePaint = new Paint();
        circlePaint.setARGB(255, 6, 41, 111);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setFilterBitmap(true);
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, circlePaint);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(32);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setFilterBitmap(true);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Get the height of the text as rendered to center it vertically
        Rect textBounds = new Rect();
        textPaint.getTextBounds(initials, 0, initials.length(), textBounds);

        // Again, the bitmap's center is at (radius, radius)
        canvas.drawText(initials, circleRadius, circleRadius - textBounds.exactCenterY(), textPaint);

        return bmp;
    }
}
