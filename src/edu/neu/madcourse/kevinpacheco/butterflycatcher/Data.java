package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.util.Log;

public class Data {
	private static final String TAG = "Data";
	private AppPreferences _appPrefs;
	 
	public Data(Context context) {
		Log.d(TAG, "Do we even get here?");
		 _appPrefs = new AppPreferences(context);
	}
	
	public void storeLatLng(LatLng latlng) {
		
		float lat = (float) latlng.latitude;
		float lng = (float) latlng.longitude;
		
		_appPrefs.saveFloatBody("lat", lat);
		_appPrefs.saveFloatBody("lng", lng);
	}
	
	public LatLng getLatLng() {
		double lat = (double) _appPrefs.getSavedFloat("lat");
		double lng = (double) _appPrefs.getSavedFloat("lng");
		
		LatLng latlng = new LatLng(lat, lng);
		return latlng;
	}
	
	public void deleteAll() {
		_appPrefs.deleteAll();
	}
	
	public void storeButterfly(Butterfly b) {
		
		Log.d(TAG, "ZZZ Storing...");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "latitude" + ", " + (float) b.getLatLng().latitude + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "longitude" + ", " + (float) b.getLatLng().longitude + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "type" + ", " + b.getType() + ")");
		
		_appPrefs.saveFloatBody(Integer.toString(b.getId()) + "latitude", (float) b.getLatLng().latitude);
		_appPrefs.saveFloatBody(Integer.toString(b.getId()) + "longitude", (float) b.getLatLng().longitude);
		_appPrefs.saveStringBody(Integer.toString(b.getId()) + "type", b.getType());
		
	}
	
	public Butterfly getButterfly(int id) {
		
		double lat = (double) _appPrefs.getSavedFloat(Integer.toString(id) + "latitude");
		double lng = (double) _appPrefs.getSavedFloat(Integer.toString(id) + "longitude");
		LatLng latlng = new LatLng(lat, lng);
		
		String type = _appPrefs.getSavedString(Integer.toString(id) + "type");
		
		Log.d(TAG, "ZZZ Retrieving...");
		Log.d(TAG, "ZZZ (" + Integer.toString(id) + "latitude" + ", " + lat + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(id) + "longitude" + ", " + lng + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(id) + "type" + ", " + type + ")");
		
		Butterfly b = new Butterfly(latlng, type, id);
		return b;
	}
	
	public int getNumOfButterfliesStored() {

		int size = _appPrefs.getMapSize() / 3;
		return size;
	}
	
	public void deleteButterfly(Butterfly b) {

		Log.d(TAG, "ZZZ Deleting...");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "latitude" + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "longitude" + ")");
		Log.d(TAG, "ZZZ (" + Integer.toString(b.getId()) + "type" + ")");
		
		_appPrefs.delete(Integer.toString(b.getId()) + "latitude");
		_appPrefs.delete(Integer.toString(b.getId()) + "longitude");
		_appPrefs.delete(Integer.toString(b.getId()) + "type");

	}
	
	public void showContents() {
		_appPrefs.showAll();
	}
	
	public boolean contains(int id) {
		
		if (_appPrefs.contains(Integer.toString(id) + "latitude")) {
			Log.d(TAG, "ZZZ Found (" + Integer.toString(id) + "latitude" + ")");
			return true;
		}
		else
			return false;
	}
	
	public boolean contains(String key) {
		if (_appPrefs.contains(key))
			return true;
		else
			return false;
	}
	
	public void storeInt(String key, int i) {
		_appPrefs.saveIntBody(key, i);
	}
	
	public int getInt(String key) {
		return _appPrefs.getSavedInt(key);
	}
}