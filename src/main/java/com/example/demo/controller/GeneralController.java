package com.example.demo.controller;

import com.example.demo.repository.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Map;

import com.example.demo.model.Agent;
import com.example.demo.model.Property;
import com.example.demo.model.Owner;
import com.example.demo.model.Department;
import com.example.demo.model.Appointment;

import com.example.demo.repository.AgentRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.OwnerRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.util.ReflectionUtils;


@RestController
@RequestMapping("/api")
public class GeneralController {

    @Autowired private PropertyRepository propertyRepository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private AgentRepository agentRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private AppointmentRepository appointmentRepository;

    @PostMapping("/data")
    public ResponseEntity<?> insertData(@RequestParam String tableName, @RequestBody Map<String, Object> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            switch (tableName.toLowerCase()) {
                case "property":
                    Property property = objectMapper.convertValue(data, Property.class);

                    if (property.getOwner() != null && property.getOwner().getId() != null) {
                        Owner owner = ownerRepository.findById(property.getOwner().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
                        property.setOwner(owner);
                    }
                    return ResponseEntity.ok(propertyRepository.save(property));

                case "owner":
                    Owner owner = objectMapper.convertValue(data, Owner.class);
                    return ResponseEntity.ok(ownerRepository.save(owner));

                default:
                    return ResponseEntity.badRequest().body("Invalid table name: " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



//    @PostMapping("/data")
//    public ResponseEntity<?> insertData(@RequestParam String tableName, @RequestBody Map<String, Object> data) {
//        try {
//            switch (tableName.toLowerCase()) {
//                case "property":
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//                    // Convert to Property
//                    Property property = objectMapper.convertValue(data, Property.class);
//
//                    // Ensure Owner exists
//                    if (property.getOwner() != null && property.getOwner().getId() != null) {
//                        Owner owner = ownerRepository.findById(property.getOwner().getId())
//                                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
//                        property.setOwner(owner);
//                    }
//
//                    return ResponseEntity.ok(propertyRepository.save(property));
//                default:
//                    return ResponseEntity.badRequest().body("Invalid table name: " + tableName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }



    @DeleteMapping("/data/{tableName}/{id}")
    public ResponseEntity<?> deleteData(@PathVariable String tableName, @PathVariable Integer id) {
        switch (tableName.toLowerCase()) {
            case "property":
                if (propertyRepository.existsById(id)) {
                    propertyRepository.deleteById(id);
                    return ResponseEntity.ok("Property deleted successfully");
                }
                break;
            case "owner":
                if (ownerRepository.existsById(id)) {
                    ownerRepository.deleteById(id);
                    return ResponseEntity.ok("Owner deleted successfully");
                }
                break;
            case "agent":
                if (agentRepository.existsById(id)) {
                    agentRepository.deleteById(id);
                    return ResponseEntity.ok("Agent deleted successfully");
                }
                break;
            case "department":
                if (departmentRepository.existsById(id)) {
                    departmentRepository.deleteById(id);
                    return ResponseEntity.ok("Department deleted successfully");
                }
                break;
            case "appointment":
                if (appointmentRepository.existsById(id)) {
                    appointmentRepository.deleteById(id);
                    return ResponseEntity.ok("Appointment deleted successfully");
                }
                break;
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/data")
    public ResponseEntity<?> updateData(@RequestParam String tableName, @RequestParam Integer id, @RequestBody Map<String, Object> updates) {
        switch (tableName.toLowerCase()) {
            case "property":
                return propertyRepository.findById(id).map(property -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Property.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, property, value);
                        }
                    });
                    return ResponseEntity.ok(propertyRepository.save(property));
                }).orElse(ResponseEntity.notFound().build());
            case "owner":
                return ownerRepository.findById(id).map(owner -> {
                    updates.forEach((key, value) -> {
                        Field field = ReflectionUtils.findField(Owner.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, owner, value);
                        }
                    });
                    return ResponseEntity.ok(ownerRepository.save(owner));
                }).orElse(ResponseEntity.notFound().build());
            default:
                return ResponseEntity.badRequest().body("Unsupported table name: " + tableName);
        }
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<?> getAllRecords(@PathVariable String tableName) {
        switch (tableName.toLowerCase()) {
            case "property":
                return ResponseEntity.ok(propertyRepository.findAll());
            case "owner":
                return ResponseEntity.ok(ownerRepository.findAll());
            case "agent":
                return ResponseEntity.ok(agentRepository.findAll());
            case "department":
                return ResponseEntity.ok(departmentRepository.findAll());
            case "appointment":
                return ResponseEntity.ok(appointmentRepository.findAll());
            default:
                return ResponseEntity.badRequest().body("Invalid table name: " + tableName);
        }
    }
}
