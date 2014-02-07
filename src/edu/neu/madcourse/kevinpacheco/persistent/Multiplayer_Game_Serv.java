package edu.neu.madcourse.kevinpacheco.persistent;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Multiplayer_Game_Serv extends Multiplayer_Intent {
	public Multiplayer_Game_Serv() {
		super("AppService");
	}

	@Override
	protected void doWakefulWork(Intent intent) {

		Toast.makeText(this, "wakeful", Toast.LENGTH_LONG).show();

	}
}