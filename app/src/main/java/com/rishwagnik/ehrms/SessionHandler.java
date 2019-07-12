package com.rishwagnik.ehrms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.HashMap;

public class SessionHandler {

    public static class SessionManager{

        SharedPreferences preferences;
        Editor editor;
        Context _context;
        int PRIVATE_MODE = 0;
        private static final String PREF_NAME = "UserPref";
        private static final String IS_LOGIN = "IsLoggedIn";
        private static final String KEY_EMAIL = "email";

        public SessionManager(Context context){

            this._context = context;
            preferences = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
            editor = preferences.edit();

        }

        public void createLoginSession(String empId){

            editor.putBoolean(IS_LOGIN,true);
            editor.putString(KEY_EMAIL,empId);
            editor.commit();

        }

        public void checkLogin(){

            /*if(!this.isLoggedIn()){

                Intent i = new Intent(_context, LoginActivity.class);
                //Clear All Activities
                *//*i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Add Flag to Start New Activity
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*//*
                _context.startActivity(i);

            }
            else {*/
            if(this.isLoggedIn()){

                Intent i = new Intent(_context, MainActivity.class);
                //Clear All Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Add Flag to Start New Activity
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);

            }

        }

        public void saveUserBean(EmployeeBean employeeBean){

            Gson gson = new Gson();
            String bean = gson.toJson(employeeBean);
            editor.putString("employee_bean", bean);
            editor.commit();

        }

        public EmployeeBean getUserBean(){

            Gson gson = new Gson();
            String bean = preferences.getString("employee_bean","");
            EmployeeBean employeeBean = gson.fromJson(bean, EmployeeBean.class);
            return employeeBean;

        }

        public void logoutUser(){

            //Clear Shared Preferences
            editor.clear();
            editor.putBoolean(IS_LOGIN,false);
            editor.commit();

            //Redirect to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);

        }

        public boolean isLoggedIn(){
            return preferences.getBoolean(IS_LOGIN,false);
        }

    }

}
