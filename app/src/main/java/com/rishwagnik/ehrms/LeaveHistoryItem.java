package com.rishwagnik.ehrms;

public class LeaveHistoryItem {

    private String employeeName;
    private int leaveID;
    private String applicationDate;
    private String startDate;
    private String endDate;
    private boolean LTC;
    private String reason;
    private int contactNo;
    private String status;

    public LeaveHistoryItem(String employeeName, int leaveID, String applicationDate, String startDate, String endDate, boolean LTC, String reason, int contactNo, String status) {
        this.employeeName = employeeName;
        this.leaveID = leaveID;
        this.applicationDate = applicationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.LTC = LTC;
        this.reason = reason;
        this.contactNo = contactNo;
        this.status = status;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(int leaveID) {
        this.leaveID = leaveID;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isLTC() {
        return LTC;
    }

    public void setLTC(boolean LTC) {
        this.LTC = LTC;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
