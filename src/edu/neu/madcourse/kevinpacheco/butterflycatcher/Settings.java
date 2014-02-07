package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Settings extends Activity {
	public static String TAG = "Settings";
	private CheckBox MusicBox;
	private CheckBox NotificationBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.butterflycatcher_activity_settings);
		prepareCheckboxes();
	}
	
	public void prepareCheckboxes() {
		
		//Log.d(TAG, "music_data.getInt(music_on) = " + music_data.getInt("music_on"));
		MusicBox = (CheckBox) findViewById( R.id.checkBox1 );
		
		if (Music.music_data.contains("music_on")) {
			Log.d(TAG, "music_data = " + Music.music_data.getInt("music_on"));
			checkMusicBox(Music.music_data.getInt("music_on"));
		}
		else
			checkMusicBox(1);
		
		
		MusicBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if (isChecked) {
					Log.d(TAG, "Check box is checked.");
					Music.music_data.storeInt("music_on", 1);
			    }
			    else {
			    	Log.d(TAG, "Check box is not checked.");
			    	Music.music_data.storeInt("music_on", 0);
			    }
			}
		});
		
		NotificationBox = (CheckBox) findViewById( R.id.checkBox2 );
		
		if (MainMenu.notification_data.contains("notification_on"))
			checkNotificationBox(MainMenu.notification_data.getInt("notification_on"));
		
		NotificationBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if (isChecked) {
					Log.d(TAG, "Check box is checked.");
					MainMenu.notification_data.storeInt("notification_on", 1);
			    }
			    else {
			    	Log.d(TAG, "Check box is not checked.");
			    	MainMenu.notification_data.storeInt("notification_on", 0);
			    }
			}
		});
	}
	
	public void onClickHereButtonClick(View view) {
		Log.d(TAG, "ZAQZ");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you sure you want to clear your data?");
		builder.setMessage("Note: Clearing your data will reset your score, number of butterflies captured, and all the butterflies on your map.");
		builder.setCancelable(false);
		builder.setIcon(R.drawable.butterfly_dialog);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AppPreferences app = new AppPreferences(getApplicationContext());
				Inventory inventory = new Inventory(getApplicationContext());
				inventory.score_data.deleteAll();
				app.deleteAll();
				finish();
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void checkMusicBox(int i) {
		Log.d(TAG, "i = " + i);
		if (i == 1)
			MusicBox.setChecked(true);
		else
			MusicBox.setChecked(false);

	}
	
	private void checkNotificationBox(int i) {
		Log.d(TAG, "i = " + i);
		if (i == 1)
			NotificationBox.setChecked(true);
		else
			NotificationBox.setChecked(false);

	}
	
	public void onBackButtonClick(View view) {
		finish();
	}

}
