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

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getString(key, "");
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
    //for knowing which activity to go after splash screen
    public void setLogInStatus() {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        if(auth.getCurrentUser()!=null)
            editor.putBoolean("logIn",true);
        else
            editor.putBoolean("logIn",false);
        editor.apply();
    }

    public Boolean getLogInStatus(){

        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getBoolean("logIn",false);
    }

}
