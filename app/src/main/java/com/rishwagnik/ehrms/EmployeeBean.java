package com.rishwagnik.ehrms;

public class EmployeeBean implements java.io.Serializable {

    public EmployeeBean() {}

    private String emp_id;
    private int role_id;
    private double basic_salary;
    private int store_id;
    private int employment_type;
    private String first_name;
    private String last_name;

    public String getEmp_id() {
        return emp_id;
    }
    public int getRole_id() {
        return role_id;
    }
    public double getBasic_salary() {
        return basic_salary;
    }
    public int getStore_id() {
        return store_id;
    }
    public int getEmployment_type() {
        return employment_type;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }



    public void setEmp_id(String emp_id) { this.emp_id = emp_id; }
    public void setRole_id(int role_id) { this.role_id = role_id; }
    public void setBasic_salary(double basic_salary) {
        this.basic_salary = basic_salary;
    }
    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
    public void setBasic_salary(float basic_salary) {
        this.basic_salary = basic_salary;
    }
    public void setEmployment_type(int employment_type) {
        this.employment_type = employment_type;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

}
