/***
Copyright (c) 2008-2012 CommonsWare, LLC
Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
by applicable law or agreed to in writing, software distributed under the
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.
From _The Busy Coder's Guide to Advanced Android Development_
http://commonsware.com/AdvAndroid
*/


package com.fairlandia.ntv;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fairlandia.ntv.R;

public class Video extends Activity {
	 private VideoView video;
	 private MediaController ctlr;
	 
	 @Override
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
		 getWindow().setFormat(PixelFormat.TRANSLUCENT);
		 setContentView(R.layout.video);
		 
		 // Determine which video to show based on location.
		 Intent intent = getIntent();
		 String loc = intent.getStringExtra(MainActivity.LOCATION_MESSAGE);
		 
		 String videoVar = "";
		 
		 //if(loc.equalsIgnoreCase("africa")) videoVar = "android.resource://" + getPackageName() + "/" + R.raw.africa;
		 if(loc.equalsIgnoreCase("unisphere")) videoVar = "android.resource://" + getPackageName() + "/" + R.raw.protest;
		 if(loc.equalsIgnoreCase("ibm")) videoVar = "android.resource://" + getPackageName() + "/" + R.raw.ibm_atthefair;
		 if(loc.equalsIgnoreCase("travelers")) videoVar = "android.resource://" + getPackageName() + "/" + R.raw.travelers;
		 
		 video=(VideoView)findViewById(R.id.videoView);
		 video.setVideoURI(Uri.parse(videoVar));
		 ctlr=new MediaController(this);
		 ctlr.setMediaPlayer(video);
		 video.setMediaController(ctlr);
		 video.requestFocus();
		 video.start();
	 }
	    
	    /** Called when the user clicks the video button */
	 	// Non-functional for current screen.
	    public void video(View view) {
//	    	Intent intent = new Intent(this, Video.class);
//	    	startActivity(intent);
	    }
	    
	    /** Called when the user clicks the model button */
	    public void model(View view) {
	    	// Launch a new view within this same app.
//	    	Intent intent = new Intent(this, Model.class);
//	    	startActivity(intent);
	    	
	    	// Launch a web page in the browser
//	    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//	    	startActivity(browserIntent);
	    	
	    	// Launch a new Android app.
	    	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.fairlandia");	    	
	    	startActivity(LaunchIntent);
	    }
	    
	    /** Called when the user clicks the map button */
	    public void map(View view) {
	    	Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
	    }
}

