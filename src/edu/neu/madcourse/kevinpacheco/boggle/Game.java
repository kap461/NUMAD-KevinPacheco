/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.neu.madcourse.kevinpacheco.boggle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.kevinpacheco.R;

public class Game extends Activity implements OnClickListener {
	public static final String boggle = "boggle";
	private static final String dice_dat = "dice" ;
	private static final String points = "points";
	private static final String words = "words";
	private static final String time = "time";
	public static final String resume = "resume";
	private static final String TAG = "BoggleGame";
	
	private SharedPreferences pref;
	private int boardSize = 5;
	protected int actualScore = 0;
	public String gameLetters;
	private String pause_board; 
	private ArrayList<String> w = new ArrayList<String>();
	protected int actualTime = 120;
	protected int id = 1;
	
	
	private Handler h = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			this.sendMessageDelayed(Message.obtain(this, id),1000);
			if(actualTime == 0 )
			{
				h.removeMessages(id);
				time_func();
				timeUp();

			}
			else
			{
				actualTime -= 1;
				time_func();
				timeUp();
			}
		} 
	};

	private TextView time_textView;

	private static List<Integer> dice_array = new ArrayList<Integer>();

	/* Official Boggle dice */
	private String[] dice_set = new String[]{
			"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
			"BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT",
			"DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
	};

	/* Array of all buttons */
	private static final int[] BUTTONS = {
		R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
		R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_10,
		R.id.button_11, R.id.button_12, R.id.button_13, R.id.button_14, R.id.button_15,
		R.id.button_16, R.id.button_17, R.id.button_18, R.id.button_19, R.id.button_20,
		R.id.button_21, R.id.button_22, R.id.button_23, R.id.button_24, R.id.button_25
	};

	protected void setgameLetters(String l){ this.gameLetters = l; }
	
	protected String getgameLetters(){ return gameLetters; }

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.boggle_game);
		if(getIntent().getBooleanExtra(resume, false) == false){
			getSharedPreferences(boggle, MODE_PRIVATE).edit().clear().commit();
		}

		String l = getLetters();
		setgameLetters(l);
		pause_board = l;
		
		pref = this.getSharedPreferences(boggle, MODE_PRIVATE);
		
		// Set Default score;
		setPoints(actualScore);

		time_textView = (TextView) findViewById(R.id.boggle_time);
		time_func();
		h.sendMessageDelayed(Message.obtain(h, id), 1000);

		// Set up click listeners for all the buttons
		View b1 = findViewById(R.id.button_1);
		b1.setOnClickListener((OnClickListener) this);
		View b2 = findViewById(R.id.button_2);
		b2.setOnClickListener((OnClickListener) this);
		View b3 = findViewById(R.id.button_3);
		b3.setOnClickListener((OnClickListener) this);
		View b4 = findViewById(R.id.button_4);
		b4.setOnClickListener((OnClickListener) this);
		View b5 = findViewById(R.id.button_5);
		b5.setOnClickListener((OnClickListener) this);
		View b6 = findViewById(R.id.button_6);
		b6.setOnClickListener((OnClickListener) this);
		View b7 = findViewById(R.id.button_7);
		b7.setOnClickListener((OnClickListener) this);
		View b8 = findViewById(R.id.button_8);
		b8.setOnClickListener((OnClickListener) this);
		View b9 = findViewById(R.id.button_9);
		b9.setOnClickListener((OnClickListener) this);
		View b10 = findViewById(R.id.button_10);
		b10.setOnClickListener((OnClickListener) this);
		View b11 = findViewById(R.id.button_11);
		b11.setOnClickListener((OnClickListener) this);
		View b12 = findViewById(R.id.button_12);
		b12.setOnClickListener((OnClickListener) this);
		View b13 = findViewById(R.id.button_13);
		b13.setOnClickListener((OnClickListener) this);
		View b14 = findViewById(R.id.button_14);
		b14.setOnClickListener((OnClickListener) this);
		View b15 = findViewById(R.id.button_15);
		b15.setOnClickListener((OnClickListener) this);
		View b16 = findViewById(R.id.button_16);
		b16.setOnClickListener((OnClickListener) this);
		View b17 = findViewById(R.id.button_17);
		b17.setOnClickListener((OnClickListener) this);
		View b18 = findViewById(R.id.button_18);
		b18.setOnClickListener((OnClickListener) this);
		View b19 = findViewById(R.id.button_19);
		b19.setOnClickListener((OnClickListener) this);
		View b20 = findViewById(R.id.button_20);
		b20.setOnClickListener((OnClickListener) this);
		View b21 = findViewById(R.id.button_21);
		b21.setOnClickListener((OnClickListener) this);
		View b22 = findViewById(R.id.button_22);
		b22.setOnClickListener((OnClickListener) this);
		View b23 = findViewById(R.id.button_23);
		b23.setOnClickListener((OnClickListener) this);
		View b24 = findViewById(R.id.button_24);
		b24.setOnClickListener((OnClickListener) this);
		View b25 = findViewById(R.id.button_25);
		b25.setOnClickListener((OnClickListener) this);
		
		View pause = findViewById(R.id.button_boggle_pause);
		pause.setOnClickListener((OnClickListener) this);
		View clear = findViewById(R.id.button_Boggle_Clear);
		clear.setOnClickListener((OnClickListener) this);
		View submit= findViewById(R.id.button_Boggle_Submit);
		submit.setOnClickListener((OnClickListener) this);
		View w = findViewById(R.id.boggle_word_textView);
		w.setOnClickListener((OnClickListener) this);
		populate_func(this.gameLetters);
	}

	public void onClick(View view) {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//String letter;

		//switch (view.getId()) {
		if (view.getId() == R.id.button_1)
		{	         
			v.vibrate(100);
			clickNoise(R.id.button_1);
			if(checkLine(R.id.button_1)){
				try {
					process_Click(R.id.button_1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_2)
		{
			v.vibrate(100);
			clickNoise(R.id.button_2);
			if(checkLine(R.id.button_2)){
				try {
					process_Click(R.id.button_2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}         
		}
		else if (view.getId() == R.id.button_3)
		{
			v.vibrate(100);
			clickNoise(R.id.button_3);
			if(checkLine(R.id.button_3)){
				try {
					process_Click(R.id.button_3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_4)
		{
			v.vibrate(100);
			clickNoise(R.id.button_4);
			if(checkLine(R.id.button_4)){
				try {
					process_Click(R.id.button_4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_5)
		{
			v.vibrate(100);
			clickNoise(R.id.button_5);
			if(checkLine(R.id.button_5)){
				try {
					process_Click(R.id.button_5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_6)
		{
			v.vibrate(100);
			clickNoise(R.id.button_6);
			if(checkLine(R.id.button_6)){
				try {
					process_Click(R.id.button_6);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_7)
		{
			v.vibrate(100);
			clickNoise(R.id.button_7);
			if(checkLine(R.id.button_7)){
				try {
					process_Click(R.id.button_7);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_8)
		{
			v.vibrate(100);
			clickNoise(R.id.button_8);
			if(checkLine(R.id.button_8)){
				try {
					process_Click(R.id.button_8);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_9)
		{
			v.vibrate(100);
			clickNoise(R.id.button_9);
			if(checkLine(R.id.button_9)){
				try {
					process_Click(R.id.button_9);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_10)
		{
			v.vibrate(100);
			clickNoise(R.id.button_10);
			if(checkLine(R.id.button_10)){
				try {
					process_Click(R.id.button_10);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_11)
		{
			v.vibrate(100);
			clickNoise(R.id.button_11);
			if(checkLine(R.id.button_11)){
				try {
					process_Click(R.id.button_11);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_12)
		{
			v.vibrate(100);
			clickNoise(R.id.button_12);
			if(checkLine(R.id.button_12)){
				try {
					process_Click(R.id.button_12);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_13)
		{
			v.vibrate(100);
			clickNoise(R.id.button_13);
			if(checkLine(R.id.button_13)){
				try {
					process_Click(R.id.button_13);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_14)
		{
			v.vibrate(100);
			clickNoise(R.id.button_14);
			if(checkLine(R.id.button_14)){
				try {
					process_Click(R.id.button_14);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_15)
		{
			v.vibrate(100);
			clickNoise(R.id.button_15);
			if(checkLine(R.id.button_15)){
				try {
					process_Click(R.id.button_15);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_16)
		{
			v.vibrate(100);
			clickNoise(R.id.button_16);
			if(checkLine(R.id.button_16)){
				try {
					process_Click(R.id.button_16);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_17)
		{
			v.vibrate(100);
			clickNoise(R.id.button_17);
			if(checkLine(R.id.button_17)){
				try {
					process_Click(R.id.button_17);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_18)
		{
			v.vibrate(100);
			clickNoise(R.id.button_18);
			if(checkLine(R.id.button_18)){
				try {
					process_Click(R.id.button_18);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_19)
		{
			v.vibrate(100);
			clickNoise(R.id.button_19);
			if(checkLine(R.id.button_19)){
				try {
					process_Click(R.id.button_19);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_20)
		{
			v.vibrate(100);
			clickNoise(R.id.button_20);
			if(checkLine(R.id.button_20)){
				try {
					process_Click(R.id.button_20);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_21)
		{
			v.vibrate(100);
			clickNoise(R.id.button_21);
			if(checkLine(R.id.button_21)){
				try {
					process_Click(R.id.button_21);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_22)
		{
			v.vibrate(100);
			clickNoise(R.id.button_22);
			if(checkLine(R.id.button_22)){
				try {
					process_Click(R.id.button_22);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_23)
		{
			v.vibrate(100);
			clickNoise(R.id.button_23);
			if(checkLine(R.id.button_23)){
				try {
					process_Click(R.id.button_23);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_24)
		{
			v.vibrate(100);
			clickNoise(R.id.button_24);
			if(checkLine(R.id.button_24)){
				try {
					process_Click(R.id.button_24);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_25)
		{
			v.vibrate(100);
			clickNoise(R.id.button_25);
			if(checkLine(R.id.button_25)){
				try {
					process_Click(R.id.button_25);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
		}
		else if (view.getId() == R.id.button_Boggle_Clear)
		{
			Music.Clear_Sound(this);
			v.vibrate(100);
			emptyDiceArray();
			enableAllButtons();
			UnselectButtons();
			hideText();
		}
		else if (view.getId() == R.id.button_Boggle_Submit)
		{
			clickNoise(R.id.button_Boggle_Submit);
			v.vibrate(100);
			if (getBoggleWord().length() >=3 ){
				try {
					check();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else if (view.getId() == R.id.button_boggle_pause)
		{
			clickNoise(R.id.button_boggle_pause);
			v.vibrate(100);
			h.removeMessages(id);
			emptyDiceArray();
			hideText();
			finish(); 
			}
		}
	

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		Music.play(this, R.raw.drum_beat);
		Log.d(TAG, "and here??");
		Log.d(TAG, "onResume letter set resume direct: "+ pref.getString(dice_dat, null));
		gameLetters = pref.getString(dice_dat, null);
		Log.d(TAG, "onResume letter set resume: "+ gameLetters);

		if (gameLetters != null){
			populate_func(this.gameLetters);
		}
		
		actualScore = pref.getInt(points, 0);
		setPoints(actualScore);
		Log.d(TAG, "onResume letter set score: "+ actualScore);
		
		String str = pref.getString(words, " ");
		Log.d(TAG, "onResume letter set word list: "+ str);
		PauseWords(str);
		
		actualTime = pref.getInt(time, 120);
		time_func();
		
		pref.getBoolean(resume, false);
	}

	@Override
	protected void onPause() {
		super.onPause();

		Music.play(this, R.raw.african_drums);
		String w = getPauseList();

		/** Store board **/
		pref.edit().putString(dice_dat, pause_board).commit();
		pref.edit().putInt(points, actualScore).commit();
		
		pref.edit().putString(words, w).commit();
		pref.edit().putInt(time, actualTime).commit();
		pref.edit().putBoolean(resume, true).commit();

	}

	/** Get random letters from different die **/
	private String getLetters(){
		int n = boardSize * boardSize;  
		List<Character> set_letters = new ArrayList<Character>(n);
		
		for (int i = 0; i < n; i++){
			int a = (int) Math.floor(Math.random() * 6);	// pick letter in list of characters
			int b = (int) Math.floor(Math.random() * dice_set.length); // pick dice in collection
			char c = dice_set[b].charAt(a);
			set_letters.add(c);
		}
		Collections.shuffle(set_letters);
		String ls = "";
		for (Character s : set_letters)
			ls += s;


		return ls;
	}

	public void populate_func(String  Letters){

		int num_dice = boardSize * boardSize; 
		for (int i = 0; i < num_dice; i++){
			Button b; 
			char c = Letters.charAt(i);

			b = (Button)findViewById(BUTTONS[i]);
			b.setText(Character.toString(c));
		}
	}

	public void time_func(){
		int seconds = actualTime;
		int minutes = seconds / 60;
		seconds     = seconds % 60;

		if (seconds < 10)
			time_textView.setText("" + minutes + ":0" + seconds);
		else
			time_textView.setText("" + minutes + ":" + seconds);            
		
	}

	/** checks whether the game is out of time **/
	public void timeUp(){
		if (actualTime == 0){
			disableAllButtons();
			View w = findViewById(R.id.boggle_word_textView);
			w.setClickable(false);
			
			if (actualScore == 1)
				Toast.makeText(this, "Game Over! You Scored " + actualScore + " point!", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, "Game Over! You Scored " + actualScore + " points!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	// processes each button click
	private void process_Click(int d) throws IOException{
		int s = dice_array.size();
		if(dice_array.isEmpty())
		{
			addToCurrentSet(d);
			SelectButton(d);
		}
		else if(dice_array.get(s-1) == d && !dice_array.isEmpty())
		{
			UnselectButton(d);
			removeLetter(d);
		}
		else if(dice_array.get(s-1) != d)
		{
			addToCurrentSet(d);
			SelectButton(d);
		}

		refreshWords();

	}

	/** Do android click noise when button press **/
	private void clickNoise(int d){
		Button b = (Button)findViewById(d);
		b.playSoundEffect(android.view.SoundEffectConstants.CLICK);
	}

	/** Disable every button on board **/
	private void disableAllButtons(){
		int l = BUTTONS.length;
		for (int i = 0; i < l; i++){
			Button b = (Button)findViewById(BUTTONS[i]);
			b.setClickable(false);
		}
	}

	/** Enable every button on board **/
	private void enableAllButtons(){
		int l = BUTTONS.length;
		for (int i = 0; i < l; i++){
			Button b = (Button)findViewById(BUTTONS[i]);
			b.setClickable(true);
		}
	}

	/** Select current button **/
	private void SelectButton(int diceID){

		Button b = (Button)findViewById(diceID);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_pressed));
	}

	/** Select current button **/
	private void UnselectButton(int diceID){

		Button b = (Button)findViewById(diceID);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
	}

	/** Select every button **/
	private void UnselectButtons(){

		int s = BUTTONS.length;
		for (int i = 0; i < s; i++){
			Button b = (Button)findViewById(BUTTONS[i]);
			b.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
		}
	}

	/** Get stored words **/
	private String getPauseList(){
		String line = null;

		for (String s : w){

			if (line == null)
				line = s;
			else
				line = line + " " + s;
		}
		return line;
	}

	/** Erase text for Text View **/
	private void hideText(){
		TextView t = (TextView)findViewById(R.id.boggle_word_textView);
		t.setText("");
	}

	/* Empty dice array */
	private void emptyDiceArray(){ dice_array.clear(); }

	//convert wordlist for getSharedPreferences storage
	private void PauseWords(String s){
		String [] str = s.split(" ");
		int size = str.length;

		for (int i = 0; i < size; i++){
			w.add(str[i]);
		}

	}

	/** Add letter to selected letters **/
	private void addToCurrentSet(int d){ dice_array.add(Integer.valueOf(d)); }

	/** Remove letter from selected letters **/
	private void removeLetter(int d){
		int s = dice_array.size();
		if(dice_array.get(s - 1) == d){
			dice_array.remove(s - 1);
		}
	}

	/** updates the current score **/
	private void newPoints(String w){
		int pts = scorePoints(w);
		actualScore = actualScore + pts;
		setPoints(actualScore);
	}

	/** Official boggle point method **/
	private int scorePoints(String w){
		int length = w.length();
		if(length == 3 || length == 4)
			return 1;
		else{
			int points = length - 4;
			return points + 1;
		}
	}

	/** Retrieve letter from die **/
	private String DiceLetters(int i){
		Button b = (Button)findViewById(i);
		String s = b.getText().toString();
		return s;
	}

	/** Makes word from string of dice **/
	private String getWord(){
		String w = ""; 
		for (int i = 0; i < dice_array.size(); i++){
			String l = DiceLetters(dice_array.get(i));
			w = w + l;
		}

		return w;
	}

	// sets the current score given the number
	private void setPoints(int s){
		TextView textView_score = (TextView)findViewById(R.id.boggle_score);
		textView_score.setText(Integer.toString(s));
	}

	/** Recognize word from buttons **/
	private String getBoggleWord(){
		TextView t = (TextView)findViewById(R.id.boggle_word_textView);
		String w = t.getText().toString().trim();
		return w;
	}

	// updates word list set
	private void list(String word){ w.add(word); }

	// updates the word text view
	private void refreshWords(){
		String w = getWord();
		TextView t = (TextView)findViewById(R.id.boggle_word_textView);
		t.setText(w.toUpperCase().trim());
	}

	private void check() throws IOException{
		Dictionary dict = new Dictionary();
		String targetWord = getBoggleWord().trim().toLowerCase();
		int wordLength = targetWord.length();

		boolean check = dict.searchDict(this.getApplicationContext(), wordLength, targetWord);

		if (check)
		{
			if(used(targetWord) == false)
			{
				Music.Right_Sound(this);
				newPoints(targetWord);
				list(targetWord);
				emptyDiceArray();
				enableAllButtons();
				UnselectButtons();
				hideText();
			}	
		}
		else
		{
			Music.Wrong_Sound(this);
			emptyDiceArray();
			enableAllButtons();
			UnselectButtons();
			hideText();	
			
		}
	}

	/** Is word in dictionary **/
	private boolean checkLine(int new_Dice){
		Dice gameDice = new Dice();

		int s = dice_array.size();
		if (s == 0)
			return true;
		else if(s > 0){
			int prev_dice = dice_array.get(s-1);
			return gameDice.isValid(prev_dice, new_Dice);
		}
		else
			return false;
	}

	/** Has word already been used **/
	private boolean used(String str){
		int s = w.size();
		for (int i = 0; i < s; i++){
			if (w.get(i).equalsIgnoreCase(str)){
				return true;
			}
		}
		return false;
	}
}
