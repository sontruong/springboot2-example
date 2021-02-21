package com.one.demo.redis.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(tags = "Redis Employee controller")
@RequestMapping(value = "api")
public class EmployeeController {

       @Autowired
       EmployeeRepository employeeRepository;

       @RequestMapping(value = "employee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
       public Employee getDetail(String id) {
             return employeeRepository.findById(id).orElse(null);
       }

       @RequestMapping(value = "employee", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
       public Employee saveDetail(@RequestBody Employee employee) {
             return employeeRepository.save(employee);
       }

       @RequestMapping(value = "employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
       public Collection<Employee> getAll() {
             Iterator<Employee> source = employeeRepository.findAll().iterator();
             List<Employee> target = new ArrayList<>();
             source.forEachRemaining(target::add);
             return target;
       }
}
