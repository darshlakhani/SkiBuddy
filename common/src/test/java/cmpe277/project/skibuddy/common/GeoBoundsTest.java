package cmpe277.project.skibuddy.common;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import cmpe277.project.skibuddy.server.PojoLocation;

import static org.junit.Assert.*;

/**
 * Created by eh on 12/6/2015.
 */
public class GeoBoundsTest {
    GeoBounds sut;
    SkiBuddyLocation seattle;
    SkiBuddyLocation losAngeles;
    SkiBuddyLocation newYorkCity;
    SkiBuddyLocation miami;

    @Before
    public void setUp() throws Exception {
        List<SkiBuddyLocation> track = new LinkedList<>();

        seattle = new PojoLocation();
        seattle.setLatitude(47.4498889);
        seattle.setLongitude(-122.3117778);
        seattle.setElevation(131.8);
        track.add(seattle);

        losAngeles = new PojoLocation();
        losAngeles.setLatitude(33.9425003);
        losAngeles.setLongitude(-118.4080736);
        losAngeles.setElevation(38.9);
        track.add(losAngeles);

        newYorkCity = new PojoLocation();
        newYorkCity.setLatitude(40.6399261);
        newYorkCity.setLongitude(-73.7786938);
        newYorkCity.setElevation(3.9);
        track.add(newYorkCity);

        miami = new PojoLocation();
        miami.setLatitude(25.7953611);
        miami.setLongitude(-80.2901111);
        miami.setElevation(2.6);
        track.add(miami);

        sut = new GeoBounds(track);
    }

    private final double M_IN_KM = 1000;

    @Test
    public void testGetWidth() throws Exception {
        assertEquals(3600 * M_IN_KM, Math.round(sut.getWidth()), 20 * M_IN_KM);
    }

    @Test
    public void testGetHeight() throws Exception {
        assertEquals(2403 * M_IN_KM, Math.round(sut.getHeight()), 20 * M_IN_KM);
    }

    @Test
    public void testGetCenter() throws Exception {
        SkiBuddyLocation center = sut.getCenter();

        // the center of the US is in Kansas, let's verify we're near Kansas
        assertTrue("Center calculated south of KS", center.getLatitude() > 36);
        assertTrue("Center calculated north of KS", center.getLatitude() < 40);
        assertTrue("Center calculated east of KS", center.getLongitude() < -94);
        assertTrue("Center calculated west of KS", center.getLongitude() > -102);
    }

    @Test
    public void testGetBoundingRadius() throws Exception {
        assertEquals(2330 * M_IN_KM, sut.getBoundingRadius(), 20 * M_IN_KM);
    }

    @Test
    public void testGetWest() throws Exception {
        assertEquals(seattle.getLongitude(), sut.getWest(), 1e-3);
    }

    @Test
    public void testGetEast() throws Exception {
        assertEquals(newYorkCity.getLongitude(), sut.getEast(), 1e-3);
    }

    @Test
    public void testGetNorth() throws Exception {
        assertEquals(seattle.getLatitude(), sut.getNorth(), 1e-3);
    }

    @Test
    public void testGetSouth() throws Exception {
        assertEquals(miami.getLatitude(), sut.getSouth(), 1e-3);
    }

    @Test
    public void testZoomLevel() throws Exception{
        assertEquals(3, sut.getZoomLevel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull(){
        GeoBounds bounds = new GeoBounds(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyList(){
        GeoBounds bounds = new GeoBounds(new LinkedList<SkiBuddyLocation>());
    }
}