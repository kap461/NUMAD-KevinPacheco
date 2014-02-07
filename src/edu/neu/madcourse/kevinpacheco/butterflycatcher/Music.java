package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

public class Music {
   private static MediaPlayer mp = null;
   public static Data music_data;
   public static String TAG = "music";
   /** Stop old song and start new one */
   
   public static void play(Context context, int resource) {
      stop(context);
      Log.d(TAG, "music_data = " + music_data.getInt("music_on"));
      if (music_data.contains("music_on")) {
      	if (music_data.getInt("music_on") != 0) {
	      mp = MediaPlayer.create(context, resource);
	      mp.setLooping(true);
	      mp.start();
      	}
      }
      else {
    	  mp = MediaPlayer.create(context, resource);
	      mp.setLooping(true);
	      mp.start();
      }
      
   }
   
   // plays the specified sound clip
   public static void playClip(Context context, int resource){
	   MediaPlayer mp = MediaPlayer.create(context, resource);  
	   mp.start();
   }
   
   public static boolean isPlaying(Context context) {
	   
	   if (mp != null && mp.isPlaying())
		   return true;
	   else
		   return false;
   }
   

   /** Stop the music */
   public static void stop(Context context) { 
      if (mp != null) {
         mp.stop();
         mp.release();
         mp = null;
      }
   }
}
