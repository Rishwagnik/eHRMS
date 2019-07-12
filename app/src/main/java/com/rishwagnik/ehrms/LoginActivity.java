package com.rishwagnik.ehrms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.annotation.Nullable;
import com.rishwagnik.ehrms.SessionHandler.SessionManager;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText usernameView, passwordView;
    ExtendedFloatingActionButton loginButton;
    AlertDialogHandler alertDialogHandler = new AlertDialogHandler();
    static SessionManager session;
    PasswordEncrypter encrypter = new PasswordEncrypter();
    static final String PKEY_URL = "http://196.1.113.93/getPublicKey";
    static final String LOGIN_URL = "http://196.1.113.93/hrmsAndroid/hrmsLogin.html";
    static final String TAG = LoginActivity.class.getSimpleName();
    String username;
    String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameView = findViewById(R.id.usernameView);
        passwordView = findViewById(R.id.passwordView);
        loginButton = findViewById(R.id.loginButton);

        //Handles removing focus from textview when enter is pressed on the soft keyboard
        handleEnter(usernameView);
        handleEnter(passwordView);

        //Checks if session already exists, ie if user is already loggedin if yes, go to MainActivity
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameView.getText().toString().trim();
                password = passwordView.getText().toString().trim();
                new LoginTask().execute();

            }
        });

    }

    //Handles removing focus from textview when enter is pressed on the soft keyboard, must be applied to each textview separately
    public void handleEnter(final TextInputEditText text){

        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction() == KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)){
                    text.clearFocus();
                }
                return false;
            }
        });

    }

    //Handles removing focus from textview when user taps outside it, must be applied in every activity separately
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    //AsyncTask to try to log in user
    public class LoginTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {

            HttpHandler httpHandler = new HttpHandler();
            String pKey = "";
            String result = "";
            String response = "";
            result = httpHandler.makeServiceCall(PKEY_URL);

            // If public key received
            if(!result.equals("")) {

                try {

                    //Fetch Public Key (To be changed)
                    JSONObject pKeyJSON = new JSONObject(result);
                    pKey = pKeyJSON.getString("public_key");

                    //Encrypt Password using Public Key
                    password = encrypter.encryptPassword(password,pKey);

                    //Create JSON of username and password and send to URL
                    JSONObject loginReq = new JSONObject();
                    loginReq.put("loginUsername", username);
                    loginReq.put("loginPassword", password);

                    //Don't pass Public Key from here
                    loginReq.put("public_key", pKey);
                    response = httpHandler.uploadData(loginReq,LOGIN_URL);
                    if(response.equals("No Internet")){
                        return response;
                    }

                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject tempBean;

                    EmployeeBean employeeBean = new EmployeeBean();

                    // If login attempt returns success
                    if(jsonObj.getBoolean("success")){

                        // Save details to employeeBean
                        employeeBean.setEmp_id(jsonObj.getString("loginUsername"));

                        tempBean = jsonObj.getJSONObject("session");
                        employeeBean.setRole_id(tempBean.getInt("selectedRole"));

                        tempBean = tempBean.getJSONObject("userAccount");
                        employeeBean.setBasic_salary(tempBean.getDouble("bacicPay"));
                        employeeBean.setStore_id(tempBean.getInt("storeId"));
                        employeeBean.setEmployment_type(tempBean.getInt("employmentType"));
                        employeeBean.setFirst_name(tempBean.getString("firstName"));
                        employeeBean.setLast_name(tempBean.getString("lastName"));

                        // Save employeeBean to session
                        session.saveUserBean(employeeBean);

                        Log.i("BeanDetails", "loginUsername: " + employeeBean.getEmp_id() +
                                " selectedRole: " + employeeBean.getRole_id() +
                                " bacicPay: " + employeeBean.getBasic_salary() +
                                " storeId: " + employeeBean.getStore_id() +
                                " employmentType: " + employeeBean.getEmployment_type() +
                                " fistName: " + employeeBean.getFirst_name() +
                                " lastName: " + employeeBean.getLast_name());

                    } else {

                        // If login attempt returns false, show incorrect username/password pop up
                        alertDialogHandler.showAlertDialog(LoginActivity.this, "Login Failed", "Username or Password Incorrect");

                    }

                    //Log.i(TAG, jsonObj.get("message").toString());
                    Log.i(TAG, "Response: "  + response);


                    return "success";

                } catch (JSONException e) {

                    // If there is an issue with the JSON structure, show relevant pop up
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogHandler.showAlertDialog(LoginActivity.this, "Login Failed", "Username or Password Incorrect");
                        }
                    });
                    Log.i(TAG, e.getClass().getName() + ": " + e.getMessage());
                    Log.i(TAG, "Response: "  + response);
                    return "failure";

                } catch (Exception e) {

                    // Generic catch phrase for uncaught errors
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertDialogHandler.showAlertDialog(LoginActivity.this, "Login Failed", "Something Went Wrong!");
                        }
                    });
                    Log.i(TAG, e.getClass().getName() + ": " + e.getMessage());
                    return "failure";

                } // May add more catch phrases for better pop up customization

            }
            else {

                // Error in receiving public key
                return "failure";

            }

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.i(TAG, "Login Request Status: " + s);
            if(session.getUserBean() != null){
                String username = session.getUserBean().getEmp_id();
            }

            if(s.equals("success")){

                // Create login session and redirect to MainActivity
                session.createLoginSession(username);
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();

            }
            if(s.equals("No Internet")){
                alertDialogHandler.showAlertDialog(getApplicationContext(), "Internet Error", "Unable to Connect to Internet");
            }


        }

    }
}
