package net.derohimat.firebasebasemvp.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import net.derohimat.baseapp.util.BasePreferenceUtils;

public class PreferencesHelper extends BasePreferenceUtils {

    private static SharedPreferences mPref;

    private static final String KEY_USER_ID = "user_id";

    public PreferencesHelper(Context context) {
        mPref = getSharedPreference(context);
    }

    public String getUserId() {
        return mPref.getString(KEY_USER_ID, null);
    }

    public void setUserId(String userId) {
        mPref.edit().putString(KEY_USER_ID, userId).apply();
    }
}
