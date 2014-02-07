package edu.neu.madcourse.kevinpacheco.boggle;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;


public class Dictionary extends Activity{
	
	public Dictionary() {
		super();
	}
	
	/** Searches through dictionary partitions*/
	@SuppressLint("NewApi")
	public boolean searchDict(Context c, int l, String w) throws IOException{
		if (w.isEmpty())
			   return false;


		   char ch = w.charAt(0);
		   AssetManager a = c.getResources().getAssets();
		   try {
			   InputStreamReader r = 
				   new InputStreamReader(a.open(l + "/" + ch + ".jpg"), "UTF-8");
			   BufferedReader br = new BufferedReader(r); 
			   String li;
			   
			   li = br.readLine();
			   
			   while (li !=null) {
				   if (li.equals(w))
					   return true;
				   else
					   li = br.readLine();
				   
			   }
		   } catch (Exception e) {

		   }

		   return false;
	}

}
