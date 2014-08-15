package com.fairlandia.ntv;

import java.util.ArrayList;
import java.util.List;

import com.fairlandia.ntv.R;
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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends FragmentActivity implements OnSeekBarChangeListener {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final LatLng FLUSHING_MEADOWS = new LatLng(40.746711, -73.841454);		// This value later centers the map roughly in the vicinity of the fairgrounds. Might be better to center to current position?
    private final List<BitmapDescriptor> mImages = new ArrayList<BitmapDescriptor>();
    private static final String[][] mapLocations = {
    	{"Travelers Insurance Pavilion","40.750085", "-73.837849"},
    	{"IBM Pavilion","40.749110", "-73.839737"},
    	{"Unisphere","40.746330","-73.845080"},
    	{"African Pavilion","40.745875","-73.844329"}
    };

    private GoogleMap mMap;
    private GroundOverlay mGroundOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        mGroundOverlay = mMap.addGroundOverlay(flushingMeadowsMap);
        
        for(String[] s: mapLocations){
        	double lat = Double.parseDouble(s[1]);
        	double lng = Double.parseDouble(s[2]);
	        mMap.addMarker(new MarkerOptions()
	                .position(new LatLng(lat, lng))
	                .title(s[0]));
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mGroundOverlay != null) {
            mGroundOverlay.setTransparency((float) progress / (float) 100);
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
    public void map(View view) {
//    	Intent intent = new Intent(this, Map.class);
//    	startActivity(intent);
    }
}
