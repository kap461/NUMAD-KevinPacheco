package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GeocodeGenerator {
	private static String address = "";
	private static LatLng geoLatLng = new LatLng(0,0);
	public static String TAG = "GEO";
	private Geocoder geoCoder;
	private List<Address> addresses;
	
	public GeocodeGenerator(LatLng latlng, Context context) {
		
		geoCoder = new Geocoder(context, Locale.getDefault());
		
        try {
            addresses = geoCoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            
            Log.d(TAG, "Number of geo locations found from locaion = " + addresses.size());
            if (addresses.size() > 0) 
                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++)
                   address += addresses.get(0).getAddressLine(i) + "\n";

            Log.d(TAG, "address = " + address);
            addresses.clear();
        }
        catch (IOException e) {                
            e.printStackTrace();
        }

		try {
            addresses = geoCoder.getFromLocationName(address, 5);

            Log.d(TAG, "Number of geo locations found from name = " + addresses.size());
            if (addresses.size() > 0) 
                geoLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

            addresses.clear();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
	
	public LatLng getLatLng() {
		return geoLatLng;
	}
	

}