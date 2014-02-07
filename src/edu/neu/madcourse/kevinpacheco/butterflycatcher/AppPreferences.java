package edu.neu.madcourse.kevinpacheco.butterflycatcher;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences _sharedPrefs;
    private Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getSavedString(String key) {
        return _sharedPrefs.getString(key, "");
    }

    public void saveStringBody(String key, String text) {
        _prefsEditor.putString(key, text);
        _prefsEditor.commit();
    }
    
    public float getSavedFloat(String key) {
    	return _sharedPrefs.getFloat(key, 0);
    }
    
    public int getSavedInt(String key) {
    	return _sharedPrefs.getInt(key, 0);
    }
    
    public void saveFloatBody(String key, float f) {
    	_prefsEditor.putFloat(key, f);
        _prefsEditor.commit();
    }
    
    public void saveIntBody(String key, int i) {
    	_prefsEditor.putInt(key, i);
        _prefsEditor.commit();
    }
    
    public void deleteAll() {
    	_prefsEditor.clear().commit();
    }
    
    public boolean contains(String key) {
    	if (_sharedPrefs.getAll().containsKey(key))
    		return true;
    	else
    		return false;
    }
    
    public int getMapSize() {
    	Map<String, ?> m = _sharedPrefs.getAll();
    	return m.size();
    }
    
    public void delete(String key) {
    	_prefsEditor.remove(key).commit();
    }
    
    public void showAll() {
    	//_sharedPrefs.getAll().
    }
}