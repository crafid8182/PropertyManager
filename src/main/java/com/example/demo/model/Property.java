package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String address;
    private String city;
    private String state;
    private Double price;

    // Many Properties belong to One Owner
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")  // This creates a foreign key column named owner_id in the property table
    @JsonIgnoreProperties("properties")
    private Owner owner;

    @OneToMany(mappedBy = "property")
    private List<Appointment> appointments;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getCity() {return city;}

    public void setCity(String city) {this.city = city;}

    public String getState() {return state;}

    public void setState(String state) {this.state = state;}

    public Double getPrice() {return price;}

    public void setPrice(Double price) {this.price = price;}



}


