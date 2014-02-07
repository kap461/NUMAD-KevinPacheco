package edu.neu.madcourse.kevinpacheco.persistent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

public class GameFunctions {
	private String currentTurn;
	private String board;
	private ArrayList<String> enteredWords;
	private Hashtable<String, Integer> scores;
	private int t;
	//private int turnP1;
	//private int turnP2;
	
	private static final String TAG = "GameFunctions";
	
	public GameFunctions(String c, String b, ArrayList<String> w, Hashtable<String, Integer> s, int n) {
		this.board = b; 
		this.currentTurn = c;
		this.enteredWords = w;
		this.scores = s;
		this.t = n;
	}
	
	/**
	 * Gets the username of the other player in the game
	 * @param user 	The name of the requesting user
	 * @return 		A string that contains the name of users' opponent
	 */
	public String findPlayer2(String p2) {
		List<String> all_users = Collections.list(scores.keys());
		for (String username : all_users) {
			if (!username.equals(p2)) {
				return username;
			}
		}
		return "Nothing to see here.";
	}
	
	public void turns_plus_1() {
		this.t++;
	}
	
	public int get_turns() {
		return this.t;
	}

	public ArrayList<String> get_words() {
		return this.enteredWords;
	}

	public String get_this_turn() {
		Log.d(TAG, "~~~~~~~~~~~~~~~~Inside get_this_turn, currentTurn = " + currentTurn);
		return this.currentTurn;
	}
	
	public boolean isTurn(String user) {
		return user.equals(currentTurn);
	}
	
	public void set_this_turn(String user) {
		this.currentTurn = user;
		Log.d(TAG, "~~~~~~~~~~~~~~~~Inside set_this_turn, currentTurn = " + currentTurn);
	}

	public void updateScore(String user, Integer score) {
		this.scores.put(user, score);
	}
	
	public int get_player_score(String user){
		return this.scores.get(user);
	}

	public String getBoard() {
		Log.d("GameWrapper", "getBoard: "+ this.board);
		return this.board;
	}
}
