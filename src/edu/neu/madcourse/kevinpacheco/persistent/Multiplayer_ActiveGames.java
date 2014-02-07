package edu.neu.madcourse.kevinpacheco.persistent;

import java.util.ArrayList;

import edu.neu.madcourse.kevinpacheco.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Multiplayer_ActiveGames extends ListActivity {
	private static final String BOGGLE_PREF = "edu.neu.madcourse.kevinpacheco.persistent";
	private static final String PREF_USER = "prefUser";
	ServerAccessor sa;
	String USERNAME;
	String TAG = "Multiplayer_CurrentGames_Requests";

	Multiplayer_Adaptor adapter;

	public void set_username(){
		Log.d(TAG, "setUsername");
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		this.USERNAME = pref.getString(PREF_USER, null);
		Log.d(TAG, "setUsername: " + USERNAME);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_current_games);

		set_username();

		getListView().setEmptyView(findViewById(android.R.id.empty));

		sa = new ServerAccessor();
		setAsynchronousListAdapter();
	}

	public void onResume(){
		super.onResume();
		getListView().setEmptyView(findViewById(R.id.emptyView));
		setAsynchronousListAdapter();
	}

	/**
	 * Sets the listAdapter asynchronously, with information from
	 * the server.
	 */
	private void setAsynchronousListAdapter() {
		final Multiplayer_ActiveGames thisActivity = this;

		sa.getGames(USERNAME, new OnStringArrayListLoadedListener() {
			public void run(ArrayList<String> list) {
				adapter = new Multiplayer_Adaptor(thisActivity, R.layout.multiplayer_current_games, list);
				Log.v(TAG, "Setting Multiplayer Games List adapter: " +list.toString());
				setListAdapter(adapter);
			}
		});
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//super.onListItemClick(l, v, position, id);
		String opoonent = adapter.get_content(position);
		Log.d(TAG, "onListItemClick: " + opoonent);
		if(! (opoonent.equals("ERROR"))){
			Intent i = new Intent(this, Multiplayer_Game.class);
			// all game data has been initialized - let's start the game!
			i.putExtra("opponent", opoonent);
			
			startActivity(i);
		}
	}

	public class Multiplayer_Adaptor extends BaseAdapter{
		private ArrayList<String> mGames = new ArrayList<String>();

		public Multiplayer_Adaptor(Context c, int rowResID, ArrayList<String> currentGamesList) {
			mGames = currentGamesList;
		}


		public String get_content(int position){
			return mGames.get(position);
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return mGames.size();
		}


		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public boolean check_list(){
			return mGames.contains("");
		}
		
		public boolean check_error() {
			return mGames.get(0).startsWith("ERROR");
		}

		public View getView(int position, View view, ViewGroup parent) {
			boolean isEmpty = check_list();
			boolean hadError = check_error();
			Log.d(TAG, "Empty ArrayList "+ isEmpty);
			Log.d(TAG, "Had error " +hadError);
			
			Log.d(TAG, "current games list: " + mGames.toString());
			if(view == null ){
				getListView().setEmptyView(findViewById(android.R.id.empty));
				if(!isEmpty && !hadError){
					Log.d(TAG, "button is there");
					LayoutInflater in = LayoutInflater.from(parent.getContext());
					view = in.inflate(R.layout.multiplayer_current_games_rows, parent, false);
				}else{
					Log.d(TAG, "button isn't there");
					LayoutInflater in = LayoutInflater.from(parent.getContext());
					view = in.inflate(R.layout.multiplayer_current_games_rows_empty, parent, false);
				}
			}

			TextView p1 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player1);
			if(!isEmpty && !hadError){
				Log.d(TAG, "no error, not empty");
				p1.setText(USERNAME);
			} else if (hadError) {
				Log.d(TAG, "error.  displaying couldnt retrieve...");
				p1.setText("No Current Games Available");
			}else{
				Log.d(TAG, "empty!");
				p1.setText("No Current Games Available");
			}

			TextView p2 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player2);
			p2.setText(mGames.get(position));

			return view;
		}			

		public void PlayerTurn(String currentPlayer, View view){
			String player = null;

			TextView p1 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player1);
			TextView p2 = (TextView) 
					view.findViewById(R.id.multiplayer_current_games_textView_player2);
			String P1 = p1.getText().toString();
			String P2 = p2.getText().toString();

			if (P1.equals(player)){
				p1.setBackgroundColor(getResources().getColor(R.color.puzzle_foreground));
				p2.setBackgroundColor(getResources().getColor(R.color.transparent));
			}else if(P2.equals(player)){
				p2.setBackgroundColor(getResources().getColor(R.color.puzzle_foreground));
				p1.setBackgroundColor(getResources().getColor(R.color.transparent));
			}

		}

	}
	
	public void new_request(View v) {
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	public void back_button(View v) {
		finish();
	}
}
