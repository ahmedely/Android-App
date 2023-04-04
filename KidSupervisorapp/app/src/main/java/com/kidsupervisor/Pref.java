package com.kidsupervisor;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

public class Pref {

    Context mContext;
    private FirebaseAuth auth;

    public Pref(Context context) {

        mContext = context;
        auth=FirebaseAuth.getInstance();
    }

    public void setUserStatus(Boolean x){
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("newUser", x);
        editor.apply();
    }
    public boolean getUserStatus(){
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getBoolean("newUser", true);
    }

    public void setBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getBoolean(key, false);
    }
    //to know if tutorial button is clicked on setting or on login
    public void setPrevFragment(Boolean x){
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putBoolean("Fragment", x);
        editor.apply();
    }
    public Boolean getPrevFragment(){
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getBoolean("Fragment",false);
    }
}