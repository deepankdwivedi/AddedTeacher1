package com.added.addedteacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	public static final String PREF_NAME = "TeacherLogin";
	public static final String TEACHER_ID = "tea_id";
	public static final String SCHOOL_ID = "sch_id";

	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn,String tea_id,String sch_id) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		editor.putString( SCHOOL_ID,sch_id);
		editor.putString(TEACHER_ID,tea_id);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}
