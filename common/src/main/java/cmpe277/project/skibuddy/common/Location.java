package cmpe277.project.skibuddy.common;

/**
 * Created by eh on 11/29/2015.
 */
public interface Location {
    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);

    double getElevation();

    void setElevation(double elevation);
}
