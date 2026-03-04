package com.example.food_buzzer_backend.dto.request;
public class RegisterOwnerRequest {

    private String fullName;
    private String email;
    private String password;
    private String phone;

    private String restaurantName;
    private String slug;
    private String address;
    private String city;
    private String zipcode;
    private String restaurantPhone;

    public String getFullName(){ return fullName; }

    public void setFullName(String fullName){ this.fullName = fullName; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public String getPassword(){ return password; }

    public void setPassword(String password){ this.password = password; }

    public String getPhone(){ return phone; }

    public void setPhone(String phone){ this.phone = phone; }

    public String getRestaurantName(){ return restaurantName; }

    public void setRestaurantName(String restaurantName){ this.restaurantName = restaurantName; }

    public String getSlug(){ return slug; }

    public void setSlug(String slug){ this.slug = slug; }

    public String getAddress(){ return address; }

    public void setAddress(String address){ this.address = address; }

    public String getCity(){ return city; }

    public void setCity(String city){ this.city = city; }

    public String getZipcode(){ return zipcode; }

    public void setZipcode(String zipcode){ this.zipcode = zipcode; }

    public String getRestaurantPhone(){ return restaurantPhone; }

    public void setRestaurantPhone(String restaurantPhone){ this.restaurantPhone = restaurantPhone; }
}