package edu.neu.madcourse.kevinpacheco.persistent;
import java.util.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import edu.neu.mobileclass.apis.KeyValueAPI;


public class ServerAccessor {
	private static final String TEAM_NAME = "Brzlnboi";
	private static final String PASSWORD  = "Yesterday1";
	private static String USER_NAME = "";
	private static String USER_PASS = "";

	private static final String BOARD_PREFIX = "board_";
	private static final String TURN_PREFIX = "turn_";
	private static final String ENTERED_WORDS_PREFIX = "entered_";
	private static final String HIGHSCORE_PREFIX = "highscore_";
	private static final String SCORES_PREFIX = "scores_";
	private static final String NUM_TURNS_PREFIX = "numTurns_";
	private static final String REQUESTS_PREFIX = "req_";
	private static final String RECEIVED_PREFIX = "rec_";
	private static final String USERS_KEY = "users";
	private static final String GAMES_KEY = "games";
	private static final String PASS_KEY = "pass_";

	// Context for doing asyncTask
	private static Context c;
	private static int size = 5;
	private static final String TAG = "ServerAccessor";

	private static String[] dice_set = new String[]{ 	// strings of all possible dice
		"AAAFRS",
		"AAEEEE",
		"AAFIRS",
		"ADENNN",
		"AEEEEM",
		"AEEGMU",
		"AEGMNN",
		"AFIRSY",
		"BJKQXZ",
		"CCNSTW",
		"CEIILT",
		"CEILPT",
		"CEIPST",
		"DDLNOR",
		"DHHLOR",
		"DHHNOT",
		"DHLNOR",
		"EIIITT",
		"EMOTTT",
		"ENSSSU",
		"FIPRSY",
		"GORRVW",
		"HIPRRY",
		"NOOTUW",
		"OOOTTU"
	};

	public ServerAccessor() {
	}

	/**
	 * Checks if the server is available
	 * @return True if we can connect to the server, false otherwise.
	 */
	public boolean canConnect() {
		return KeyValueAPI.isServerAvailable();
	}

	/**
	 * Gets the value associated with the given key
	 * @param key    the key associated with the value to get
	 */
	public String get(String key) {
		return KeyValueAPI.get(TEAM_NAME, PASSWORD, key);
	}

	/**
	 * Stores given key/value pair on the server
	 * @param key    the key of the key/value pair
	 * @param value  the value of the key/value pair.
	 */
	public void put(String key, String value) {
		KeyValueAPI.put(TEAM_NAME, PASSWORD, key, value);
	}

	/**
	 * Clears all key/value pairs from the server.
	 */
	public void clear() {
		KeyValueAPI.clear(TEAM_NAME, PASSWORD);
	}

	/**
	 * Clears a specific key/value pairing
	 * @param key the key of the key/value pair to delete.
	 */
	public void clearKey(String key) {
		KeyValueAPI.clearKey(TEAM_NAME, PASSWORD, key);
	}

	/**
	 * Returns true if the server is available.
	 */
	public boolean isAvailable() {
		return KeyValueAPI.isServerAvailable();
	}

	/**
	 * Converts a list of Strings to a single String
	 * @param users The list of users to convert
	 */
	public String arrayListToString(ArrayList<String> users) {
		StringBuilder builder = new StringBuilder();
		for (String user : users) {
			if (!user.equals("")) {
				builder.append(user + ",");
			}
		}
		return builder.toString();
	}

	public ArrayList<String> stringToArrayList(String strUsers) {
		ArrayList<String> users = new ArrayList<String>(Arrays.asList(strUsers.split(",")));
		return users;
	}
	// Requests

	/**
	 * Adds a game request for the given user
	 * @param user the user for whom to add the request
	 * @param req  the user to add to users' request list
	 */
	private void addRequest(String user, String req) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> reqs = this.stringToArrayList(this.get(key));
		ArrayList<String> recs = this.stringToArrayList(this.get(RECEIVED_PREFIX+user));

