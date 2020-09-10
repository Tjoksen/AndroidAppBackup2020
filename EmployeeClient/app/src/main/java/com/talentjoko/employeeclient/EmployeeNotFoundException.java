package com.talentjoko.employeeclient;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(Long id){
        System.out.println("Employee not found" + id);
    }
}
