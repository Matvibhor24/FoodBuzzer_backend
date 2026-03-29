package com.example.food_buzzer_backend.dto.menu;

import java.util.List;

public class PublicMenuResponseDTO {

    private RestaurantInfoDTO restaurant;
    private List<PublicProductResponseDTO> products;

    // Getters and Setters
    public RestaurantInfoDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantInfoDTO restaurant) {
        this.restaurant = restaurant;
    }

    public List<PublicProductResponseDTO> getProducts() {
        return products;
    }

    public void setProducts(List<PublicProductResponseDTO> products) {
        this.products = products;
    }

    // Nested DTO for restaurant basic info
    public static class RestaurantInfoDTO {
        private Long id;
        private String name;
        private String slug;
        private String address;
        private String city;
        private String phone;
        private String email;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
