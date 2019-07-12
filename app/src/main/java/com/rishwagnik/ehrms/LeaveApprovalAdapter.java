package com.rishwagnik.ehrms;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveApprovalAdapter extends RecyclerView.Adapter<LeaveApprovalAdapter.MyViewHolder> {

    List<LeaveApprovalItem> approvalList;
    private static int currentPosition = 0;
    Context context;
    AlertDialogHandler alertDialogHandler;
    LeaveApprovalItem currentEntry;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView nameView,
                leaveTypeView,
                applicationDateView,
                startDateView,
                endDateView,
                ltcView,
                reasonView,
                contactView,
                holidayTypeView,
                daysNoView;

        public LinearLayout expanderLayout;
        public CardView cardView;
        boolean isExpanded;
        Button approveButton, rejectButton;

        public MyViewHolder(View view){

            super(view);
            nameView = view.findViewById(R.id.leaveApprovalName);
            leaveTypeView = view.findViewById(R.id.leaveApprovalLeaveType);
            applicationDateView = view.findViewById(R.id.leaveApprovalApplicationDate);
            startDateView = view.findViewById(R.id.leaveApprovalStartDate);
            endDateView = view.findViewById(R.id.leaveApprovalEndDate);
            ltcView = view.findViewById(R.id.leaveApprovalLTC);
            reasonView = view.findViewById(R.id.leaveApprovalReason);
            contactView = view.findViewById(R.id.leaveApprovalContact);
            holidayTypeView = view.findViewById(R.id.leaveApprovalHolidayType);
            expanderLayout = view.findViewById(R.id.recyclerExpanderLayout);
            cardView = view.findViewById(R.id.leaveApprovalCardView);
            daysNoView = view.findViewById(R.id.leaveApprovalNoOfDays);
            isExpanded = false;
            approveButton = view.findViewById(R.id.leaveApproveButton);
            rejectButton = view.findViewById(R.id.leaveRejectButton);
            alertDialogHandler = new AlertDialogHandler();

        }

    }

    public LeaveApprovalAdapter(Context context, List<LeaveApprovalItem> approvalList) {
        this.approvalList = approvalList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final LeaveApprovalItem entry = approvalList.get(position);
        holder.nameView.setText(entry.getEmployeeName());
        holder.leaveTypeView.setText(entry.getLeaveType());
        holder.applicationDateView.setText(entry.getApplicationDate());
        holder.startDateView.setText(entry.getStartDate());
        holder.endDateView.setText(entry.getEndDate());
        holder.reasonView.setText(entry.getReason());
        holder.ltcView.setText(entry.isLTC()?"Yes":"No");
        holder.contactView.setText(Long.toString(entry.getContactNo()));
        holder.daysNoView.setText(Integer.toString(entry.getNoOfDays()));
        currentEntry = entry;

        //Toggle the expandable part of the item
        holder.expanderLayout.setVisibility(View.GONE);
        if(currentPosition == position){

            if(holder.isExpanded){

                Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                holder.expanderLayout.startAnimation(slideUp);
                holder.isExpanded = false;

            } else{

                Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                holder.expanderLayout.setVisibility(View.VISIBLE);
                holder.expanderLayout.startAnimation(slideDown);
                holder.isExpanded = true;

            }

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
            }
        });

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    JSONObject approvalObject = new JSONObject();
                    //Putting static for now should be changed when proper role authentication is present
                    approvalObject.put("role_id", 2);
                    approvalObject.put("leave_id",entry.getLeaveID());
                    //Putting static for now should be changed when proper role authentication is present
                    approvalObject.put("emp_id", "456456");
                    //jsonObject.put()
                    new SendApprovalStatus().execute(approvalObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_approval, parent, false);
        return new MyViewHolder(view);

    }

    public int getItemCount(){
        return approvalList.size();
    }

    public class SendApprovalStatus extends AsyncTask<JSONObject, Void, String>{

        @Override
        protected String doInBackground(JSONObject... jsonObjects) {

            Resources res = context.getResources();
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.uploadData(jsonObjects[0],res.getString(R.string.approval_response_url));
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s != null){

                if(s.equals("No Internet")){
                    alertDialogHandler.showAlertDialog(context, "No Internet", "Could not connect to the internet");
                } else {

                    Log.i("Approval Sent Response", s);
                    try {

                        JSONObject obj = new JSONObject(s);
                        if(obj.getBoolean("success")){
                            approvalList.remove(currentPosition);
                            notifyItemRemoved(currentPosition);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }
    }

}
