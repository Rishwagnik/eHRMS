package com.rishwagnik.ehrms;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rishwagnik.ehrms.SessionHandler.SessionManager;

public class LeaveApplicationFragment extends Fragment {

    public LeaveApplicationFragment() {
    }

    static final String TAG = LeaveApplicationFragment.class.getSimpleName();
    boolean tableVisible;
    AppCompatTextView balanceTextView;
    AppCompatTextView pendingTextView;
    AppCompatTextView availableTextView;
    RelativeLayout calendarContainer;
    CalendarView calendarView;
    LinearLayout formContainer;
    TextInputEditText[] formFields;
    JSONObject formJSON;
    MaterialCheckBox availLTC;
    TableHandler tableHandler;
    SessionManager session;
    ArrayList<String> leaveTypeList;
    ArrayList<String> holidayTypeList;
    TextInputEditText contactText;
    AlertDialogHandler alertDialogHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_leave_application, container, false);

        final ExtendedFloatingActionButton leaveBalanceButton = rootView.findViewById(R.id.leaveBalanceButton);
        ExtendedFloatingActionButton submitButton = rootView.findViewById(R.id.submitButton);
        final CardView leaveTableView = rootView.findViewById(R.id.leaveTableView);
        calendarContainer = rootView.findViewById(R.id.calendarLayout);
        calendarView = rootView.findViewById(R.id.calendarView);
        formContainer = rootView.findViewById(R.id.formContainer);
        final Spinner leaveTypeSpinner = rootView.findViewById(R.id.leaveTypeSpinner);
        final Spinner holidayTypeSpinner = rootView.findViewById(R.id.holidayTypeSpinner);
        availLTC = rootView.findViewById(R.id.availLTC);
        session = new SessionManager(getActivity().getApplicationContext());
        alertDialogHandler = new AlertDialogHandler();

        final TextInputEditText leaveReasonText = rootView.findViewById(R.id.leaveReasonText);
        final TextInputEditText leaveTypeText = rootView.findViewById(R.id.leaveTypeText);
        contactText = rootView.findViewById(R.id.contactText);
        final TextInputEditText addressText = rootView.findViewById(R.id.addressText);
        final TextInputEditText startDateText = rootView.findViewById(R.id.startDateText);
        final TextInputEditText endDateText = rootView.findViewById(R.id.endDateText);
        final TextInputEditText holidayTypeText = rootView.findViewById(R.id.holidayTypeText);

        formJSON = new JSONObject();
        tableHandler = new TableHandler();

        handleEnter(leaveReasonText);
        handleEnter(contactText);
        handleEnter(addressText);

        formFields = new TextInputEditText[]{leaveReasonText, leaveTypeText, contactText, addressText, addressText, startDateText, endDateText, holidayTypeText};


        startDateText.setShowSoftInputOnFocus(false);
        endDateText.setShowSoftInputOnFocus(false);


        balanceTextView = rootView.findViewById(R.id.balanceTextView);
        pendingTextView = rootView.findViewById(R.id.pendingTextView);
        availableTextView = rootView.findViewById(R.id.availableTextView);
        tableVisible = false;


        /*<item>Casual Leave</item>
        <item>Restricted Holiday</item>
        <item>Earned Leave</item>
        <item>Special Leave Without Pay</item>
        <item>Special Casual Leave</item>
        <item>Paternity Leave</item>
        <item>Half Paid Leave</item>
        <item>Study Leave</item>
        <item>Contract Leave</item>
        <item>Child Care Leave</item>
        <item>Hospital Leave</item>
        <item>Special Disability Leave</item>
        <item>Child Adoption Leave</item>
        <item>Maternity Leave</item>*/

        leaveTypeList = new ArrayList<>();
        leaveTypeList.add("Casual Leave");
        leaveTypeList.add("Earned Leave");
        leaveTypeList.add("Special Leave Without Pay");
        leaveTypeList.add("Special Casual Leave");
        leaveTypeList.add("Paternity Leave");
        leaveTypeList.add("Half Paid Leave");
        leaveTypeList.add("Study Leave");
        leaveTypeList.add("Contract Leave");
        leaveTypeList.add("Child Care Leave");
        leaveTypeList.add("Hospital Leave");
        leaveTypeList.add("Special Disability Leave");
        leaveTypeList.add("Child Adoption Leave");
        leaveTypeList.add("Maternity Leave");
        leaveTypeList.add("");

        holidayTypeList = new ArrayList<>();
        holidayTypeList.add("Full Day");
        holidayTypeList.add("Half Day");
        holidayTypeList.add("Second Half");
        holidayTypeList.add("");

        //new FetchJSONTask().execute();

        leaveBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tableVisible) {

                    tableHandler.collapseTable(leaveTableView);
                    leaveBalanceButton.setText("Leave Balance");

                } else {
                    tableHandler.revealTable(leaveTableView);
                    leaveBalanceButton.setText("Collapse");
                }

                tableVisible = !tableVisible;

            }
        });


        // Disable keyboard on clicking holidy type text view
        holidayTypeText.setShowSoftInputOnFocus(false);
        // Set up holiday type text field to set off spinner set behind it on click
        holidayTypeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    holidayTypeSpinner.performClick();

                } else {

                    leaveTypeSpinner.clearFocus();

                }

            }
        });

        holidayTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holidayTypeSpinner.performClick();

            }
        });

        // Disable keyboard on clicking leave type text view
        leaveTypeText.setShowSoftInputOnFocus(false);
        // Set up leave type text field to set off spinner set behind it on click
        leaveTypeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    leaveTypeSpinner.performClick();

                } else {

                    leaveTypeSpinner.clearFocus();

                }

            }
        });

        leaveTypeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leaveTypeSpinner.performClick();

            }
        });

        Resources res = getResources();
        Object[] intermediateList = leaveTypeList.toArray();
        String[] leaveTypes = Arrays.copyOf(intermediateList, intermediateList.length, String[].class);
        ArrayAdapter<String> leaveTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, leaveTypes);
        leaveTypeSpinner.setAdapter(leaveTypeAdapter);
        leaveTypeSpinner.setSelection(13, false);
        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = leaveTypeSpinner.getSelectedItem().toString();
                leaveTypeText.setText(item);
                leaveTypeText.clearFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        intermediateList = holidayTypeList.toArray();
        String[] holidayTypes = Arrays.copyOf(intermediateList, intermediateList.length, String[].class);
        ArrayAdapter<String> holidayTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, holidayTypes);
        holidayTypeSpinner.setAdapter(holidayTypeAdapter);
        holidayTypeSpinner.setSelection(3, false);
        holidayTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = holidayTypeSpinner.getSelectedItem().toString();
                holidayTypeText.setText(item);
                holidayTypeText.clearFocus();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate(startDateText);

            }
        });

        startDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    setDate(startDateText);
                }

            }
        });

        endDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDate(endDateText);

            }
        });

        endDateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    setDate(endDateText);
                }

            }
        });

        calendarContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calendarContainer.setVisibility(View.GONE);

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean filled = checkFormValidity(formFields);
                if (!filled) {
                    Toast.makeText(getActivity(), "Please fill all form fields.", Toast.LENGTH_SHORT).show();
                } else {
                    try {

                        formJSON.put("leave_from_date", startDateText.getText().toString());
                        formJSON.put("leave_to_date", endDateText.getText().toString());
                        formJSON.put("leave_reason", leaveReasonText.getText().toString());
                        formJSON.put("contact_no", Long.parseLong(contactText.getText().toString()));
                        formJSON.put("contact_address", addressText.getText().toString());
                        //formJSON.put("leave_day_type",leaveTypeText.getText().toString());
                        formJSON.put("is_ltc", availLTC.isChecked());
                        formJSON.put("leave_type_id", leaveTypeList.indexOf(leaveTypeText.getText().toString()));
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        String appDate = dateFormat.format(date);
                        //formJSON.put("leave_app_date", appDate);
                        StringBuilder builder = new StringBuilder();
                        for (char c : holidayTypeText.getText().toString().toCharArray()) {
                            if (Character.isUpperCase(c)) {
                                builder.append(c);
                            }
                        }
                        formJSON.put("leave_day_type", builder.toString());
                        formJSON.put("draftORsubmit", 1);

                        EmployeeBean bean = session.getUserBean();
                        formJSON.put("emp_id", bean.getEmp_id());
                        formJSON.put("employment_type", bean.getEmployment_type());

                        Log.i("JSON", formJSON.toString());
                        Resources res = getResources();
                        new UploadJSON().execute(res.getString(R.string.leave_application_url));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setTable(String balance, String pending, String available) {

        balanceTextView.setText(balance);
        pendingTextView.setText(pending);
        availableTextView.setText(available);

    }

    void setDate(final TextInputEditText text) {

        calendarContainer.setVisibility(View.VISIBLE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = dateFormat.format(date);
                text.setText(formattedDate);
                calendarContainer.setVisibility(View.GONE);
                text.clearFocus();

            }
        });

    }

    public void handleEnter(final TextInputEditText text) {

        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    text.clearFocus();
                }
                return false;
            }
        });

    }

    public boolean checkFormValidity(TextInputEditText[] fields) {

        boolean filled = true;
        for (int i = 0; i < fields.length; i++) {

            if (fields[i].getText().toString().matches("")) {
                filled = false;
                break;
            }

        }

        //Check validity of contact no
        if (contactText.getText().toString().length() != 10) {
            filled = false;
        }
        return filled;

    }

    public void clearForm(TextInputEditText[] fields) {


        for (int i = 0; i < fields.length; i++) {
            fields[i].setText("");
        }
        availLTC.setChecked(false);

    }



    /*private class FetchJSONTask extends AsyncTask<Void,Void, HashMap<String,String>> {

        @Override
        protected HashMap<String,String> doInBackground(Void... voids) {
            HashMap<String,String> leaveMap = new HashMap<>();
            HttpHandler handler = new HttpHandler();
            String JSONString = handler.makeServiceCall("https://api.myjson.com/bins/1e642b");
            Log.i(TAG,"Response From URL: " + JSONString);

            if(JSONString != null){

                try{

                    JSONObject leaveBalance = new JSONObject(JSONString);
                    leaveMap.put("balance", leaveBalance.get("balance").toString());
                    leaveMap.put("pending", leaveBalance.get("pending").toString());
                    leaveMap.put("available", leaveBalance.get("available").toString());

                    return leaveMap;

                } catch (Exception e ){
                    Log.e(TAG, e.getClass().getName() + ": " + e.getMessage());
                }

            } else {

               *//* getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Failed To Fetch JSON", Toast.LENGTH_SHORT).show();
                    }
                });*//*

            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String,String> stringHashMap) {
            super.onPostExecute(stringHashMap);

            if(stringHashMap != null){

                setTable(
                        stringHashMap.get("balance"),
                        stringHashMap.get("pending"),
                        stringHashMap.get("available")
                );

            }

        }

    } */

    private class UploadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.uploadData(formJSON, strings[0]);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {

                Log.i("Form Upload Response", s);
                if (s.equals("No Internet")) {
                    alertDialogHandler.showAlertDialog(getContext(), "No Internet", "Could not connect to the internet");
                    //Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        JSONObject json = new JSONObject(s);
                        if (json.getBoolean("success")) {
                            //Toast.makeText(getContext(), "Form Submitted", Toast.LENGTH_SHORT).show();
                            alertDialogHandler.showAlertDialog(getContext(), "Form Submitted", "Your form has been successfully submitted to server");
                            clearForm(formFields);
                        } else {
                            //Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                            alertDialogHandler.showAlertDialog(getContext(), "Form Not Submitted", "Something went wrong and the form could not be submitted");
                        }

                    } catch (JSONException e) {
                        //Toast.makeText(getContext(), "Invalid JSON", Toast.LENGTH_SHORT).show();
                        alertDialogHandler.showAlertDialog(getContext(), "Form Not Submitted", "Something went wrong and the form could not be submitted");
                        e.printStackTrace();

                    }

                }

            }

        }

    }

}


