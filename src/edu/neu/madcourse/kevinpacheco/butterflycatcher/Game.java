package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import java.util.Random;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Game {
	private Location location;
	Context context;
	private LatLng previous = new LatLng(0, 0);
	private static final String TAG = "Game";
	private Rabble rabble;
	private static int MaxNumofButterflies = 8;
	private static int MinNumofButterflies = 3;
	private static int index = 0;
	private static GeocodeGenerator geocoder;
	private static LatLng randomLatLng = new LatLng(0, 0);
	public static Data data;
	public static Data id_data;
	private static int highestId = 0;
	public Inventory inventory;
	
	
	public Game(Context c) {
		setLocation(getLocation());
		context = c;
		previous = new LatLng(location.getLatitude(), location.getLongitude());
		rabble  = new Rabble(context);
		data = new Data(context);
		id_data = new Data(context);
		inventory = new Inventory(context);
		
	}
	
	public void play() {
		
		placeStoredButterfliesonMap();
		
		int num = getNumOfButterfliesAroundUser(getLocation());
		Log.d(TAG, "ZZZ num = " + num);
		if (num < 3) {
			Log.d(TAG, "AAA Is this happening? num = " + num);
			generateButterflies(getRandInt(MinNumofButterflies, MaxNumofButterflies) - num);
		}

	}
	
	private void setLocation(Location loc) {
		location = loc;
	}
	
	private Location getLocation() {
		return MainActivity.LOCATION;
	}
	
	public void checkPlayerLocation(Location location) {
		
		setLocation(location);
		
		LatLng current = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
		double distance = getDistance(current.latitude, current.longitude, previous.latitude, previous.longitude);
		if (distance > 0.05)
		{
			Log.d(TAG, "Distance changed = " + distance);
			Log.d(TAG, "I'm too far.");
			
			int num = getNumOfButterfliesAroundUser(getLocation());
			if (num < 3)
				generateButterflies(getRandInt(MinNumofButterflies, MaxNumofButterflies) - num);
			
			previous = current;
		}
		
		}
	
	private static double getDistance(double latA, double longA, double latB, double longB) {

	    double theDistance = (Math.sin(Math.toRadians(latA)) *
	            Math.sin(Math.toRadians(latB)) +
	            Math.cos(Math.toRadians(latA)) *
	            Math.cos(Math.toRadians(latB)) *
	            Math.cos(Math.toRadians(longA - longB)));

	   return ((Math.toDegrees(Math.acos(theDistance))) * 69.09);
	}
	
	public void capture(Location location) {
		LatLngBounds bounds = new LatLngBounds(
				new LatLng(location.getLatitude() - 0.0002, location.getLongitude() - 0.0002),
				new LatLng(location.getLatitude() + 0.0002, location.getLongitude() + 0.0002));
		
		for (int i = 0; i < rabble.getListofButterflies().size(); i++)
			Log.d(TAG, "* " + rabble.getListofButterflies().get(i).getId());
		
		for (int i = 0; i < rabble.getListofButterflies().size(); i++) {
			if (rabble.getListofButterflies().get(i).getMarker() != null)
					if (bounds.contains(rabble.getListofButterflies().get(i).getMarker().getPosition())) {
						
						inventory.updateInventory(rabble.getListofButterflies().get(i));
						rabble.removeButterfly(rabble.getListofButterflies().get(i));
						
					}
		}

	}
	
	public int getNumOfButterfliesAroundUser(Location location) {
		//count how many butterflies on screen/around user
		int count = 0;
		LatLngBounds bounds = new LatLngBounds(
				new LatLng(location.getLatitude() - 0.001, location.getLongitude() - 0.001),
				new LatLng(location.getLatitude() + 0.001, location.getLongitude() + 0.001));
		
		Log.d(TAG, "ASDF bounds = " + bounds);
		for (int i = 0; i < rabble.getListofButterflies().size(); i++) {
			if (bounds.contains(rabble.getListofButterflies().get(i).getMarker().getPosition())) {
				count++;
				Log.d(TAG, "ASDF count = " + count);
			}
				
		}
		Log.d(TAG, "ASDF About to return count = " + count);
		return count;
	}
	
	private void generateButterflies(int n) {
		if (n > 0)
			for (int i = 0; i < n; i++)
				addNewButterfly();
	}
	
	private String getSomeType() {

		int i = getRandInt(1, 1000);
		
		String type = "butterflies/11.png";

		if (i <= 200)
			type = "butterflies/4.png";
		else if (i > 201 && i <= 400)
			type = "butterflies/11.png";
		else if (i > 401 && i <= 600)
			type = "butterflies/12.png";
		else if (i > 601 && i <= 650)
			type = "butterflies/3.png";
		else if (i > 651 && i <= 700)
			type = "butterflies/5.png";
		else if (i > 701 && i <= 750)
			type = "butterflies/6.png";
		else if (i > 751 && i <= 800)
			type = "butterflies/7.png";
		else if (i > 801 && i <= 850)
			type = "butterflies/9.png";
		else if (i > 851 && i <= 900)
			type = "butterflies/13.png";
		else if (i > 901 && i <= 950)
			type = "butterflies/16.png";
		else if (i > 951 && i <= 960)
			type = "butterflies/2.png";
		else if (i > 961 && i <= 970)
			type = "butterflies/8.png";
		else if (i > 971 && i <= 980)
			type = "butterflies/14.png";
		else if (i > 981 && i <= 990)
			type = "butterflies/15.png";
		else if (i > 991 && i <= 95)
			type = "butterflies/1.gif";
		else if (i > 996 && i <= 1000)
			type = "butterflies/10.png";
		else
			type = "butterflies/11.png";
		
		return type;
	}
	
	public void addNewButterfly() {
		Log.d(TAG, "add new butterfly");
		
		randLatLng(getLocation());
		geocoder = new GeocodeGenerator(getRandLatLng(), context);
		
		LatLng geolatlng = geocoder.getLatLng();
		
		Butterfly b = new Butterfly(geolatlng, getSomeType(), getNextId());

			if (!rabble.containsButterfly(b))
			{
				Log.d(TAG,"Butterfly added to " + geolatlng + "with id = " + b.getId());
				placeButterflyOnMap(b);

			}
			else {
				Log.d(TAG,"Butterfly added to " + getRandLatLng() + "with id = " + b.getId());
				Butterfly b2 = new Butterfly(getRandLatLng(), b.getType(), b.getId());
				placeButterflyOnMap(b2);
			}
			
			if (b.getId() > highestId)
				highestId = b.getId();

			id_data.storeInt("highestButterflyId", highestId);

		}
	
	private int getNextId() {
		Log.d(TAG, "XYF index = " + index);
		return ++index;
	}
	
	private void placeButterflyOnMap(Butterfly b) {
		rabble.addButterfly(b);
	}
	
	private void randLatLng(Location location) {
		
		double lat = location.getLatitude() + randFloat();
		double lon = location.getLongitude() + randFloat();
		
		LatLng rand = new LatLng(lat, lon);
		
		setRandLatLng(rand);
	}
	
	private float randFloat() {
		return (float) (Math.random() * 0.001 - 0.0005);
	}
	
	private void setRandLatLng(LatLng latlng) {
		randomLatLng = latlng;
	}
	
	private LatLng getRandLatLng() {
		return randomLatLng;
	}
	
	private void placeStoredButterfliesonMap() {
		
		clearButterfliesList();
		
		Log.d(TAG, "Wanted to see what's up " + data.getNumOfButterfliesStored());
		
		int max = 0;
		if (id_data.contains("highestButterflyId"))
			max = id_data.getInt("highestButterflyId");
		
		for (int i = 1; i <= max; i++) {
			
			if (data.contains(i)) {
				Log.d(TAG, "What's going on here");
				rabble.addButterfly(data.getButterfly(i));
				getNextId();
			}
		}
	}
	
	private int getRandInt(int min, int max) {
		max = max - (min - 1);
		Random r = new Random();
		int a = r.nextInt(max) + min;
		Log.d(TAG, "FFFF a = " + a);
		return a;
	}
	
	private void clearButterfliesList() {
		Rabble.butterflies.clear();
	}
	
}