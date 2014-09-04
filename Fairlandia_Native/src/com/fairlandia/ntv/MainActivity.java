package com.fairlandia.ntv;

import java.util.ArrayList;
import java.util.List;

import com.fairlandia.ntv.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.graphics.Color;		// Used for testing only.
import android.location.Location;
import android.media.MediaPlayer;

import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;		// Used for testing only.


// TODO: Use geofencing for location determination.
// TODO: Create a location object instead of depending on an array.
// TODO: Wrap all "for development" code in a conditional to allow easy and straightforward toggling of development and production mode. (Ideally, add some key combination to production app to toggle this while running.)

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	public final static String LOCATION_MESSAGE = "com.fairlandia.ntv.LOC";

	
	private static final LatLng FLUSHING_MEADOWS = new LatLng(40.746711, -73.841454);		// This value later centers the map roughly in the vicinity of the fairgrounds. Might be better to center to current position?
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private static final String[][] mapLocations = {
    	{"Travelers Insurance Pavilion","40.750085", "-73.837849"},
    	{"IBM Pavilion","40.749110", "-73.839737"},
    	{"Unisphere","40.746330","-73.845080"},
    	{"African Pavilion","40.745875","-73.844329"}
    };			// The order of this array must be maintained for location checking to function.
    LinearLayout mGui;
    
    // State management, i.e., what pavilion zone, if any, is the user currently in?
    // Assumes user starts at none. Probably irrelevant since this is regularly updated, but not 100% accurate.
    private boolean atAfrica = false;
    private boolean atUnisphere = false;
    private boolean atIbm = false;
    private boolean atTravelers = false;

    private GoogleMap mMap;
    private GroundOverlay mGroundOverlay;		// Used to save a reference to the overlay if needed.
    private LocationClient mLocationClient;
    
    private Location currentLocation;
    private double currentLat;
    private double currentLng;
    private boolean mUpdatesRequested;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// Check if Google Play Services exists
    	int ec = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    	if (ec != ConnectionResult.SUCCESS) {
    	  GooglePlayServicesUtil.getErrorDialog(ec, this, 0).show();
    	}
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mLocationClient = new LocationClient(this, this, this);
        mUpdatesRequested = false;

        mLocationRequest= new LocationRequest();	// This can contain interval preferences - how often to query for location change, etc.
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(5);

        setUpMapIfNeeded();
        
        mGui = (LinearLayout)findViewById(R.id.gui);
        mGui.setVisibility(View.GONE);		// (Since we're assuming user starts outside any pavilion zones, I'm initializing the gui to be invisible for smoother experience. Otherwise the buttons briefly appear at launch.)
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // Connect the client.
        mLocationClient.connect();
    }
    
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mUpdatesRequested = true;	// I don't know why this goes here, but this is the location specified in the Android SDK documentation.
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FLUSHING_MEADOWS, 14));
        mMap.setMyLocationEnabled(true);

        mImages.clear();
        
        // Using variables for this to allow for easier testing and debugging.
		double n = 40.754500;
		double s = 40.737804;
		double e = -73.830640;
		double w = -73.853629;
        
        LatLng sw = new LatLng(s, w);
        LatLng ne = new LatLng(n, e);
        
        LatLngBounds flushingMeadowsBounds = new LatLngBounds(
                sw,       	// South west corner of image
        		ne);    	// North east corner of image
        GroundOverlayOptions flushingMeadowsMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fairgrounds_big_square))
                .positionFromBounds(flushingMeadowsBounds);
        mGroundOverlay = mMap.addGroundOverlay(flushingMeadowsMap);	// Save a reference in case you need to update the overlay (e.g. transparency)
        
        for(String[] str: mapLocations){
        	double lat = Double.parseDouble(str[1]);
        	double lng = Double.parseDouble(str[2]);
	        mMap.addMarker(new MarkerOptions()
	                .position(new LatLng(lat, lng))
	                .title(str[0]));
        }
        
