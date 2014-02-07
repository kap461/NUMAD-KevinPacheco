/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.neu.madcourse.kevinpacheco.persistent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.kevinpacheco.R;
import edu.neu.madcourse.kevinpacheco.boggle.Dice;
import edu.neu.madcourse.kevinpacheco.boggle.Dictionary;
import edu.neu.madcourse.kevinpacheco.boggle.Music;

public class Multiplayer_Game extends Activity implements OnClickListener {
	private static final String TAG = "MultiplayerBoggleGame";

	public static final String 	MULTI_PREF	= "edu.neu.madcourse.kevinpacheco.persistent";
	public static final String  PREF_BOGGLE = "prefBoggle";
	private static final String 	PREF_USER = "prefUser";
	public static final String PREF_RESUME = "MultipalyerboggleResume";

	private SharedPreferences sf;
	private int size = 5;			
	private int score_p1 = 0; 	// current game score
	private int score_p2 = 0; 	// current game score
	private String username;
	private String opponent; 
	private String letterSet; // letters used in game
	private String onPauseLetters; 
	private ServerAccessor sa = new ServerAccessor();
	private GameFunctions game;
	private String current_turn;
	private	Multiplayer_Game_Serv service;

	private ArrayList<String> wordList= new ArrayList<String>();			// string of accepted words seperated by spaces

	private static List<Integer> current_dice_set = 
			new ArrayList<Integer>(); // set of current dice selected

	/* Official Boggle dice */
	private String[] dice_set = new String[]{
			"AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY",
			"BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT",
			"DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
	};

	/* Array of all buttons */
	private static final int[] BUTTONS = {	// array of dice buttons on the grid
		R.id.button_1,
		R.id.button_2,
		R.id.button_3,
		R.id.button_4,
		R.id.button_5,
		R.id.button_6,
		R.id.button_7,
		R.id.button_8,
		R.id.button_9,
		R.id.button_10,
		R.id.button_11,
		R.id.button_12,
		R.id.button_13,
		R.id.button_14,
		R.id.button_15,
		R.id.button_16,
		R.id.button_17,
		R.id.button_18,
		R.id.button_19,
		R.id.button_20,
		R.id.button_21,
		R.id.button_22,
		R.id.button_23,
		R.id.button_24,
		R.id.button_25
	};

	Handler handler = new Handler() 
	{ 
		@Override 
		public void handleMessage(Message m) { 
			super.handleMessage(m); 
		} 
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE); // no title

		opponent = getIntent().getStringExtra("opponent");
		username = getSharedPreferences(MULTI_PREF, MODE_PRIVATE).getString(PREF_USER, "guest");

		sendBroadcast(new Intent(this, OnBootReceiver.class));
		
		Toast.makeText(this, R.string.alarms_active, Toast.LENGTH_LONG).show();
		
		// Sets to multiplayer views
		setContentView(R.layout.multiplayer_game);

		this.game = sa.getGame(username, opponent);

		this.letterSet = this.game.getBoard();
		if (letterSet.isEmpty()){
			letterSet = this.choose_letters();
			sa.updateBoard(this.username, this.opponent, letterSet);
		}
		this.current_turn = this.game.get_this_turn();
		this.wordList = this.game.get_words();
		
		/****************************************************************************************************/
		Log.d(TAG, "~~~~~~~~~current_turn set to "+this.game.get_this_turn()+" in onCreate");
		Log.d(TAG, "~~~~~~~~~current_turn set to "+this.game.get_this_turn()+" in onCreate");

		this.score_p1 = this.game.get_player_score(username);
		this.score_p2 = this.game.get_player_score(opponent);		

		Log.d(TAG, "current set: "+current_dice_set.toString());
		onPauseLetters = choose_letters();

		Log.d(TAG, "onCreate Letterset: "+letterSet);
		Log.d(TAG, "onCreate tempLetterset: "+ onPauseLetters);

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

		this.setScore(R.id.multiplayer_player1, username, this.score_p1);

		this.setScore(R.id.multiplayer_player2, opponent, this.score_p2);

		whose_turn(this.current_turn);

		View used_words = findViewById(R.id.boggle_used_words);
		used_words.setOnClickListener((OnClickListener) this);

		fill(this.letterSet);
		
