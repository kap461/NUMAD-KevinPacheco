package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.neu.madcourse.kevinpacheco.R;

public class MainActivity extends FragmentActivity 
	implements OnMyLocationChangeListener {
	
	public static GoogleMap myMap = null;
	private LocationManager locationmanager;
	private static final String TAG = "MainActivity";
	public Context context = this;
	public static Location LOCATION;
	public static Game game;
	public static boolean locationFailed = false;
	public static boolean birdseyeOn = false;
	public static boolean scrolled = false;
	public static Music music;
	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);


		myMap = null;
		if(isGooglePlay()) {
			setContentView(R.layout.butterflycatcher_activity_main);
			setUpMap();
		}
		
		if (getLocation() != null) {
			game = new Game(getApplicationContext());
			game.play();
			animateCamera(getLocation(), birdseyeOn);
		}
		else
			locationFailed = true;

		Music.music_data = new Data(getApplicationContext());
		Music.play(this, R.raw.naturesounds);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.butterflycatcher_main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.menu_legalnotices:
	    	String LicenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(
	    			getApplicationContext());
	    	AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(MainActivity.this);
	    	LicenseDialog.setTitle("Legal Notices");
	    	LicenseDialog.setMessage(LicenseInfo);
	    	LicenseDialog.show();
	        return true;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isGooglePlay() {
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (status == ConnectionResult.SUCCESS)
		{
			//Toast.makeText(getApplicationContext(), "isGooglePlayServicesAvailable SUCCESS", Toast.LENGTH_LONG).show();
			return(true);
		}
		else
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
		return(false);
	}
	
	private void setUpMap() {

		if(myMap == null) {
			SupportMapFragment myMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			myMap = myMapFragment.getMap();
			myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		}
		if(myMap != null) {
			//add some code here to initialize map
			
			myMap.setMyLocationEnabled(true);
			myMap.setOnMyLocationChangeListener(this);
			
			myMap.getUiSettings().setZoomControlsEnabled(false);
			myMap.getUiSettings().setZoomGesturesEnabled(false);
			
			myMap.getUiSettings().setMyLocationButtonEnabled(false);
			
			locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
			String provider = locationmanager.getBestProvider(new Criteria(), true);
			
			Log.d(TAG, "provider = " + provider);
			
			if (provider == null || !locationmanager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
				onProviderDisabled(provider);
				
			if (provider != null) {
				Log.d(TAG, "locationmanager.getLastKnownLocation(provider) = " + locationmanager.getLastKnownLocation(provider));
				LOCATION = locationmanager.getLastKnownLocation(provider);
				
				Runnable showWaitDialog = new Runnable() {
		            @Override
		            public void run() {
		            while (LOCATION == null) {
		            	// Wait for first location fix (do nothing until LOCATION != null)
		            }
		            // After receiving first location fix dismiss the progress dialog
		            dialog.dismiss();
		            }
		            };
		            // Create a dialog to let the user know that we're waiting for a location fix
		            dialog = ProgressDialog.show(this, "Please wait.","Retrieving location...", true);
		            Thread t = new Thread(showWaitDialog);
		            t.start();

		            Log.d(TAG, "Map location = " + getLocation());
		    			
					if (getLocation() != null)
						animateCamera(getLocation(), birdseyeOn);
		
			}
			
		}
		
		myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				Log.d(TAG, "You click on " + arg0);
			}
		});
	}
	
	private Location getLocation() {
		return LOCATION;
	}

	private void setLocation(Location location) {
		LOCATION = location;
	}

	private void onProviderDisabled(String provider) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("GPS is disabled");
		builder.setMessage("Note: GPS must be enabled in order to play.");
		builder.setIcon(R.drawable.butterfly_dialog);
		builder.setCancelable(false);
		builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent startGps = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(startGps);
			}
		});
		
		builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onMyLocationChange(Location location) {
		
		setLocation(location);
		
		animateCamera(getLocation(), birdseyeOn);
		
		if (locationFailed == true) {
			Log.d(TAG, "We are here instead.");
			game = new Game(getApplicationContext());
			game.play();
			locationFailed = false;
		}

		game.checkPlayerLocation(getLocation());

	}
	
	private void animateCamera(Location location, boolean b) {
		
		LatLng locLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		
		if (!scrolled)
			if (!birdseyeOn)
				myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locLatLng, 18));
			else
				myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locLatLng, 16));

	}
	
	private void animateCamera(float z) {
			myMap.animateCamera(CameraUpdateFactory.zoomTo(z));
	}
	
	public void onTargetButtonClick(View view)
	{
		birdseyeOn = false;
		scrolled = false;
		if (getLocation() != null)
			animateCamera(getLocation(), birdseyeOn);
		
		//music.playClick(view);
	}
	
	public void onBirdseyeButtonClick(View view) {
		birdseyeOn = true;
		animateCamera(16);
		
		//music.playClick(view);
	}
	
	public void onNetButtonClick(View view) {
		Log.d(TAG, "1 REMOVE: Net button clicked");
		if (getLocation() != null)
			game.capture(getLocation());
		
		//music.playClick(view);

	}
	
	public void onInventoryButtonClick(View view) {
		Intent i = new Intent(this, ScoreScreen.class);
		startActivity(i);
		
		//music.playClick(view);
	}
	
	public void onBackButtonClick(View view) {
		finish();
	}
	
	@Override
	public void onPause()
	{
	   super.onPause();

	   if (Music.isPlaying(this))
		   Music.stop(this);

	}
	
	@Override
	public void onResume()
	{
	   super.onResume();

	   if (!Music.isPlaying(this))
		   Music.play(this, R.raw.naturesounds);

	}


}
