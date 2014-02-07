package edu.neu.madcourse.kevinpacheco.persistent;

import edu.neu.madcourse.kevinpacheco.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.util.Log;

public class Multiplayer extends TabActivity {
	
	private static final String TAG = "Boggle";

	TabHost tabHost;
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Inside onCreate function");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiplayer_main);
		tabHost = (TabHost) findViewById(android.R.id.tabhost);

		// Tab for Current Games
		TabSpec currentGamesSpec = tabHost.newTabSpec("Current_Games");
		TextView currentGamesTextView = new TextView(this); 
		currentGamesSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_current_games));
		Intent currentGamesIntent = new Intent(this, Multiplayer_ActiveGames.class);
		currentGamesSpec.setContent(currentGamesIntent);

		// Tab for Received
		TabSpec receivedSpec = tabHost.newTabSpec("Received");
		receivedSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_received));
		Intent receivedIntent = new Intent(this, Multiplayer_Received_Requests.class);
		receivedSpec.setContent(receivedIntent);

		// Tab for Sent
		TabSpec sentSpec = tabHost.newTabSpec("Sent");
		TextView sentTextView = new TextView(this); 
		sentSpec.setIndicator(this.getResources().getText(R.string.multiplayer_tab_label_sent));
		Intent sentIntent = new Intent(this, Multiplayer_Sent_Requests.class);
		sentSpec.setContent(sentIntent);
		
		// Adding all TabSpec to TabHost
		tabHost.addTab(currentGamesSpec); // Adding current games tab
		tabHost.addTab(receivedSpec); // Adding received tab
		tabHost.addTab(sentSpec); // Adding sent tab

	}

	public void OnMultiplayerBackClicked(View v) {
		finish();
	}

	public void OnMultiplayerNewRequestClicked(View v) {
		Intent i = new Intent(this, Multiplayer_Sent_Requests.class);
		startActivity(i);
	}

}
