package com.rishwagnik.ehrms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveHistoryFragment extends Fragment {

    private List<LeaveHistoryItem> historyEntryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LeaveHistoryAdapter historyAdapter;
    private SessionHandler.SessionManager session;

    public LeaveHistoryFragment(){}

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
        historyAdapter = new LeaveHistoryAdapter(historyEntryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);

        populateRecyclerView();

        return rootView;

    }

    /* private class FetchJSONTask extends AsyncTask<Void,Void, HashMap<String,String>> {

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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Failed To Fetch JSON", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String,String> stringHashMap) {
            super.onPostExecute(stringHashMap);

            if(stringHashMap != null){



            }

        }

    }
*/

    public void populateRecyclerView(){

        LeaveHistoryItem entry = new LeaveHistoryItem("John Doe", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, "Pending");

        historyEntryList.add(entry);
        entry = new LeaveHistoryItem("John Smith", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, "Pending");
        historyEntryList.add(entry);
        entry = new LeaveHistoryItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, "Pending");
        historyEntryList.add(entry);
        entry = new LeaveHistoryItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, "Pending");
        historyEntryList.add(entry);
        entry = new LeaveHistoryItem("John", 5, "20/02/19", "22/02/19", "25/02/19",false, "Medical Issues", 999551, "Pending");
        historyEntryList.add(entry);

        historyAdapter.notifyDataSetChanged();

    }

}