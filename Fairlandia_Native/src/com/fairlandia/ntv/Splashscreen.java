package com.fairlandia.ntv;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends Activity {

	private static final int SPLASH_TIME = 10600;	 // 11 seconds
	
	private MediaPlayer mp;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);

    	mp = MediaPlayer.create(getApplicationContext(), R.raw.fair_song_intro);
    	mp.start(); // no need to call prepare(); create() does that for you
	
	    new Handler().postDelayed(new Runnable() {
	    	public void run() {
	            Intent mainIntent = new Intent(Splashscreen.this,
	                    MainActivity.class);
	            startActivity(mainIntent);
	
	            finish();
	            overridePendingTransition(R.anim.mainfadein,R.anim.splashfadeout);
	        }
	    }, SPLASH_TIME);
	}
	
}
