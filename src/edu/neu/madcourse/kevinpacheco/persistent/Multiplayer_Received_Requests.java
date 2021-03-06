package edu.neu.madcourse.kevinpacheco.persistent;

import java.util.ArrayList;
import edu.neu.madcourse.kevinpacheco.R;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Multiplayer_Received_Requests  extends ListActivity{
	private String TAG = "Multiplayer_Received_Requests";
	private static final String BOGGLE_PREF = "edu.madcourse.dancalacci.multiplayer";
	private static final String PREF_USER = "prefUser";
	private ServerAccessor sa;
	private String USERNAME;
	Multiplayer_Received_Request_Adaptor adapter;

	public void setUsername(){
		SharedPreferences pref = getSharedPreferences(BOGGLE_PREF, MODE_PRIVATE);
		USERNAME = pref.getString(PREF_USER, null);
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.d(TAG, "set contentview");
		setContentView(R.layout.multiplayer_received);

		setUsername();

		getListView().setEmptyView(findViewById(android.R.id.empty));

		this.sa = new ServerAccessor();
		
		setAsynchronousListAdapter();
	}

	public void onResume(){
		super.onResume();
		setAsynchronousListAdapter();
	}

	/**
	 * Sets the listAdapter asynchronously, with information from
	 * the server.
	 */
	private void setAsynchronousListAdapter() {
		final Multiplayer_Received_Requests thisActivity = this;
		
		sa.getReceivedRequests(USERNAME, new OnStringArrayListLoadedListener() {
			public void run(ArrayList<String> list) {
				adapter = new Multiplayer_Received_Request_Adaptor(thisActivity, R.layout.multiplayer_received, list);
				Log.v(TAG, "Setting List adapter: " +adapter.toString());
				setListAdapter(adapter);
			}
		});
	}
	
	final Multiplayer_Received_Requests thisActivity = this;

	public class Multiplayer_Received_Request_Adaptor extends BaseAdapter{
		private ArrayList<String> mReceivedRequests = new ArrayList<String>();
		private Context mContext;
		private int rowResID;

		public Multiplayer_Received_Request_Adaptor(Context c, int rowResID, ArrayList<String> receivedRequestsList) {
			mContext = c;
			mReceivedRequests = receivedRequestsList;
			this.rowResID = rowResID;
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return mReceivedRequests.size();
		}


		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}


		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public boolean isEmptyList(){
			return mReceivedRequests.contains("");
		}
		
		public boolean hadError() {
			return mReceivedRequests.get(0).startsWith("ERROR");
		}

		public View getView(int position, View view, ViewGroup parent) {
			final String row = this.mReceivedRequests.get(position);
			boolean isEmpty = isEmptyList();
			boolean hadError = hadError();
			Log.d(TAG, "Empty ArrayList "+ isEmpty);
			Log.d(TAG, "Had error " +hadError);

			if(view == null ){
				getListView().setEmptyView(findViewById(android.R.id.empty));
				if(!isEmpty && !hadError){
					Log.d(TAG, "button is there");
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					view = inflater.inflate(R.layout.multiplayer_received_rows, parent, false);
				}else{
					Log.d(TAG, "button isn't there");
					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					view = inflater.inflate(R.layout.multiplayer_received_rows_empty, parent, false);
				}
			}

			TextView username = (TextView) 
					view.findViewById(R.id.multiplayer_received_requests_textView_content);
			if(!isEmpty && !hadError){
				username.setText(mReceivedRequests.get(position));
			} else if (hadError) {
				username.setText("Couldn't retrieve data...");
			} else{
				username.setText("No New Requests");
			}

			// Give it a nice background

			buttonClickHandler btn_Handler = new buttonClickHandler(username, row);

			Button btnAccept = (Button) view.findViewById(R.id.multiplayer_received_accept_button);
			btnAccept.setOnClickListener(btn_Handler);
			btnAccept.setTag(row);

			Button btnReject = (Button) view.findViewById(R.id.multiplayer_received_reject_button);
			btnReject.setOnClickListener(btn_Handler);
			btnReject.setTag(row);

			return view;
		}			


		/**
		 * Delete a row from the list of rows
		 * @param row row to be deleted
		 */
		public void deleteRow(String row) {
			if(this.mReceivedRequests.contains(row)) {
				this.mReceivedRequests.remove(row);
			}
		}
		
		class buttonClickHandler implements View.OnClickListener {
			TextView textView;
			String row;

			public buttonClickHandler(TextView textView, String row ) {
				this.textView = textView;
				this.row = row;
			}

			public void onClick(View v) {
				Button button = (Button) v;
				final String row = (String) button.getTag();

				switch(v.getId()){
				case R.id.multiplayer_received_accept_button:
					// make a "game is loading..." dialog while the game is created.
					AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
			        builder.setMessage(R.string.multiplayer_game_loading);
			        final AlertDialog loadingDialog = builder.create();
			        
			        
			        loadingDialog.show();
					Log.d(TAG, "Accept Button Clicked");
					
					Log.d(TAG, "adding a new game using sa.createNewGame");
					sa.createNewGame(USERNAME, row, new OnBooleanReceivedListener() {
						
						public void run(Boolean exitState) {
							if (!exitState) {
								Toast.makeText(getBaseContext(),
										"Couldn't accept the game.  Try again!", Toast.LENGTH_SHORT).show();
							} else {
								Log.d(TAG, "new game created, removing request");
								
								sa.removeSentRequest(row, USERNAME, new OnBooleanReceivedListener() {
									public void run(Boolean exitState) {
										if (!exitState) {
											Log.d(TAG, "Couldn't remove the request");
											Toast.makeText(getBaseContext(), 
													"We had an error - That request might stay in your received requests list...", 
													Toast.LENGTH_SHORT).show();
										} else {
											loadingDialog.dismiss(); // get rid of the game is loading dialog.
											
											Intent i = new Intent(mContext, Multiplayer_Game.class);
											
											// all game data has been initialized - let's start the game!
											i.putExtra("opponent", row);
											deleteRow(row);
											notifyDataSetChanged();

											startActivity(i);
											

											Log.d(TAG, "Accept Button Clicked Delete Row");
										}
									}
								});
							}
						}
					});
					break;
					
					//row is the user that sent the request that we have to remove
				case R.id.multiplayer_received_reject_button:
					//TODO: Update Request List -> Server Call
					Log.d(TAG, "Reject Button Clicked");
					
					//row is the user that sent the request that we have to remove
					sa.removeSentRequest(row, USERNAME, new OnBooleanReceivedListener() {
						public void run(Boolean exitState) {
							Log.d(TAG, "in exitState of removeSentRequest");
							if (!exitState) {
								Log.d(TAG, "request from "+row+"was not deleted");
								Toast.makeText(getBaseContext(), 
										"Could not reject request from "+row+".  Try again!", 
										Toast.LENGTH_SHORT).show();
							} else {
								Log.d(TAG, "request from "+row+" was deleted");
								Toast.makeText(getBaseContext(), 
										"Request from "+row+" rejected!", 
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					deleteRow(row);
					notifyDataSetChanged();
					Log.d(TAG, "Reject Button Clicked Delete Row");
					break;
				}
			}

		}
	
	}

	public void onMultiplayerReceivedRequestsSendNewRequestsButtonClicked(View v) {
		Intent i = new Intent(this, Multiplayer_New_Request_Form.class);
		startActivity(i);
	}

	public void onMultiplayersReceivedRequestsBackButtonClicked(View v) {
		finish();
	}
}
