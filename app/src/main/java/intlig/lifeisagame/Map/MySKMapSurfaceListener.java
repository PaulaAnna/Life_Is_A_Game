package intlig.lifeisagame.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.skobbler.ngx.SKCoordinate;
import com.skobbler.ngx.SKMaps;
import com.skobbler.ngx.map.SKAnnotation;
import com.skobbler.ngx.map.SKCoordinateRegion;
import com.skobbler.ngx.map.SKMapCustomPOI;
import com.skobbler.ngx.map.SKMapPOI;
import com.skobbler.ngx.map.SKMapScaleView;
import com.skobbler.ngx.map.SKMapSurfaceListener;
import com.skobbler.ngx.map.SKMapSurfaceView;
import com.skobbler.ngx.map.SKMapViewHolder;
import com.skobbler.ngx.map.SKPOICluster;
import com.skobbler.ngx.map.SKScreenPoint;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;

/**
 * Created by Dominik on 19.04.2016.
 */
public class MySKMapSurfaceListener implements SKMapSurfaceListener {

    private SKMapSurfaceView mapView;

    /**
     * Current position provider
     */
    private SKCurrentPositionProvider currentPositionProvider;
    /**
     * Current position
     */
    private SKPosition currentPosition;

    private Context context;

    public MySKMapSurfaceListener(Context context) {
        this.context=context;
    }

    @Override
    public void onActionPan() {

    }

    @Override
    public void onActionZoom() {

    }

    @Override
    public void onSurfaceCreated(SKMapViewHolder skMapViewHolder) {
        mapView=skMapViewHolder.getMapSurfaceView();

        // display the scale view on the map
        skMapViewHolder.setScaleViewEnabled(true);
        // set the scale view’s position
        skMapViewHolder.setScaleViewPosition(0, 80, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM);
        // get the map scale object from the map holder object that contains it
        SKMapScaleView scaleView = skMapViewHolder.getScaleView();
        // set one of the color used to render the scale
        scaleView.setLighterColor(Color.argb(255, 255, 200, 200));
        // disable fade out animation on the scale view
        scaleView.setFadeOutEnabled(false);
        // set the distance units displayed to miles and feet
        scaleView.setDistanceUnit(SKMaps.SKDistanceUnitType.DISTANCE_UNIT_KILOMETER_METERS);
        position();

    }

    @Override
    public void onMapRegionChanged(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeStarted(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onMapRegionChangeEnded(SKCoordinateRegion skCoordinateRegion) {

    }

    @Override
    public void onDoubleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onSingleTap(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onRotateMap() {

    }

    @Override
    public void onLongPress(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onInternetConnectionNeeded() {

    }

    @Override
    public void onMapActionDown(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onMapActionUp(SKScreenPoint skScreenPoint) {

    }

    @Override
    public void onPOIClusterSelected(SKPOICluster skpoiCluster) {

    }

    @Override
    public void onMapPOISelected(SKMapPOI skMapPOI) {

    }

    @Override
    public void onAnnotationSelected(SKAnnotation skAnnotation) {

    }

    @Override
    public void onCustomPOISelected(SKMapCustomPOI skMapCustomPOI) {

    }

    @Override
    public void onCompassSelected() {

    }

    @Override
    public void onCurrentPositionSelected() {

    }

    @Override
    public void onObjectSelected(int i) {

    }

    @Override
    public void onInternationalisationCalled(int i) {

    }

    @Override
    public void onBoundingBoxImageRendered(int i) {

    }

    @Override
    public void onGLInitializationError(String s) {

    }

    @Override
    public void onScreenshotReady(Bitmap bitmap) {

    }
    public void position()
    {
        currentPosition=new SKPosition();
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.d("Location",location.toString());
                currentPosition.setAltitude(location.getAltitude());
                currentPosition.setCoordinate(new SKCoordinate(location.getLongitude(),location.getLatitude()));
                currentPosition.setSpeed(location.getSpeed());
                mapView.setPositionAsCurrent(currentPosition.getCoordinate(),1,true);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentPosition.setAltitude(last.getAltitude());
        currentPosition.setCoordinate(new SKCoordinate(last.getLongitude(),last.getLatitude()));
        currentPosition.setSpeed(last.getSpeed());
        mapView.setPositionAsCurrent(currentPosition.getCoordinate(),1,true);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}

