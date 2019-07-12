package com.rishwagnik.ehrms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.MyViewHolder> {

    List<LeaveHistoryItem> historyList;

    public LeaveHistoryAdapter(List<LeaveHistoryItem> historyList) {
        this.historyList = historyList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView nameView,
        leaveIdView,
        applicationDateView,
        startDateView,
        endDateView,
        ltcView,
        reasonView,
        contactView,
        statusView;

        public MyViewHolder(View view) {

            super(view);
            nameView = view.findViewById(R.id.leaveHistoryName);
            leaveIdView = view.findViewById(R.id.leaveHistoryId);
            applicationDateView = view.findViewById(R.id.leaveHistoryApplicationDate);
            startDateView = view.findViewById(R.id.leaveHistoryStartDate);
            endDateView = view.findViewById(R.id.leaveHistoryEndDate);
            ltcView = view.findViewById(R.id.leaveHistoryLTC);
            reasonView = view.findViewById(R.id.leaveHistoryReason);
            contactView = view.findViewById(R.id.leaveHistoryContact);
            statusView = view.findViewById(R.id.leaveHistoryStatus);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        LeaveHistoryItem entry = historyList.get(position);
        holder.nameView.setText(entry.getEmployeeName());
        holder.leaveIdView.setText(Integer.toString(entry.getLeaveID()));
        holder.applicationDateView.setText(entry.getApplicationDate());
        holder.startDateView.setText(entry.getStartDate());
        holder.endDateView.setText(entry.getEndDate());
        holder.reasonView.setText(entry.getReason());
        holder.ltcView.setText(entry.isLTC()?"Yes":"No");
        holder.contactView.setText(Integer.toString(entry.getContactNo()));
        holder.statusView.setText(entry.getStatus());

    }

    @NonNull
    @Override
    public LeaveHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_history, parent, false);
        return new MyViewHolder(view);

    }

    public int getItemCount(){
        return historyList.size();
    }

}
