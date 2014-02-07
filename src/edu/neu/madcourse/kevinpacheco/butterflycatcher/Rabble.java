package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class Rabble {
	private static final String TAG = "Rabble";
	Context context;
	static List<Butterfly> butterflies = new ArrayList<Butterfly>();
	//private Data data;
	
	public Rabble(Context c) {
		context = c;
		//data = new Data(context);
	}
	
	public Location getLocation() {
		return MainActivity.LOCATION;
	}

	public void addButterfly(Butterfly b) {
		b.place();
		butterflies.add(b);
	}
	
	public boolean containsButterfly(Butterfly b) {
		LatLngBounds bounds = new LatLngBounds(
				new LatLng(b.getLatLng().latitude - 0.0001, b.getLatLng().longitude - 0.0001),
				new LatLng(b.getLatLng().latitude + 0.0001, b.getLatLng().longitude + 0.0001));
		
		Log.d(TAG, "Starting new containsButterfly()");
		for (int i = 0; i < butterflies.size(); i++) {
			
			Log.d(TAG, "TTT is " + butterflies.get(i).getLatLng() + " equal to ");
			Log.d(TAG, "TTT " + b.getLatLng());
			if (bounds.contains(butterflies.get(i).getLatLng())) {
				Log.d(TAG, "TTT Found one in the same place id = " + b.getId());
				return true;
			}
		}
		
		return false;
	}
	
	public List<Butterfly> getListofButterflies() {
		return butterflies;
	}
	
	public void removeButterfly(Butterfly b) {
		Log.d(TAG, "ZZZ Deleting butterfly");
		butterflies.remove(b);
		b.getMarker().remove();
		Game.data.deleteButterfly(b);
	}

}