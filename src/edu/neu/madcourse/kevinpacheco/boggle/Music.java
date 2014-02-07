/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
***/
package edu.neu.madcourse.kevinpacheco.boggle;

import edu.neu.madcourse.kevinpacheco.R;
import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer m = null;
	
	/** Stop one song and play next one **/
	
	public static void play(Context c, int file) {
		stop(c);
		
		/** Check Settings for music check-box **/
		if (Prefs.getMusic(c)) {
			m = MediaPlayer.create(c, file);
			m.setLooping(true);
			m.start();
        }
	}
   
    /** Play selected music **/
    public static void playClip(Context c, int file){
    	MediaPlayer m = MediaPlayer.create(c, file);  
	    m.start();
    }
   

    /** Stop playing the current music **/
    public static void stop(Context c) { 
       if (m != null) {
    	   m.stop();
    	   m.release();
    	   m = null;
       }
    }
   
    public static void Click_Sound(Context c) {
    	MediaPlayer m = MediaPlayer.create(c, R.raw.button);
    	m.start();
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        	@Override
        	public void onCompletion(MediaPlayer m) {
        		m.release();
            }
        });
    }
	
	public static void Wrong_Sound(Context c) {
		MediaPlayer m = MediaPlayer.create(c, R.raw.wrong);
		m.start();
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        	@Override
        	public void onCompletion(MediaPlayer m) {
        		m.release();
            }
        });
    }
	
	public static void Right_Sound(Context c) {
		MediaPlayer m = MediaPlayer.create(c, R.raw.correct);
		m.start();
		m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer m) {
				m.release();
			}
		});
	}
	
	public static void Clear_Sound(Context c) {
		MediaPlayer m = MediaPlayer.create(c, R.raw.clear);
		m.start();
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        	@Override
        	public void onCompletion(MediaPlayer m) {
        		m.release();
        	}
        });
	}
}
