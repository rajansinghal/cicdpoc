package com.technology.controller;

import com.technology.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rsinghal on 4/14/2018.
 */
@RestController
@RequestMapping("/api/v1/employee/")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @RequestMapping(value = "id/{id}", method = RequestMethod.GET)
    public String getCustomerName(@PathVariable String id) {
        return employeeService.getCustomerName(id);
    }

}
