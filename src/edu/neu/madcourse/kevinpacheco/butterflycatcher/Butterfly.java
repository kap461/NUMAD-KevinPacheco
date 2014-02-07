package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Butterfly {
	private LatLng latLng = new LatLng(0, 0);
	private static final String TAG = "Butterfly";
	public String type = "blue_butterfly_small.png";
	private Marker marker = null;
	int id = 0;
	
	public Butterfly(LatLng l, String t, int i) {
		Log.d(TAG, "Butterfly Class");
		setLatLng(l);
		setType(t);
		setId(i);
		
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng l) {
		latLng = l;
	}
	
	public void place() {
		
		if (MainActivity.myMap != null) {
			Log.d(TAG, "QWERTY getType() = " + getType());
			marker = MainActivity.myMap.addMarker(new MarkerOptions().position(getLatLng()).icon(BitmapDescriptorFactory.fromAsset(getType())));
			Game.data.storeButterfly(this);
		}
			
	}
	
	public Marker getMarker() {
		return marker;
	}
	
	public void setType(String t) {
		type = t;
	}
	
	public String getType() {
		return type;
	}
	
	public void setId(int i) {
		id = i;
	}
	
	public int getId() {
		return id;
	}

}