		// if we already have a received request from that user or we have already sent
		// a request to that user
		if ( reqs.contains(req) || recs.contains(req) ) {
			Log.e(TAG, "Trying to add a request that already exists");
			throw new RuntimeException("given request is already in the requests list");
		}
		reqs.add(req);
		String val = this.arrayListToString(reqs);
		this.put(key, val);
	}

	//TODO: Deprecated.  Implement calling getSentRequests(asynchronous)
	/**
	 * Returns a list of the given users' requests
	 * @param user the user whose requests we're getting
	 * @return an ArrayList<String> of all the given users' requests
	 */
	private ArrayList<String> getSentRequests(String user) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> reqs = this.stringToArrayList(this.get(key));
		return reqs;
	}

	public void getSentRequests(String user, final OnStringArrayListLoadedListener l) {

		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetKeyTask");
		class GetSentRequestsTask extends AsyncTask<String, Integer, ArrayList<String>> {

			protected ArrayList<String> doInBackground(String... key) {
				Log.d(TAG, "in doInBackground for getSentRequests");
				return thisSA.getSentRequests(key[0]);
				//return stringToArrayList(thisSA.get(key[0]));
			}

			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for getSentRequests");
				l.run(result);
			}
		}

		try {
			new GetSentRequestsTask().execute(user);
		} catch(Exception e) {
			Log.e(TAG, "GetKeyTask thread died: " +e);
		}
	}

	/**
	 * Adds all the given requests to the users' requests list
	 * @param user The user to add requests to
	 * @param reqsToAdd The list of Strings that represents all the requests to add
	 */
	public void addRequestList(String user, ArrayList<String> reqsToAdd) {
		ArrayList<String> reqs = this.getSentRequests(user);
		reqs.addAll(reqsToAdd);
		String key = REQUESTS_PREFIX + user;
		String val = this.arrayListToString(reqs);
		this.put(key, val);
	}

	/**
	 * Removes the given request from the given users' sent request list
	 * @param user  The user whose request list we're removing stuff from
	 * @param req   The user request we're removing
	 */
	private void removeRequest(String user, String req) {
		String key = REQUESTS_PREFIX + user;
		ArrayList<String> curReqs = this.getSentRequests(user);
		System.out.println("getRequests for " +user +"looks like: " +this.arrayListToString(this.getSentRequests(user)));
		if (curReqs.contains(req)) {
			curReqs.remove(req);
			System.out.println("removing " +req +" from " +user+"'s sent request list");
			String val = this.arrayListToString(curReqs);
			System.out.println("putting '" +val +"' at key " +key);
			this.put(key, val);
		}

	}

	// Received

	/**
	 * Adds user rec to user1's recieved list
	 * @param user  The user whose recieved list we're editing
	 * @param rec   The user to add to users's recieved list
	 */
	private void addReceived(String user, String rec) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> recs = this.stringToArrayList(this.get(key));
		ArrayList<String> reqs = this.stringToArrayList(this.get(REQUESTS_PREFIX)+user);

		// if we already have a received request from that user or we have already sent
		// a request to that user
		if( recs.contains(rec) || reqs.contains(rec)) {
			Log.e(TAG, "Trying to add a received request that already exits");
			throw new RuntimeException ("Given request already in received request list");
		}
		recs.add(rec);
		String val = this.arrayListToString(recs);
		this.put(key, val);
	}

	/**
	 * Returns a list of the given users' received requests...asynchronously
	 * @param user  The user whose requests we're returning
	 * @param arrayListListener The listener whose run(ArrayList<String>) function we're gonna call at the end
	 * @return    The list of users' received requests 
	 */
	public void getReceivedRequests(String user, final OnStringArrayListLoadedListener arrayListListener) {

		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetKeyTask");
		class GetReceivedRequestsTask extends AsyncTask<String, Integer, ArrayList<String>> {

			protected ArrayList<String> doInBackground(String... key) {
				return stringToArrayList(thisSA.get(key[0]));
			}

			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for getSentRequests");
				arrayListListener.run(result);
			}
		}

		try {
			new GetReceivedRequestsTask().execute(RECEIVED_PREFIX + user);
		} catch(Exception e) {
			Log.e(TAG, "GetKeyTask thread died: " +e);
		}
	}


	//TODO: Deprecated.  implement calling getReceivedRequests
	/**
	 * Returns a list of the given users' received requests
	 * @param user the user whose requests we're getting
	 * @return an ArrayList<String> of all the given users' requests
	 */
	private ArrayList<String> getReceivedRequests(String user) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> recs = this.stringToArrayList(this.get(key));
		return recs;
	}

	/**
	 * Adds all users in the list to the given users' received list
	 * @param user  The user whose received list we're editing
	 * @param recs  The list of requests to add
	 */
	private void addReceivedRequests(String user, ArrayList<String> recs) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> curRecs = this.stringToArrayList(this.get(key));
		curRecs.addAll(recs);
		String val = this.arrayListToString(curRecs);
		this.put(key, val);
	}

	/**
	 * Removes rec from the given users' list of received requests
	 * @param user  The user whose received list we're editing
	 * @param rec   The received request we're removing
	 */
	private void removeReceivedRequest(String user, String rec) {
		String key = RECEIVED_PREFIX + user;
		ArrayList<String> curRecs = this.getReceivedRequests(user);
		if (curRecs.remove(rec)) {
			System.out.println(rec + " was found in curRecs");
			String val = this.arrayListToString(curRecs);
			System.out.println("putting '" +val +"' at key " +key);
			this.put(key, val);
		}
	}

	/**
	 * Returns true if the given user/pass combination exists
	 * @param user  The given username
	 * @param pass  The given password
	 */
	public void login(String user, String pass, final OnBooleanReceivedListener l) {
		final String usersKey = USERS_KEY;
		final String passKey = PASS_KEY + user;
		final ServerAccessor thisSA = this;

		class LoginTask extends AsyncTask<String, Integer, Boolean> {
			protected Boolean doInBackground(String... keys) {
				String user = keys[0];
				String pass = keys[1];
				if (!thisSA.canConnect()) {
					Log.d(TAG, "Can't connect to server");
					return false;
				}
				Log.d(TAG, "In doInBackground for LoginTask");
				String passVal = thisSA.get(passKey);
				Log.d(TAG, passVal + " is the password");
				ArrayList<String> users = stringToArrayList(thisSA.get(usersKey));
				if (users.get(0).startsWith("ERROR")) {
					Log.d(TAG, "Connection error to server");
					return false;
				}

				if (!passVal.equals(pass)) {
					Log.d(TAG, "passwords don't match: "+passVal+" stored, "+pass+" entered.");
					return false;
				}
				// if we're here, the user exists and the password was right
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute in LoginTask");
				l.run(result);
			}
		}
		try {
			new LoginTask().execute(user, pass);
		} catch(Exception e) {
			Log.e(TAG, "LoginTask thread died: " +e);
		}
	}

	/**
	 * Registers a new user
	 * This will only be called if the given user/pass is valid
	 * @param user  The new username to register
	 * @param pass  The password for the new username
	 */
	public void register(String user, String pass, final OnBooleanReceivedListener l) {
		final String usersKey = USERS_KEY;
		final String passKey = "pass_" + user;
		final ServerAccessor thisSA = this;

		class RegisterUserTask extends AsyncTask<String, Integer, Boolean> {
			protected Boolean doInBackground(String... keys) {
				if (!thisSA.canConnect()) {
					return false;
				}
				String user = keys[0];
				String pass = keys[1];
				if (!thisSA.canRegister(user, pass)) {
					return false;
				}
				// if we're here, the user can be registered.
				Log.d(TAG, "In doInBackground for RegisterUserTask");
				thisSA.put(passKey, pass); // enters the new user/pass combo in server
				ArrayList<String> users = stringToArrayList(thisSA.get(usersKey));
				if (users.get(0).startsWith("ERROR")) {
					return false;
				}
				users.add(user); //adds given username to list of users
				thisSA.put(usersKey, thisSA.arrayListToString(users)); // updates list of users on server
				thisSA.put(passKey, pass); // enters the password into the server
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute in getUserList");
				l.run(result);
			}
		}
		try {
			new RegisterUserTask().execute(user, pass);
		} catch(Exception e) {
			Log.e(TAG, "RegisterUserTask thread died: " +e);
		}

		}

	/**
	 * Gets the list of users
	 * @return An ArrayList representation of users
	 */
	public void getUserList(final OnStringArrayListLoadedListener l) {
		final ServerAccessor thisSA = this;
		class GetUserListTask extends AsyncTask<String, Integer, ArrayList<String>> {

			protected ArrayList<String> doInBackground(String... keys) {
				Log.d(TAG, "In doInBackground for getUserList");
				if (!thisSA.canConnect()) {
					return new ArrayList<String>() {{
						add("ERROR");
					}};
				}
				String usersKey = USERS_KEY;
				return thisSA.stringToArrayList(thisSA.get(usersKey));
			}

			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute in getUserList");
				l.run(result);
			}
		}
		try {
			new GetUserListTask().execute();
		} catch(Exception e) {
			Log.e(TAG, "GetGames thread died: " +e);
		}
	}

	/**
	 * Checks to see if a new user/pass combo can be registered
	 * @param user  The username to check
	 * @param pass  The password to check
	 * @return true if the username/pass can be registered, false otherwise
	 */
	public boolean canRegister(String user, String pass) {
		// if the username or password contains commas, it can't be registered
		if (user.contains(",") || pass.contains(",")) {
			return false;
		} else if (alreadyRegistered(user)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Checks to see if the given username is already registered
	 * @param user  The username to check
	 * @return true if the username is already registered
	 */
	public boolean alreadyRegistered(String user) {
		String usersKey = USERS_KEY;
		ArrayList<String> users = stringToArrayList(this.get(usersKey));
		return users.contains(user);
	}

	/**
	 * Returns the key for the given two users - order unspecific
	 * I recognize that this does not generate a very unique
	 * key, but it needs to be an int and we have a very small use case.
	 * @param user1   The first user
	 * @param user2   The second user
	 * @return        An integer value that is the key value for the two users
	 */
	private String getUsersKey(String user1, String user2) {

		int hash1 = user1.hashCode();
		int hash2 = user2.hashCode();
		int key = hash1+hash2;
		return "" + key;
	}

	/**
	 * Returns true if a game between the two users exists
	 * @param user1   The first user
	 * @param user2   The second user
	 * @return        True if a game exists, false otherwise
	 */
	public boolean doesGameExist(String user1, String user2) {
		String gameKey = GAMES_KEY;
		// get the unique identifier for these two users
		String thisGame = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(gameKey));
		return games.contains(thisGame);
	}

	private void addGame(String user1, String user2) {
		final String usersKey = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(GAMES_KEY));
		games.add(usersKey);
		String gameVal = this.arrayListToString(games);
		this.put(GAMES_KEY,  gameVal);
	}

	public void removeGame(String user1, String user2) {
		String gameKey = GAMES_KEY;
		String usersKey = this.getUsersKey(user1, user2);
		ArrayList<String> games = this.stringToArrayList(this.get(gameKey));
		games.remove(usersKey);
		String gameVal = this.arrayListToString(games);
		this.put(gameKey,  gameVal);
	}

	
	public ArrayList<String> getGames(String user, final OnStringArrayListLoadedListener arrayListListener) {

		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask GetGamesTask");
		class GetGamesTask extends AsyncTask<String, Integer, ArrayList<String>> {

			protected ArrayList<String> doInBackground(String... key) {
				Log.d(TAG, "in doInBackground for GetGamestask");
				String user = key[0];
				ArrayList<String> serverGames = thisSA.stringToArrayList(thisSA.get(GAMES_KEY));
				ArrayList<String> serverUsers = thisSA.stringToArrayList(thisSA.get(USERS_KEY));
				ArrayList<String> games = new ArrayList<String>();

				//TODO
				// if serverUsers is empty for some reason, populat with error SEE BELOW.

				for (String user2 : serverUsers) {
					if (serverGames.contains(thisSA.getUsersKey(user, user2))) {
						Log.d(TAG, "serverUsers: "+ user2);
						games.add(user2);
					}
				}
				//TODO

				if (games.isEmpty()) {
					Log.d(TAG, "games is empty! populating with error message");
					games.add("ERROR");
				}
				Log.d(TAG, "returning: " +games.toString()+"for current games"); 
				return games;
			}

			protected void onPostExecute(ArrayList<String> result) {
				Log.d(TAG, "in onPostExecute for GetGamesTask");
				arrayListListener.run(result);
			}
		}

		try {
			new GetGamesTask().execute(user);
		} catch(Exception e) {
			Log.e(TAG, "GetGames thread died: " +e);
		}


		ArrayList<String> serverGames = this.stringToArrayList(this.get(GAMES_KEY));
		ArrayList<String> serverUsers = this.stringToArrayList(this.get(USERS_KEY));
		ArrayList<String> games = new ArrayList<String>();
		for (String user2 : serverUsers) {
			if (serverGames.contains(this.getUsersKey(user, user2))) {
				games.add(user2);
			}
		}
		return games;
	}

	// only called when a previous game doesn't exist
	private void initializeNewGame(String creator, String opponent, String board) {
		Log.d(TAG, "Creator: " + creator + "|");
		Log.d(TAG, "Opponent: " + opponent + "|");

		String userskey = this.getUsersKey(creator, opponent);

		// add game to game list
		this.addGame(creator, opponent);
		System.out.println("adding game: " +creator +" vs " +opponent);

		// add board to the server
		String boardKey = BOARD_PREFIX + userskey;
		Log.d(TAG, "board key: "+ boardKey);
		Log.d(TAG, "baord board: "+ board);
		this.put(boardKey, board); // create the board on the server
		Log.d(TAG, "baord Content: "+ this.get(boardKey));

		// value of turn element will always be a username - initiates to the creator.
		String turnKey = TURN_PREFIX + userskey;
		Log.d(TAG, "turnkey key: "+ turnKey);
		Log.d(TAG, "turnkey Content: "+ this.get(turnKey));
		this.put(turnKey, creator); // create the turn element on the server

		System.out.println(turnKey + " is now: " + creator);

		// create the entered words element on the server
		String enteredKey = ENTERED_WORDS_PREFIX + userskey;
		this.put(enteredKey, "");

		String scoresKey = SCORES_PREFIX + userskey;
		String scoresContent = creator + "|0," + opponent + "|0";
		this.put(scoresKey, scoresContent);

		Log.d(TAG, "scores key: "+ scoresKey);
		Log.d(TAG, "scores Content: "+ this.get(scoresKey));
		System.out.println(scoresKey + " is now: " + creator + "|0," + opponent + "|0");

		// create the number of turns element on the server
		String numTurnsKey = NUM_TURNS_PREFIX + userskey;
		this.put(numTurnsKey, "0");
	}

	/**
	 * Updates the score for the game 
	 * @param user1
	 * @param score_1
	 * @param user2
	 * @param score_2
	 */
	public void setScoreContent(String user1, int score_1, String user2, int score_2){
		String userskey = this.getUsersKey(user1, user2);
		String scoresKey = SCORES_PREFIX + userskey;
		String scoresContent = user1 + "|" +score_1+ "," + user2 + "|"+score_2;
		this.put(scoresKey, scoresContent);
	}

	/**
	 * Gets the board for the game. 
	 * @param user1
	 * @param user2
	 * @return
	 */
	public String getBooard(String user1, String user2){
		String userskey = this.getUsersKey(user1, user2);
		// add board to the server
		String boardKey = BOARD_PREFIX + userskey;
		return this.get(boardKey);
	}



	/**
	 * Send a request from user1 to user2
	 * @param user1 The sender of the request
	 * @param user2 The receiver of the request
	 */
	public void sendRequest(String user1, String user2, final OnBooleanReceivedListener booleanListener) {

		final ServerAccessor thisSA = this;
		Log.d(TAG, "About to create an AsyncTask sendRequestTask");
		class sendRequestTask extends AsyncTask<String, Integer, Boolean> {

			protected Boolean doInBackground(String... key) {

				// if we can't connect to the server, stop and return false.
				if (!thisSA.canConnect()) {
					return false;
				}

				Log.d(TAG, "in doInBackground for sendRequestTask");
				String user1 = key[0];
				String user2 = key[1];

				try {
					thisSA.addRequest(user1, user2);
					thisSA.addReceived(user2, user1);
				} catch(Exception e) {
					return false;
				}
				Log.d(TAG, "Added the request successfully");
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute for sendRequestTask. Result: " +result);
				booleanListener.run(result);
			}
		}

		try {
			new sendRequestTask().execute(user1, user2);
		} catch(Exception e) {
			Log.e(TAG, "sendRequestTask thread died: " +e);
		}
	}

	/**
	 * Remove a sent request from user1 to user2
	 * @param user1 The sender of the request to be removed
	 * @param user2 The receiver of the request to be removed
	 */
	public void removeSentRequest(String user1, String user2, final OnBooleanReceivedListener booleanListener) {
		final ServerAccessor thisSA = this;
		class removeSentRequestTask extends AsyncTask<String, Integer, Boolean> {
			protected Boolean doInBackground(String... keys) {
				String user1 = keys[0];
				String user2 = keys[1];

				Log.d(TAG, "In doInBackground for removeSentRequest");
				if (!thisSA.canConnect()) {
					return false;
				}
				try {
					thisSA.removeReceivedRequest(user2, user1);
					thisSA.removeRequest(user1, user2);
				} catch(Exception e) {
					return false;
				}
				Log.d(TAG, "removed request successfully");
				return true;
			}
			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute for removeSentRequest. Result: "+result);
				booleanListener.run(result);
			}
		}
		try {
			new removeSentRequestTask().execute(user1, user2);
		} catch(Exception e) {
			Log.e(TAG,  "removeRequestTask thread died: " +e);
		}
	}

	// TODO: create a scores interpreter
	// TODO: create a numTurns interpreter
	// TODO: create a board interpreter
	// TODO: create an enteredKey interpreter
	// TODO: make a (makeTurn) method that takes in a users' name, a new board,
	//        the user they're playing against, that users' updated score, and
	//        the updated entered word list....or if we choose to store everything 
	//        on the server, the word that the user just entered(might be cleaner).
	//        turn_... should update itself.

	protected static String generateBoard() {
		Log.d(TAG, "Pick Dice");
		int num_dice = size * size;  
		List<Character> set_letters = new ArrayList<Character>(num_dice);
		for (int i = 0; i<num_dice; i++){
			int x = (int) Math.floor(Math.random() * 6);	// pick letter in list of characters
			int d = (int) Math.floor(Math.random() * dice_set.length); // pick dice in collection
			char c = dice_set[d].charAt(x);
			set_letters.add(c);
		}
		Collections.shuffle(set_letters);
		String final_letters = "";
		for (Character s : set_letters){
			final_letters += s;
		}
		
		Log.d(TAG, "Pick letters: " + final_letters);
		return final_letters;	   
	}

	/**
	 * Build a string representation of the board for state saving
	 * @param board	The list representation of the board
	 * @return		The string representation of the board
	 */
	static private String boardToString(List<Character> board) {
		StringBuilder builder = new StringBuilder();
		for (Character ch : board) {
			builder.append(ch);
		}
		return builder.toString();
	}

	/**
	 * Convert a string to a board
	 * @param rep	The string representation of the board
	 * @return		The list representation of the board
	 */
	protected ArrayList<Character> stringToBoard(String rep) {
		ArrayList<Character> board = new ArrayList<Character>();
		for (int i = 0; i < rep.length(); i++) {
			board.add(rep.charAt(i));
		}
		return board;
	}

	public void createNewGame(String user1, String user2, final OnBooleanReceivedListener l) {

		final String usersKey = this.getUsersKey(user1, user2);
		final ServerAccessor thisSA = this;

		Log.d(TAG, "about to create an asynctask CreateGameTask");

		class CreateGameTask extends AsyncTask<String, Integer, Boolean> {

			protected Boolean doInBackground(String... key) {
				String user1 = key[0];
				String user2 = key[1];
				System.out.println("Creating a new game!");
				if (!thisSA.canConnect()) {
					return false;
				}
				String board = generateBoard();
				thisSA.initializeNewGame(user1, user2, board);

				// put updated games list into the server
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute for createGameTask");
				l.run(result);
			}
		}
		try {
			new CreateGameTask().execute(user1, user2);
		} catch(Exception e) {
			Log.e(TAG, "CreateGameTask thread died: "+e);
		}
	}

	public void updateBoard(String user1, String user2, String board) {
		String boardKey = BOARD_PREFIX + this.getUsersKey(user1, user2);
		String boardVal = board;
		this.put(boardKey, boardVal);
	}

	public GameFunctions getGame(String user1, String user2) {

		Log.d(TAG, "USER1: " + user1 + "|");
		Log.d(TAG, "USER2: " + user2 + "|");

		String userskey = this.getUsersKey(user1, user2);

		Log.d(TAG, "getGame userKey: "+ userskey);

		String turnKey = TURN_PREFIX + userskey;
		String currentTurn = 
				this.get(turnKey);

		String boardKey = BOARD_PREFIX + userskey;
		Log.d(TAG, "getGame boardKey: "+ boardKey);
		String board = this.get(boardKey);
		Log.d(TAG, "getGame boardLetters: "+ board.toString());

		String enteredWordsKey = ENTERED_WORDS_PREFIX + userskey;
		ArrayList<String> enteredWords =
				this.stringToArrayList(this.get(enteredWordsKey));

		String scoresKey = SCORES_PREFIX + userskey;
		Log.d(TAG, "getGame scoresKey: "+ scoresKey);
		String gameScores = this.get(scoresKey);
		Hashtable<String, Integer> scores = this.scoresStringToHashtable(
				gameScores);

		String numTurnsKey = NUM_TURNS_PREFIX + userskey;
		int numTurns = Integer.parseInt(this.get(numTurnsKey));

		return new GameFunctions(
				currentTurn,
				board,
				enteredWords,
				scores,
				numTurns);
	}

	/**
	 * Converts a given string representation of scores to a hashtable
	 * The string looks like: "user1|00,user2|00"
	 * @param scores the string representation to convert
	 * @return the hashtable conversion
	 */
	private Hashtable<String, Integer> scoresStringToHashtable(String scores) {

		//TODO: THIS AIN'T WORKIN'
		Log.d(TAG, "scoresToString: "+ scores);
		ArrayList<String> parts = new ArrayList<String>(
				Arrays.asList(scores.split(",")));
		System.out.println("Parts is: "+ parts.toString());
		Hashtable<String, Integer> scoresDic = new Hashtable<String, Integer>();
		for (String usrScore : parts) {
			List<String> part = Arrays.asList(usrScore.split("\\|"));
			System.out.println("part is now: " +part.toString());
			System.out.println("parsing " + part.get(1) +" as an integer.");
			scoresDic.put(part.get(0), Integer.parseInt(part.get(1)));
		}
		return scoresDic;
	}

	/**
	 * Returns the Current turn of the the Game 
	 * @param user1 << Host
	 * @param user2 << Opponent
	 * @return The string representation of the current turn of the game
	 */
	public String getTurn(String user1, String user2){
		String userskey = this.getUsersKey(user1, user2);
		String turnKey = TURN_PREFIX + userskey;
		String currentTurn = this.get(turnKey);
		return currentTurn;
	}

	/**
	 * Sets the Games current player's turn
	 * @param user1 << Host
	 * @param user2 << Opponent
	 * @param The player's turn in the game 
	 * @return The string representation of the current turn of the game
	 */
	public void setTurn(String user1, String user2, String turn){
		String userskey = this.getUsersKey(user1, user2);
		String turnKey = TURN_PREFIX + userskey;
		this.put(turnKey, turn);
	}

	/**
	 * Sets the number of turns passed
	 * @param user1 << host
	 * @param user2 << opponent
	 * @param numTurns
	 */
	public void setTurnTotal(String user1, String user2, int numTurns, final OnBooleanReceivedListener l){
		
		final String usersKey = this.getUsersKey(user1, user2);
		final String numTurnsKey = NUM_TURNS_PREFIX + usersKey;
		final ServerAccessor thisSA = this;
		final int nTurns = numTurns;
		Log.d(TAG, "about to create an asynctask setTurnTotalTask");

		class setTurnTotalTask extends AsyncTask<String, Integer, Boolean> {

			protected Boolean doInBackground(String... key) {
				System.out.println("Setting the turn totals!");
				if (!thisSA.canConnect()) {
					return false;
				}
				thisSA.put(numTurnsKey, Integer.toString(nTurns));
				// this totally worked even though i didnt check.  trust me.
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute for setTurnTotalTask");
				l.run(result);
			}
		}
		try {
			new setTurnTotalTask().execute();
		} catch(Exception e) {
			Log.e(TAG, "setTurnTotalTask thread died: "+e);
		}
	}
	
	
	/**
	 * Sets the enteredWords for the game
	 * @param user1
	 * @param user2
	 * @param usedWords
	 */
	public void setEnteredWords(String user1, String user2, String usedWords, final OnBooleanReceivedListener l){
		final ServerAccessor thisSA = this;
		final String usersKey = this.getUsersKey(user1, user2);
		final String enteredWordsKey = ENTERED_WORDS_PREFIX + usersKey;
		class setEnteredWordsTask extends AsyncTask<String, Integer, Boolean> {

			protected Boolean doInBackground(String... key) {
				String usedWords = key[0];
				System.out.println("Setting the entered words list!");
				if (!thisSA.canConnect()) {
					return false;
				}
				thisSA.put(enteredWordsKey, usedWords);
				// this totally worked even though i didnt check.  trust me.
				return true;
			}

			protected void onPostExecute(Boolean result) {
				Log.d(TAG, "in onPostExecute for setEnteredWordsTask");
				l.run(result);
			}
		}
		try {
			new setEnteredWordsTask().execute(usedWords);
		} catch(Exception e) {
			Log.e(TAG, "setEnteredWordsTask thread died: "+e);
		}
	}
	
	/**
	 * Gets the enteredWords for the game
	 * @param user1
	 * @param user2
	 * @param usedWords
	 */
	public String getEnteredWords(String user1, String user2){
		String userskey = this.getUsersKey(user1, user2);
		String enteredWordsKey = ENTERED_WORDS_PREFIX + userskey;
		return this.get(enteredWordsKey);
	}
	
	public void setHighscore(String user1, int score){
		String enternedHighScore = HIGHSCORE_PREFIX + user1;
		this.put(enternedHighScore, Integer.toString(score));
	}
	
	public int getHighscore(String user1){
		String enternedHighScore = HIGHSCORE_PREFIX + user1;
		int score = Integer.parseInt(this.get(enternedHighScore));
		return score;
	}


}
