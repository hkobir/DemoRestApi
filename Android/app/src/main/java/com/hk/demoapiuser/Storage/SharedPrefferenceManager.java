package com.hk.demoapiuser.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.hk.demoapiuser.ModelClass.User;

public class SharedPrefferenceManager {
    private String SHARED_PREFFERENCE_NAME = "session";
    private static SharedPrefferenceManager mInstance;
    private Context context;

    public SharedPrefferenceManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefferenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefferenceManager(context);
        }
        return mInstance;

    }

    public void saveUser(User user) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("school", user.getSchool());
        editor.apply();

    }

    public boolean isLogIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;


    }

    public User getuser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCE_NAME, Context.MODE_PRIVATE);
        User user = new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("school", null)


        );
        return user;
    }
    public void clear(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
