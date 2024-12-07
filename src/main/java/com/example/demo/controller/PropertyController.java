package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Property;
import com.example.demo.repository.PropertyRepository;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    // Create a new property
    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    // Get all properties
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // Get property by ID
    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        return propertyRepository.findById(id)
                .map(property -> ResponseEntity.ok().body(property))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update property
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable Integer id, @RequestBody Property propertyDetails) {
        return propertyRepository.findById(id)
                .map(property -> {
                    property.setAddress(propertyDetails.getAddress());
                    property.setCity(propertyDetails.getCity());
                    property.setState(propertyDetails.getState());
                    property.setPrice(propertyDetails.getPrice());
                    // Update other fields as necessary
                    Property updatedProperty = propertyRepository.save(property);
                    return ResponseEntity.ok().body(updatedProperty);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete property
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
        return propertyRepository.findById(id)
                .map(property -> {
                    propertyRepository.delete(property);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get properties by city
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<Property>> getPropertiesByCity(@PathVariable String cityName) {
        List<Property> properties = propertyRepository.findByCity(cityName);
        if (properties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(properties);
    }

    // Get properties above a certain price
    @GetMapping("/above-price/{price}")
    public ResponseEntity<List<Property>> getPropertiesAbovePrice(@PathVariable Double price) {
        List<Property> properties = propertyRepository.findPropertiesAbovePrice(price);
        if (properties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(properties);
    }

    // Get properties with the maximum price
    @GetMapping("/max-priced")
    public ResponseEntity<List<Property>> getMaxPricedProperties() {
        List<Property> properties = propertyRepository.findMaxPricedProperties();
        if (properties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(properties);
    }



}
