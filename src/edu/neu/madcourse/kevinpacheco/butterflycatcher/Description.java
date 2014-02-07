package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import edu.neu.madcourse.kevinpacheco.R.layout;
import edu.neu.madcourse.kevinpacheco.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Description extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_description);
	}
	
	public void startFinalProject(View view) {
		Intent i = new Intent(this, MainMenu.class);
		startActivity(i);
	}
}
