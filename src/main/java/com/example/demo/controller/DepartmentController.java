package com.example.demo.controller;

import com.example.demo.model.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Create a new department
    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }

    // Get all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Get department by ID
    @GetMapping("/{dnum}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Integer dnum) {
        return departmentRepository.findById(dnum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update department
    @PutMapping("/{dnum}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Integer dnum, @RequestBody Department departmentDetails) {
        return departmentRepository.findById(dnum)
                .map(department -> {
                    department.setDname(departmentDetails.getDname());
                    return ResponseEntity.ok(departmentRepository.save(department));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete department
    @DeleteMapping("/{dnum}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Integer dnum) {
        return departmentRepository.findById(dnum)
                .map(department -> {
                    departmentRepository.delete(department);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
