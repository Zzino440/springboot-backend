package com.crudapp.springbootbackend.controller;

import com.crudapp.springbootbackend.exception.ResourceNotFoundException;
import com.crudapp.springbootbackend.model.Employee;
import com.crudapp.springbootbackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    //get all employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // create employee rest api
    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    //get employee by id rest api
    @GetMapping("/employees/{id}") //vogliamo mappare il valore di Long id all'inidirizzo {id} attraverso @Pathvariable
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
        return ResponseEntity.ok(employee);
    }

    //update employee rest api
    //Tramite @RequestBody possiamo mappare una richiesta di tipo json in un java object
    /*
    Abbiamo ritrovato l'oggetto Employee dal database tramite l'id e abbiamo fatto l'update dei suoi attributi
    e poi abbiamo messo l'oggetto aggiornato nel database
     */

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeDetails.getEmailId());

        Employee updatedEmployee = employeeRepository.save(employee);
        return  ResponseEntity.ok(updatedEmployee);
    }

    //delete employee rest api
    @DeleteMapping("/employees/{id}")
    public ResponseEntity <Map<String,Boolean>> deleteEmployee(@PathVariable Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));

//Il metodo delete non ritorna nulla quindi creo una mappa che restituisce un messaggio "deleted" quando l'operazione va a buon fine in modo da poter passare questa risposta al client (angular)

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

}
