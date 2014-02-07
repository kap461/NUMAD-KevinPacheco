package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import edu.neu.mobileClass.PhoneCheckAPI;

public class MainMenu extends Activity {
	//public static Data music_data;
	public static Data notification_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PhoneCheckAPI.doAuthorization(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Music.music_data = new Data(getApplicationContext());
		notification_data = new Data(getApplicationContext());
		
		setContentView(R.layout.butterflycatcher_activity_main_menu);
		Music.play(this, R.raw.cavern_short);
	}
	
	public void onPlayButtonClick(View view) {
		Intent i = new Intent(this, MainActivity.class);
    	startActivity(i);
	}
	
	public void onSettingsButtonClick(View view) {
		Intent i = new Intent(this, Settings.class);
    	startActivity(i);
	}
	
	public void onInstructionsButtonClick(View view) {
		Intent i = new Intent(this, Instructions.class);
		startActivity(i);
	}
	
	public void onQuitButtonClick(View view) {
		finish();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Music.stop(this);
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
		   Music.play(this, R.raw.cavern_short);

	}


}
