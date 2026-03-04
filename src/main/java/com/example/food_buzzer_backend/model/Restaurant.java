package com.example.food_buzzer_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String slug;

    private String address;

    private String city;

    private String zipcode;

    private String phone;

    private String approvalStatus = "PENDING";

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

    public Restaurant(){}

    public Long getId(){ return id; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getSlug(){ return slug; }

    public void setSlug(String slug){ this.slug = slug; }

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

    public String getCity(){ return city; }

    public void setCity(String city){ this.city = city; }

    public String getZipcode(){ return zipcode; }

    public void setZipcode(String zipcode){ this.zipcode = zipcode; }

    public String getPhone(){ return phone; }

    public void setPhone(String phone){ this.phone = phone; }

    public User getOwner(){ return owner; }

    public void setOwner(User owner){ this.owner = owner; }
}