		if ((this.game.get_this_turn().equals(username))){
			Log.d(TAG, "onCreate: enable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			submitButton.setEnabled(true);
		}else{
			Log.d(TAG, "onCreate: disable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			submitButton.setEnabled(false);
		}
	}

	public void onClick(View v) {
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		//String letter;
		if(isYourTurn()){
			this.current_turn = sa.getTurn(username, opponent);
			this.game.set_this_turn(current_turn);
			this.check_submit_button();
		}

		switch (v.getId()) {
		case R.id.button_1:	         
			vib.vibrate(50);
			playClick(R.id.button_1);
			if(check_sequence(R.id.button_1)){
				try {
					process_Click(R.id.button_1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_2:
			vib.vibrate(50);
			playClick(R.id.button_2);
			if(check_sequence(R.id.button_2)){
				try {
					process_Click(R.id.button_2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}         
			break;
		case R.id.button_3:
			vib.vibrate(50);
			playClick(R.id.button_3);
			if(check_sequence(R.id.button_3)){
				try {
					process_Click(R.id.button_3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_4:
			vib.vibrate(50);
			playClick(R.id.button_4);
			if(check_sequence(R.id.button_4)){
				try {
					process_Click(R.id.button_4);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_5:
			vib.vibrate(50);
			playClick(R.id.button_5);
			if(check_sequence(R.id.button_5)){
				try {
					process_Click(R.id.button_5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_6:
			vib.vibrate(50);
			playClick(R.id.button_6);
			if(check_sequence(R.id.button_6)){
				try {
					process_Click(R.id.button_6);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_7:
			vib.vibrate(50);
			playClick(R.id.button_7);
			if(check_sequence(R.id.button_7)){
				try {
					process_Click(R.id.button_7);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_8:
			vib.vibrate(50);
			playClick(R.id.button_8);
			if(check_sequence(R.id.button_8)){
				try {
					process_Click(R.id.button_8);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_9:
			vib.vibrate(50);
			playClick(R.id.button_9);
			if(check_sequence(R.id.button_9)){
				try {
					process_Click(R.id.button_9);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_10:
			vib.vibrate(50);
			playClick(R.id.button_10);
			if(check_sequence(R.id.button_10)){
				try {
					process_Click(R.id.button_10);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_11:
			vib.vibrate(50);
			playClick(R.id.button_11);
			if(check_sequence(R.id.button_11)){
				try {
					process_Click(R.id.button_11);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_12:
			vib.vibrate(50);
			playClick(R.id.button_12);
			if(check_sequence(R.id.button_12)){
				try {
					process_Click(R.id.button_12);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_13:
			vib.vibrate(50);
			playClick(R.id.button_13);
			if(check_sequence(R.id.button_13)){
				try {
					process_Click(R.id.button_13);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_14:
			vib.vibrate(50);
			playClick(R.id.button_14);
			if(check_sequence(R.id.button_14)){
				try {
					process_Click(R.id.button_14);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_15:
			vib.vibrate(50);
			playClick(R.id.button_15);
			if(check_sequence(R.id.button_15)){
				try {
					process_Click(R.id.button_15);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_16:
			vib.vibrate(50);
			playClick(R.id.button_16);
			if(check_sequence(R.id.button_16)){
				try {
					process_Click(R.id.button_16);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_17:
			vib.vibrate(50);
			playClick(R.id.button_17);
			if(check_sequence(R.id.button_17)){
				try {
					process_Click(R.id.button_17);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_18:
			vib.vibrate(50);
			playClick(R.id.button_18);
			if(check_sequence(R.id.button_18)){
				try {
					process_Click(R.id.button_18);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_19:
			vib.vibrate(50);
			playClick(R.id.button_19);
			if(check_sequence(R.id.button_19)){
				try {
					process_Click(R.id.button_19);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_20:
			vib.vibrate(50);
			playClick(R.id.button_20);
			if(check_sequence(R.id.button_20)){
				try {
					process_Click(R.id.button_20);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_21:
			vib.vibrate(50);
			playClick(R.id.button_21);
			if(check_sequence(R.id.button_21)){
				try {
					process_Click(R.id.button_21);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_22:
			vib.vibrate(50);
			playClick(R.id.button_22);
			if(check_sequence(R.id.button_22)){
				try {
					process_Click(R.id.button_22);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_23:
			vib.vibrate(50);
			playClick(R.id.button_23);
			if(check_sequence(R.id.button_23)){
				try {
					process_Click(R.id.button_23);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_24:
			vib.vibrate(50);
			playClick(R.id.button_24);
			if(check_sequence(R.id.button_24)){
				try {
					process_Click(R.id.button_24);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		case R.id.button_25:
			vib.vibrate(50);
			playClick(R.id.button_25);
			if(check_sequence(R.id.button_25)){
				try {
					process_Click(R.id.button_25);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	         
			break;
		}
	}

	public void return_button(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Back button clicked");
		playClick(R.id.multiplayer_game_back);
		vib.vibrate(50);
		clearCurrentSet();
		finish(); 
	}

	public void clear_button(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Clear button clicked");
		playClick(R.id.multiplayer_game_clear_word);
		vib.vibrate(50);
		clearCurrentSet();
		enableAllButtons();
		swap_all_unclick();
		clearWordTextView();
	}

	public void submit_button(View v){
		Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		Log.d(TAG,"Submit button clicked");
		playClick(R.id.multiplayer_game_submit_word);
		Log.d(TAG, "submit clicked");
		vib.vibrate(50);
		//TODO if number of turns has reached the limit
		if (get_word().length() >=3 ){
			try {
				isWord();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		Music.play(this, R.raw.african_drums);

		letterSet = sa.getBooard(this.username, this.opponent);
		Log.d(TAG, "onResume letter set resume: "+ letterSet);
		if (letterSet != null){
			fill(this.letterSet);
		}
		update_used_words();
	}

	// Picks a collection of letters from set of dice
	private String choose_letters(){
		Log.d(TAG, "Pick Dice");
		int dice = size * size;  
		List<Character> set = new ArrayList<Character>(+dice);
		for (int i = 0; i < dice; i++){
			int x = (int) Math.floor(Math.random() * 6);	// pick letter in list of characters
			int d = (int) Math.floor(Math.random() * dice_set.length); // pick dice in collection
			char c = dice_set[d].charAt(x);
			set.add(c);
		}
		Collections.shuffle(set);
		String f = "";
		for (Character s : set){
			f += s;
		}
		Log.d(TAG, "Pick letters: " + f);
		return f;	   
	}

	// Fills the buttons with selected dice
	public void fill(String  l){
		Log.d(TAG, l.toString());
		int dice = size * size; 
		for (int i = 0; i < dice; i++){
			Button b; 
			char c = l.charAt(i);
			Log.d(TAG, Character.toString(c));
			if (c == 'Q'){
				b = (Button)findViewById(BUTTONS[i]);
				b.setText("Qu");
			}else{
				b = (Button)findViewById(BUTTONS[i]);
				b.setText(Character.toString(c));
			}
		}
	}

	// processes each button click
	private void process_Click(int d) throws IOException{
		int s = current_dice_set.size();
		if(current_dice_set.isEmpty()){
			Log.d(TAG, "process_Click:isEmpty");
			add_to_set(d);
			swap_click(d);
		}else if(current_dice_set.get(s-1) == d && !current_dice_set.isEmpty()){
			Log.d(TAG, "process_Click:peek==diceID");
			swap_unclick(d);
			remove_from_set(d);
			enable_btns();
		}else if(current_dice_set.get(s-1) != d){
			Log.d(TAG, "process_Click:peek!=diceID");
			disable_btns();
			add_to_set(d);
			swap_click(d);
		}
		Log.d(TAG, "process_click: UpdateWordTextView");
		updateWordTextView();

	}

	// plays click sound
	private void playClick(int d){
		Log.d(TAG, "playClick: "+ d);
		Button b = (Button)findViewById(d);
		b.playSoundEffect(android.view.SoundEffectConstants.CLICK);
	}

	// disable last button in set
	private void disable_btns(){
		int size = current_dice_set.size();
		Log.d(TAG, "disableButton");
		if(!current_dice_set.isEmpty()){
			Button dice = (Button)findViewById(current_dice_set.get(size-1));
			dice.setClickable(false);
		}
	}	

	// enable last button in set
	private void enable_btns(){
		int size = current_dice_set.size();
		if(!current_dice_set.isEmpty()){
			Button dice = (Button)findViewById(current_dice_set.get(size-1));
			dice.setClickable(true);
		}
	}

	// enables all button
	private void enableAllButtons(){
		Log.d(TAG, "enableAllButton");
		int size = BUTTONS.length;
		for (int i = 0; i < size; i++){
			Button dice = (Button)findViewById(BUTTONS[i]);
			dice.setClickable(true);
		}
	}

	// swap button to click state
	private void swap_click(int d){
		Log.d(TAG,"swapButonClick");
		Button b = (Button)findViewById(d);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_pressed));
	}

	// Swap button to unclick state
	private void swap_unclick(int d){
		Log.d(TAG,"swapButonUnClick");
		Button b = (Button)findViewById(d);
		b.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
	}

	// swap All buttons to Unclick state
	private void swap_all_unclick(){
		Log.d(TAG,"swapButonUnClick");
		int size = BUTTONS.length;
		for (int i = 0; i < size; i++){
			Button dice = (Button)findViewById(BUTTONS[i]);
			dice.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_default_normal));
		}
	}

	// generates the onPause word list for storage
	private String generate_onPause_wordList(){
		String line = null;
		Log.d(TAG, "generate_onPause_wordList");
		for (String v : wordList){
			Log.d(TAG, "generate_onPause_wordList: "+ v);
			if (line == null){
				line = v;
			}else{
				line = line + " " + v;
			}
		}
		return line;
	}

	// clears the word text view
	private void clearWordTextView(){
		Log.d(TAG, "clearWordTextView");
		TextView ans = (TextView)findViewById(R.id.boggle_word_textView);
		ans.setText("");
	}

	// clears current dice set
	private void clearCurrentSet(){
		Log.d(TAG, "clearCurrentSet()");
		current_dice_set.clear();
	}

	// add dice to current dice set
	private void add_to_set(int dice){
		Log.d(TAG, "addToCurrentSet");
		current_dice_set.add(Integer.valueOf(dice));
		Log.d(TAG,"add to current_dice_set :"+Integer.toString(dice));
		//Log.d(TAG,"add to current_dice_set peek()"+Integer.toString(current_dice_set.get(size-1)));
		Log.d(TAG,"add to current_dice_set size()"+Integer.toString(current_dice_set.size()));
	}

	//remove dice from current dice set
	private void remove_from_set(int dice){
		int size = current_dice_set.size();
		if(current_dice_set.get(size-1) == dice){
			current_dice_set.remove(size-1);
		}
	}

	// updates player 1  score
	private void update_player1_score(String word){
		Log.d(TAG, "updateScore P1");
		int add_points = scoring(word);
		this.score_p1 = this.score_p1 + add_points;

		Log.i(TAG, "additional points : " + Integer.toString(this.score_p1));
		Log.i(TAG, "current score : " + Integer.toString(this.score_p1));

		this.game.updateScore(username, this.score_p1);

		updateOverAllScores();
		setScore(R.id.multiplayer_player1, username, this.score_p1);
	}


	// update player 2 score
	private void update_player2_score(String w){
		Log.d(TAG, "updateScore P2");
		int pts = scoring(w);
		this.score_p2 = this.score_p2+ pts;

		Log.i(TAG, "additional points : " + Integer.toString(this.score_p2));
		Log.i(TAG, "current score : " + Integer.toString(this.score_p2));

		this.game.updateScore(opponent, this.score_p2);

		updateOverAllScores();
		setScore(R.id.multiplayer_player2, opponent, this.score_p2);
	}

	// scoring points
	private int scoring(String w){
		int l = w.length();
		if(l == 3 || l == 4){
			return 1;
		}else{
			int pts = l - 4;
			return pts + 1;
		}
	}

	// get letter from dice
	private String get_letter(int d){
		Button b = (Button)findViewById(d);
		String l = b.getText().toString();
		return l;
	}

	// generates the word from dice set
	private String get_list(){

		String w = ""; 
		for (int i = 0; i < current_dice_set.size(); i++){
			String l = get_letter(current_dice_set.get(i));
			w = w + l;

		}
		String f = w;
		return f;
	}

	// sets the current score given the number
	private void setScore(int id, String user, int s){
		Log.d(TAG, "setScore");
		TextView t = (TextView)findViewById(id);
		Log.d(TAG, "setScore ID: "+ id);
		Log.d(TAG, user + "Score : " + t.getText().toString());

		String str = user + "|" + s;
		t.setText(str);
	}

	//gets the word in the word view
	private String get_word(){
		TextView t = (TextView)findViewById(R.id.boggle_word_textView);
		String w = t.getText().toString().trim();

		return w;
	}

	// updates word list set
	private void update_words(String word){
		Log.d(TAG, "updateWordList");
		wordList.add(word);
		//this.game.addEnteredWord(word);
		/**
		 * Server Side add word 
		 */
		Log.d(TAG, "updateWordList: " + wordList);
	}

	// updates the used word list
	private void update_used_words(){
		TextView usedWordsView = (TextView)findViewById(R.id.boggle_used_words);
		String temp = generate_onPause_wordList();
		this.updateUsedWords();
		usedWordsView.setText(temp);
	}

	// updates the word text view
	private void updateWordTextView(){
		Log.d(TAG, "updateWordTextView");
		String f = get_list();
		TextView t = (TextView)findViewById(R.id.boggle_word_textView);
		t.setText(f.toUpperCase().trim());
	}

	// checks whether the word in word view is valid word in the dictionary
	private void isWord() throws IOException{
		Log.d(TAG,"isWord");

		Dictionary dict = new Dictionary();
		String s = get_word().trim().toLowerCase();
		int wordLength = s.length();

		boolean check = dict.searchDict(this.getApplicationContext(), wordLength, s);

		if (check){
			Log.d(TAG,"isWord: PASS");
			if(check_word(s) == false){
				Music.playClip(this, R.raw.correct);
				if(this.current_turn.equals(this.username)){
					update_player1_score(s);
					this.game.set_this_turn(this.opponent);
					this.current_turn = this.opponent;
					this.game.turns_plus_1();
					whose_turn(this.opponent);
					
					/****************************************************************************************************/
					Log.d(TAG, "~~~~~~~~~Turn set to "+this.opponent+" in isWord");
					Log.d(TAG, "~~~~~~~~~current_turn = "+this.opponent+" in isWord");
				}
				update_words(s);

				clearCurrentSet();

				enableAllButtons();

				swap_all_unclick();

				clearWordTextView();

				update_used_words();

				check_submit_button();
			}
		}else{
			//Log.d(TAG,"isWord: Fail");
		}
	}

	protected void check_submit_button(){
		Button b = (Button)findViewById(R.id.multiplayer_game_submit_word);
		b.setEnabled(false);
		if ((this.game.get_this_turn().equals(username))){
			Log.d(TAG, "Disable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			//submitButton.setClickable(false);
			submitButton.setEnabled(true);
		}else{
			Log.d(TAG, "enable Submit Button");
			Button submitButton = (Button)findViewById(R.id.multiplayer_game_submit_word);
			//submitButton.setClickable(true);
			submitButton.setEnabled(true);
		}
	}

	protected void whose_turn(String user){
		TextView t =(TextView) findViewById(R.id.multiplayer_current_turn);
		if (this.username.equals(user)){
			t.setText("<--");
		}else if(this.opponent.equals(user)){
			t.setText("-->");
		}
	}

	// checks whether the sequence of letters is valid
	private boolean check_sequence(int new_Dice){
		Dice gameDice = new Dice();
		Log.d(TAG, "checkSequenceIsValid");

		int size = current_dice_set.size();
		Log.d(TAG, "size :" + Integer.toString(size));
		if (size == 0){
			Log.d(TAG, "size == 0: isValid");
			return true;
		}else if(size > 0){
			int prev_dice = current_dice_set.get(size-1);
			Log.d(TAG, "size > 0: isValid");
			return gameDice.isValid(prev_dice, new_Dice);
		}else{
			return false;
		}
	}

	// checks whether a word is already used
	private boolean check_word(String s){
		Log.d(TAG, "checkUsedWords");
		int sz = wordList.size();
		for (int i = 0; i < sz; i++){
			Log.d(TAG, "checkUsedWords " + wordList.get(i));
			if (wordList.get(i).equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}

	private void updateOverAllScores(){
		/**
		 * 
		 */
		sa.setScoreContent(username, this.score_p1, opponent, this.score_p2);
	}
	
	// updates the serverside user words lists
	private void updateUsedWords(){
		sa.setEnteredWords(username, opponent, generate_onPause_wordList(), new OnBooleanReceivedListener() {
			
			public void run(Boolean exitState) {
				if (!exitState) {
					Log.e(TAG, "Couldn't set the used word list on the server");
				}
			}
		});
	}

	public boolean isYourTurn(){
		String current_turn = this.game.get_this_turn();
		if (this.game.get_this_turn().equals(current_turn)){
			return false;
		}else{
			return true;
		} 

	}

	public void foo(){
		Intent i = new Intent(this, Multiplayer_Game.class);
		i.putExtra("username", this.username);
		i.putExtra("opponent", this.opponent);
		i.putExtra("currentTurn", this.game.get_this_turn());
	}
}

