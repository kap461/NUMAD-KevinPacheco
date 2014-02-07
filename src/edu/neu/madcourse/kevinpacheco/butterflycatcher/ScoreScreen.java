package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ScoreScreen extends Activity {
	private Inventory inventory;
	private static String TAG = "ScoreScreen";
	private int totalcount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.butterflycatcher_activity_score_screen);
		
		inventory = new Inventory(getApplicationContext());
		updateCounters();
		updateScore();
		
		Music.music_data = new Data(getApplicationContext());
		Music.play(this, R.raw.cavern_short);
	}
	
	public void updateCounters() {
		int count = 0;
		
		for (int i = 1; i <= 16; i++) {
			if (inventory.score_data.contains(Integer.toString(i))) {
				count = inventory.score_data.getInt(Integer.toString(i));
			
				Log.d(TAG, "Textview = " + inventory.getTextView(i));
				TextView textview = (TextView)findViewById(inventory.getTextView(i));
				textview.setText(Integer.toString(count));
				totalcount = totalcount + count;
			}
			else {
				TextView textview = (TextView)findViewById(inventory.getTextView(i));
				textview.setText(Integer.toString(0));
			}
		}

	}
	
	public void updateScore() {
		int score = 0;
		
		if (inventory.score_data.contains("score"))
			score = inventory.score_data.getInt("score");
		
		TextView textview = (TextView)findViewById(R.id.textView0);
		textview.setText(Integer.toString(score));
	}
	
	public void onBackButtonClick(View view) {
		finish();
	}
	
	public void onScoreTextClick(View view) {
		String eol = System.getProperty("line.separator");
		String score = Integer.toString(inventory.score_data.getInt("score"));
		
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		
		String shareSubject = "Butterfly Catcher!";
		String shareBody = 
				"My top score is " + score + 
				" and I've captured " + totalcount + 
				" butterflies!" + eol + eol + "Download Butterfly Catcher for Android and try to beat my score!";
		
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
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
