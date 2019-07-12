package com.rishwagnik.ehrms;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.rishwagnik.ehrms.SessionHandler.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.FragmentDrawerListener{

    Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    private SessionManager session;
    static final String LEAVE_BALANCE_SITE = "https://api.myjson.com/bins/1e642b";
    static final String APPROVAL_DESK_SITE = "http://196.1.113.93/hrmsAndroid/OpenLeaveApprovalDesk.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, new LeaveApplicationFragment());
        fragmentTransaction.commit();
        //new FetchTask().execute(LEAVE_BALANCE_SITE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.logoutMenuButton){
            session.logoutUser();
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new LeaveApplicationFragment();
                title = getString(R.string.title_leave_application);
                break;
            case 1:
                fragment = new LeaveHistoryFragment();
                title = getString(R.string.title_leave_history);
                break;
            case 2:
                fragment = new LeaveApprovalFragment();
                title = getString(R.string.title_leave_approval);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }

    }

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

    /*public class FetchTask extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... strings) {

            //strings[0] --> leave balance
            //strings[1] --> approval list
            HttpHandler handler = new HttpHandler();
            String[] response = new String[2];
            response[0] = handler.makeServiceCall(strings[0]);

            return response;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if(strings[0]!=null){

                try {

                    JSONObject jsonObject = new JSONObject(strings[0]);
                    LeaveApplicationFragment fragment = new LeaveApplicationFragment();
                    fragment.setTable(jsonObject.getString("balance"), jsonObject.getString("pending"), jsonObject.getString("available"));
                    fragment.

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }*/

}
