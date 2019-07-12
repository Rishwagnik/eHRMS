package com.rishwagnik.ehrms;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LeaveApprovalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<LeaveApprovalItem> approvalEntryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeaveApprovalAdapter approvalAdapter;
    private SessionHandler.SessionManager session;
    static final String TAG = LeaveApprovalFragment.class.getSimpleName();
    AlertDialogHandler alertDialogHandler;
    SwipeRefreshLayout swipeRefreshLayout;
    Resources res;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        session = new SessionHandler.SessionManager(getActivity().getApplicationContext());
        recyclerView = rootView.findViewById(R.id.sharedRecyclerView);
        approvalAdapter = new LeaveApprovalAdapter(getContext(), approvalEntryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(approvalAdapter);
        alertDialogHandler = new AlertDialogHandler();
        res = getResources();

        //Set up swipeRefreshLayout to refresh list when recyclerview is pulled down
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                new FetchLeaveRequests().execute(res.getString(R.string.leave_approval_url));
            }
        });
        new FetchLeaveRequests().execute(res.getString(R.string.leave_approval_url));

        //populateRecyclerView();

        return rootView;

    }

    @Override
    public void onRefresh() {
        new FetchLeaveRequests().execute(res.getString(R.string.leave_approval_url));
    }

    private class FetchLeaveRequests extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpHandler handler = new HttpHandler();
            JSONObject jsonObject = new JSONObject();
            try {
                EmployeeBean bean = session.getUserBean();
                String emp_id = bean.getEmp_id();
                int role_id = bean.getRole_id();
                jsonObject.put("emp_id", emp_id);
                jsonObject.put("role_id", role_id);
                Log.i("JSON Object", jsonObject.toString());
                String jsonString = handler.uploadData(jsonObject, strings[0]);
                return jsonString;
            } catch (JSONException e) {
                e.printStackTrace();
                return "failure";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Approval Response", s);
            swipeRefreshLayout.setRefreshing(false);
            if(s != null){

                if(s.equals("No Internet")){
                    alertDialogHandler.showAlertDialog(getContext(), "No Internet", "Could not connect to the internet");
                } else {

                    try {

                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for(int i = 0; i < jsonArray.length(); i++){

                            JSONObject obj = jsonArray.getJSONObject(i);

                            //Currently commented out name because because database has null values for it,
                            //using name from session bean instead, uncomment and change when database fixed
                            //String name = obj.getString("firstName") + " " + obj.getString("lastName");
                            String name = session.getUserBean().getFirst_name() + " " + session.getUserBean().getLast_name();

                            String startDate = formatDates(obj.getString("leaveFromDate"));
                            String endDate = formatDates(obj.getString("leaveToDate"));
                            String appDate = formatDates(obj.getString("leaveAppDate"));
                            String contact = obj.getString("contactNo");
                            Long contactNo = Long.parseLong(contact);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

                            long diff = dateFormat.parse(endDate).getTime() - dateFormat.parse(startDate).getTime();

                            int noOfDays = (int)diff/(24*60*60*1000);

                            LeaveApprovalItem entry = new LeaveApprovalItem(
                                                            name,
                                                            obj.getString("leaveTypeName"),
                                                            obj.getString("leaveDayType"),
                                                            appDate,
                                                            startDate,
                                                            endDate,
                                                            obj.getBoolean("ltc"),
                                                            obj.getString("leaveReason"),
                                                            contactNo,
                                                            noOfDays,
                                                            obj.getInt("leaveId"));

                            approvalEntryList.add(entry);
                            approvalAdapter.notifyDataSetChanged();

                        }



                    } catch (Exception e ){

                        alertDialogHandler.showAlertDialog(getContext(), "Invalid Response", "Invalid Response Received From Server");

                    }
                }

            }

        }

    }

    public String formatDates(String unformattedDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
        Date date = dateFormat.parse(unformattedDate);
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        return dateFormat.format(date);

    }


    /*public void populateRecyclerView() {

        LeaveApprovalItem entry = new LeaveApprovalItem("John Doe", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, false);

        approvalEntryList.add(entry);
        entry = new LeaveApprovalItem("John Smith", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, false);
        approvalEntryList.add(entry);
        entry = new LeaveApprovalItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, false);
        approvalEntryList.add(entry);
        entry = new LeaveApprovalItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, false);
        approvalEntryList.add(entry);
        entry = new LeaveApprovalItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, false);
        approvalEntryList.add(entry);

        approvalAdapter.notifyDataSetChanged();

    }*/

}
