/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.neu.madcourse.kevinpacheco.boggle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.neu.madcourse.kevinpacheco.R;


public class Boggle extends Activity implements OnClickListener{
	private static final String DATA = "boardData";
	public static final String SETTINGS = "edu.neu.madcourse.kevinpacheco.boggle";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle_main);
		
		Music.play(this, R.raw.african_drums);
		
		// Set up click listeners for all the buttons
		View resumeButton = findViewById(R.id.boggle_resumeGame);
		resumeButton.setOnClickListener(this);
		resumeButton.setVisibility(resumeButton.GONE);
		View newButton = findViewById(R.id.boggle_newGame);
	    newButton.setOnClickListener(this);
	    View howtoButton = findViewById(R.id.boggle_howtoplay);
	    howtoButton.setOnClickListener(this);
	    View optionsButton = findViewById(R.id.boggle_options);
	    optionsButton.setOnClickListener(this);
	    View creditsButton = findViewById(R.id.boggle_credits);
	    creditsButton.setOnClickListener(this);
	    View quitButton = findViewById(R.id.boggle_quit);
	    quitButton.setOnClickListener(this);
	    
		this.showResume();
		this.setTitle("Boggle");
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.boggle_newGame:
			startgame(v);
	        break;
		case R.id.boggle_resumeGame:
			resumegame(v);
			break;
		case R.id.boggle_howtoplay:
			Intent i = new Intent(this, HowtoPlay.class);
			startActivity(i);
	        break;
		case R.id.boggle_options:
			Intent in = new Intent(this, Prefs.class);
			startActivity(in);
			break;
		case R.id.boggle_credits:
			credits(v);
	        break;
		case R.id.boggle_quit:
			Music.stop(this);
			finish();
	        break;
		}
	}

	public void startgame(View v) {
		View continueButton = findViewById(R.id.boggle_resumeGame);
		continueButton.setVisibility(continueButton.VISIBLE);
		Intent i = new Intent(this, Game.class);
		i.putExtra(Game.resume, false);
		startActivity(i);
	}

	public void resumegame(View v) {
		
		Intent i = new Intent(this, Game.class);
		i.putExtra(Game.resume, true);
		startActivity(i);
	}

	public void credits(View v) {
		Intent i = new Intent(this, Credits.class);
		startActivity(i);
	}

	/** Make Resume button visible if previous board data exists **/
	private void showResume() {
		Button b = (Button)findViewById(R.id.boggle_resumeGame);
		if (!getSharedPreferences(SETTINGS, MODE_PRIVATE).contains(DATA)) {
			b.setVisibility(View.GONE);
		} else {
			b.setVisibility(0);
		}
	}

}