package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import edu.neu.madcourse.kevinpacheco.R;
import android.content.Context;
import android.util.Log;

public class Inventory {
	private static final String TAG = "Inventory";
	public Data score_data;
	private static int butterfly_id;
	
	public Inventory(Context context) {
		score_data = new Data(context);
	}
	
	public void updateInventory(Butterfly b) {
		
		Log.d(TAG, "SSScore: We got b.getType() = " + b.getType());
		int i = getScore(b.getType());
		updateScore(i);
		Log.d(TAG, "SSScore: butterfly_id = " + butterfly_id);
		updateCount(butterfly_id);
	}
	
	private void updateScore(int score) {
		int i = score;
		
		if (score_data.contains("score")) {
			i = score_data.getInt("score");
			i = i + score;
		}
		
		score_data.storeInt("score", i);
		
	}
	
	private void updateCount(int i) {
		int count = 1;
		
		if (score_data.contains(Integer.toString(i)))
			count = score_data.getInt(Integer.toString(i)) + 1;

		Log.d(TAG, "SSScore storing " + Integer.toString(i) + " with value of " + count);
		score_data.storeInt(Integer.toString(i), count);
		
	}
	
	public int getTextView(int i) {
		
		if (i == 4)
			return R.id.textView1;
		else if (i == 11)
			return R.id.textView2;
		else if (i == 12)
			return R.id.textView3;
		else if (i == 3)
			return R.id.textView4;
		else if (i == 5)
			return R.id.textView5;
		else if (i == 6)
			return R.id.textView6;
		else if (i == 7)
			return R.id.textView7;
		else if (i == 9)
			return R.id.textView8;
		else if (i == 13)
			return R.id.textView9;
		else if (i == 16)
			return R.id.textView10;
		else if (i == 2)
			return R.id.textView11;
		else if (i == 8)
			return R.id.textView12;
		else if (i == 14)
			return R.id.textView13;
		else if (i == 15)
			return R.id.textView14;
		else if (i == 10)
			return R.id.textView15;
		else if (i == 1)
			return R.id.textView16;
		else
			return R.id.textView1;
		
	}
	
	private int getScore(String t) {
		double perc = 1.0;
		
		Log.d(TAG, "SSScore: t = " + t);
		if (t == "butterflies/4.png") {
			perc = 0.2;
			butterfly_id = 4;
		}
		if (t == "butterflies/11.png") {
			perc = 0.2;
			butterfly_id = 11;
		}
		if (t == "butterflies/12.png") {
			perc = 0.2;
			butterfly_id = 12;
		}
		if (t == "butterflies/3.png") {
			perc = 0.05;
			butterfly_id = 3;
		}
		if (t == "butterflies/5.png") {
			perc = 0.05;
			butterfly_id = 5;
		}
		if (t == "butterflies/6.png") {
			perc = 0.05;
			butterfly_id = 6;
		}
		if (t == "butterflies/7.png") {
			perc = 0.05;
			butterfly_id = 7;
		}
		if (t == "butterflies/9.png") {
			perc = 0.05;
			butterfly_id = 9;
		}
		if (t == "butterflies/13.png") {
			perc = 0.05;
			butterfly_id = 13;
		}
		if (t == "butterflies/16.png") {
			perc = 0.05;
			butterfly_id = 16;
		}
		if (t == "butterflies/2.png") {
			perc = 0.01;
			butterfly_id = 2;
		}
		if (t == "butterflies/8.png") {
			perc = 0.01;
			butterfly_id = 8;
		}
		if (t == "butterflies/14.png") {
			perc = 0.01;
			butterfly_id = 14;
		}
		if (t == "butterflies/15.png") {
			perc = 0.01;
			butterfly_id = 15;
		}
		if (t == "butterflies/10.png") {
			perc = 0.005;
			butterfly_id = 10;
		}
		if (t == "butterflies/1.png") {
			perc = 0.005;
			butterfly_id = 1;
		}
		
		Log.d(TAG, "SSScore: 1/perc = " + 1 + "/" + perc + " = " + 1/perc);
		return (int) (1/perc);
		
	}
	
	// 1/percentage
}