//        // For development, add markers at the corners of the image.
//        mMap.addMarker(new MarkerOptions()
//        	.position(sw)
//        	.title("SW Corner")
//        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
//        mMap.addMarker(new MarkerOptions()
//        	.position(ne)
//        	.title("NE Corner")
//        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
//        mMap.addMarker(new MarkerOptions()
//        	.position(new LatLng(n, w))
//        	.title("NW Corner (unspecified)")
//        	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//        mMap.addMarker(new MarkerOptions()
//    		.position(new LatLng(s, e))
//    		.title("SE Corner (unspecified)")
//    		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);	
    }
    
    /** Called when the user clicks the video button */
    public void video(View view) {
    	Intent intent = new Intent(this, Video.class);
    	
    	// Pass in a nickname for the current area so video class knows which video to display.
    	String nickname = "africa";	// default to Africa, mainly for testing purposes (e.g. when not actually at any location).
    	if(atUnisphere) nickname = "unisphere";
    	if(atIbm) nickname = "ibm";
    	if(atTravelers) nickname = "travelers";
    	intent.putExtra(LOCATION_MESSAGE, nickname);
    	
    	startActivity(intent);
    }
    
    /** Called when the user clicks the model button */
    public void model(View view) {
    	// Launch a new view within this same app.
//    	Intent intent = new Intent(this, Model.class);
//    	startActivity(intent);
    	
    	// Launch a web page in the browser
//    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//    	startActivity(browserIntent);
    	
    	// Launch a new Android app.
    	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.fairlandia");
    	startActivity(LaunchIntent);
    }
    
    /** Called when the user clicks the map button */
    // Currently non-functional because the map is there by default and back navigates to it sufficiently.
    // Leaving here as it makes for a nice way to run a test by click.
    public void map(View view) {
//    	Location mCurrentLocation = mLocationClient.getLastLocation();
//    	
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//    	dbg.append(mCurrentLocation.toString());
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//    	dbg.setText(mCurrentLocation.toString());
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle connectionHint) {
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
	}

	@Override
	public void onDisconnected() {
		// Auto-generated method stub
	}

	// Note that this would be better implemented with geofencing, but for this limited scope app, I chose what seemed like the quickest route.
	@Override
    public void onLocationChanged(Location location) {
		currentLocation = location;
		
		// It is unnecessary to store the latitude and longitude separately, but it's a little easier to think about this way.
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();
		
		// Uncomment this to manually define the current location for development purposes.
		// These values place the user at the Travelers Insurance pavilion.
//		currentLat = 40.749986;
//		currentLng = -73.837948;
		// These values place the user at the IBM pavilion.
//		currentLat = 40.749233;
//		currentLng = -73.839645;
		
    	double unisphereLat = Double.parseDouble(mapLocations[2][1]);
    	double unisphereLong = Double.parseDouble(mapLocations[2][2]);
    	double travLat = Double.parseDouble(mapLocations[0][1]);
    	double travLong = Double.parseDouble(mapLocations[0][2]);
    	double ibmLat = Double.parseDouble(mapLocations[1][1]);
    	double ibmLong = Double.parseDouble(mapLocations[1][2]);
    	double africaLat = Double.parseDouble(mapLocations[3][1]);
    	double africaLong = Double.parseDouble(mapLocations[3][2]);
		
		// Based on on-site testing, the unisphere's region is defined as +/- 0.000400 degrees of latitude and longitude.
    	// The other regions are defined as 1/3rd of that (+/- 0.000133).
    	double unisphereRange = 0.000400;
    	double otherRange = 0.000133;
    	
		// For each location checks first if latitude is in range then if longitude is in range.
		// If yes, set state to be at that location.
		// If no, check next location until all checked, in which case set state to no location.
    	if((currentLat > (africaLat - otherRange) && currentLat < (africaLat + otherRange)) && (currentLng > (africaLong - otherRange) && currentLng < (africaLong + otherRange))){
			atAfrica = true;
			atUnisphere = false;
			atIbm = false;
			atTravelers = false;
		}
		else if((currentLat > (unisphereLat - unisphereRange) && currentLat < (unisphereLat + unisphereRange)) && (currentLng > (unisphereLong - unisphereRange) && currentLng < (unisphereLong + unisphereRange))){
			atAfrica = false;
			atUnisphere = true;
			atIbm = false;
			atTravelers = false;
		}
		else if((currentLat > (ibmLat - otherRange) && currentLat < (ibmLat + otherRange)) && (currentLng > (ibmLong - otherRange) && currentLng < (ibmLong + otherRange))){
			atAfrica = false;
			atUnisphere = false;
			atIbm = true;
			atTravelers = false;
		}
		else if((currentLat > (travLat - otherRange) && currentLat < (travLat + otherRange)) && (currentLng > (travLong - otherRange) && currentLng < (travLong + otherRange))){
			atAfrica = false;
			atUnisphere = false;
			atIbm = false;
			atTravelers = true;
		}
		else{
			atAfrica = false;
			atUnisphere = false;
			atIbm = false;
			atTravelers = false;
		}
		
		//atTravelers = true;	// For development only.
		// Now that location has been determined, display relevant buttons only if currently at one of the designated locations.
    	if(atAfrica || atUnisphere || atIbm || atTravelers){
    		mGui.setVisibility(View.VISIBLE);
    	}
    	else{
    		mGui.setVisibility(View.GONE);
//    		mGui.setVisibility(View.VISIBLE);	// Toggle to make gui always visible regardless of proximity to pavilion locations.
    	}
    	
		// For development purposes, show the current location.
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//		dbg.setText(currentLat + ", " + currentLng);
    }  
}
