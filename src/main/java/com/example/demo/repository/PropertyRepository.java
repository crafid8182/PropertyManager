package com.example.demo.repository;

import com.example.demo.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

    // 1) Find properties by city
    List<Property> findByCity(String city);

    // 2) Find properties above a certain price
    @Query("SELECT p FROM Property p WHERE p.price > :price")
    List<Property> findPropertiesAbovePrice(@Param("price") Double price);

    // 3) Find properties with the maximum price
    @Query("SELECT p FROM Property p WHERE p.price = (SELECT MAX(p2.price) FROM Property p2)")
    List<Property> findMaxPricedProperties();
}
