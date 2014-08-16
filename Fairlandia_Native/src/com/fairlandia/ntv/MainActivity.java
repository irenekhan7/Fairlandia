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
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public final static String LOCATION_MESSAGE = "com.fairlandia.ntv.LOC";

	
	private static final LatLng FLUSHING_MEADOWS = new LatLng(40.746711, -73.841454);		// This value later centers the map roughly in the vicinity of the fairgrounds. Might be better to center to current position?
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private static final String[][] mapLocations = {
    	{"Travelers Insurance Pavilion","40.750085", "-73.837849"},
    	{"IBM Pavilion","40.749110", "-73.839737"},
    	{"Unisphere","40.746330","-73.845080"},
    	{"African Pavilion","40.745875","-73.844329"}
    };
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

        mLocationRequest = new LocationRequest();	// This can contain interval preferences - how often to query for location change, etc.
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
        
        LatLngBounds flushingMeadowsBounds = new LatLngBounds(
                new LatLng(40.737304, -73.853629),       	// South west corner of image
        		new LatLng(40.754, -73.83034));    		 	// North east corner of image
        GroundOverlayOptions flushingMeadowsMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fairgrounds_big_square))
                .positionFromBounds(flushingMeadowsBounds);
        mGroundOverlay = mMap.addGroundOverlay(flushingMeadowsMap);	// Save a reference in case you need to update the overlay (e.g. transparency)
        
        for(String[] s: mapLocations){
        	double lat = Double.parseDouble(s[1]);
        	double lng = Double.parseDouble(s[2]);
	        mMap.addMarker(new MarkerOptions()
	                .position(new LatLng(lat, lng))
	                .title(s[0]));
        }
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
    	String nickname = "";
    	if(atAfrica) nickname = "africa";
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
    // I'm presently using it to test location.
    public void map(View view) {
    	Location mCurrentLocation = mLocationClient.getLastLocation();
//    	
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//    	dbg.append(mCurrentLocation.toString());
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//    	dbg.setText(mCurrentLocation.toString());
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}

	// Note that this would be better implemented with geofencing, but for this limited scope app, I chose what seemed like the quickest route.
	@Override
    public void onLocationChanged(Location location) {
		currentLocation = location;
		
		// It is unnecessary to store the latitude and longitude separately, but it's a little easier to think about this way.
		currentLat = location.getLatitude();
		currentLng = location.getLongitude();
		
		// Uncomment this to manually define the current location for development purposes.
//		currentLat = 40.7490111;
//		currentLng = -73.839838;
		
		// For each location checks first if latitude is in range then if longitude is in range.
		// If yes, set state to be at that location.
		// If no, check next location until all checked, in which case set state to no location.
		// This would be better served by checking against the array of locations instead of hard-coding the limits.
		// (Limits are all +/- 0.000100 of the location's coordinates.)
		if((currentLat > 40.745775 && currentLat < 40.745975) && (currentLng > -73.844429 && currentLng < -73.844229)){
		//if((currentLat > 42.730993 && currentLat < 42.731103) && (currentLng > -73.688321 && currentLng < -73.688121)){		// Places the African pavilion near Broadway and 5th in Troy, NY for testing purposes.
			atAfrica = true;
			atUnisphere = false;
			atIbm = false;
			atTravelers = false;
		}
		else if ((currentLat > 40.746230 && currentLat < 40.746430) && (currentLng > -73.845180 && currentLng < -73.844980)){
			atAfrica = false;
			atUnisphere = true;
			atIbm = false;
			atTravelers = false;			
		}
		else if ((currentLat > 40.749010 && currentLat < 40.749210) && (currentLng > -73.839837 && currentLng < -73.839637)){
			atAfrica = false;
			atUnisphere = false;
			atIbm = true;
			atTravelers = false;
		}
		else if ((currentLat > 40.749985 && currentLat < 40.750185) && (currentLng > -73.837949 && currentLng < -73.837749)){
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
		    	
		// Now that location has been determined, display relevant buttons only if currently at one of the designated locations.
    	if(atAfrica || atUnisphere || atIbm || atTravelers){
    		mGui.setVisibility(View.VISIBLE);
    	}
    	else{
    		mGui.setVisibility(View.GONE);
    	}
    	
		// For development purposes, show the current location.
//    	TextView dbg = (TextView) findViewById(R.id.debug_text);
//    	dbg.setText(currentLat + ", " + currentLng);
    }
    
}