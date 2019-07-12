package com.rishwagnik.ehrms;

public class LeaveApprovalItem {

    private String employeeName;
    private String leaveType;
    private String applicationDate;
    private String startDate;
    private String endDate;
    private boolean LTC;
    private String reason;
    private long contactNo;
    private boolean approval;
    private String holidayType;
    private int noOfDays;
    private int leaveID;


    public LeaveApprovalItem(String employeeName, String leaveType, String holidayType, String applicationDate, String startDate, String endDate, boolean LTC, String reason, long contactNo, int noOfDays, int leaveID) {

        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.holidayType = holidayType;
        this.applicationDate = applicationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.LTC = LTC;
        this.reason = reason;
        this.contactNo = contactNo;
        this.noOfDays = noOfDays;
        this.leaveID = leaveID;

    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLTC(boolean LTC) {
        this.LTC = LTC;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setContactNo(long contactNo) {
        this.contactNo = contactNo;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public void setLeaveID(int leaveID) { this.leaveID = leaveID; }


    public String getEmployeeName() {
        return employeeName;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isLTC() {
        return LTC;
    }

    public String getReason() {
        return reason;
    }

    public long getContactNo() {
        return contactNo;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public int getNoOfDays() { return noOfDays; }
    public int getLeaveID() { return leaveID; }

    public boolean isApproval() {
        return approval;
    }

}
