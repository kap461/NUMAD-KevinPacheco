package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

public class Instructions extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.butterflycatcher_activity_instructions);
		
	}
	
	public void onBackButtonClick(View view) {
		finish();
	}
}
