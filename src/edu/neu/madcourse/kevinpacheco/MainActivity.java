package edu.neu.madcourse.kevinpacheco;

import java.util.EmptyStackException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import edu.neu.madcourse.kevinpacheco.persistent.Multiplayer;
import edu.neu.madcourse.kevinpacheco.persistent.Multiplayer_Login_Form;
import edu.neu.madcourse.kevinpacheco.boggle.Boggle;
import edu.neu.madcourse.kevinpacheco.boggle.Credits;
import edu.neu.madcourse.kevinpacheco.butterflycatcher.Description;
import edu.neu.madcourse.kevinpacheco.sudoku.Sudoku;
import edu.neu.mobileClass.PhoneCheckAPI;

public class MainActivity extends Activity {
	private static final String  MULTI_PREF = "edu.neu.madcourse.kevinpacheco.persistent";
	private static final String PREF_USER = "prefUser";
	private static final String PREF_PASS = "prefPass";
	
	private static final String TAG = "Main";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PhoneCheckAPI.doAuthorization(this);
		
		this.setTitle("Kevin Pacheco");
	}
	
	public boolean isUserInfoAvailable(){
		SharedPreferences pref = getSharedPreferences(MULTI_PREF, MODE_PRIVATE);
		String username = pref.getString(PREF_USER, "null");
		String password = pref.getString(PREF_PASS, "null");
		boolean result = (username.equalsIgnoreCase("null") && password.equalsIgnoreCase("null"));
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/** Called when the user clicks the Persistent Boggle button **/
	public void OnMultiplayerButtonClicked(View v){

		if(isUserInfoAvailable()){
			Log.d(TAG, "IF TRUE");
			Intent intentLogin = new Intent(this, Multiplayer_Login_Form.class);
			startActivity(intentLogin);
			Log.d(TAG, "IF TRUE2");
		}else{
			Log.d(TAG, "Before");
			Intent intentMultiplayer = new Intent(this, Multiplayer.class);
			Log.d(TAG, "During");
			startActivity(intentMultiplayer);
			Log.d(TAG, "After");
		}
	}
	
	/** Called when the user clicks the Team Members button **/
	public void show_info(View view) {
	    Intent intent = new Intent(this, TeamMembers.class);
	    startActivity(intent);
	}
	
	/** Called when the user clicks the Sudoku button **/
	public void start_sudoku(View view) {
		Intent intent = new Intent(this, Sudoku.class);
    	startActivity(intent);
	}
	
	/** Called when the user clicks the Boggle button **/
    public void OnBoggleButtonClicked(View v) {
    	Intent i = new Intent(this, Boggle.class);
    	startActivity(i);
    }
	
	/** Called when the user clicks the Quit button **/
	public void exit(View view) {
		finish();
	}
	
	/** Called when the user clicks the Log Out button **/
	public void logOut(View view) {
		Editor editor = 
		this.getSharedPreferences(MULTI_PREF, this.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
	
	/** Called when the user clicks the Acknowledgments button **/
	public void onAcknowledgmentsButtonClick(View view) {
		Intent i = new Intent(this, Credits.class);
		startActivity(i);
	}
	
	/** Called when the user clicks the Final Project button **/
	public void onFinalProjectButtonClickClick(View view) {
		Intent i = new Intent(this, Description.class);
		startActivity(i);
	}
	
	public void die(View view) {
		throw new EmptyStackException();
	}
}
