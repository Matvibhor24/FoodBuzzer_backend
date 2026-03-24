package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.order.CartItemDTO;
import com.example.food_buzzer_backend.dto.order.OrderRequest;
import com.example.food_buzzer_backend.dto.order.OrderResponse;
import com.example.food_buzzer_backend.model.Cart;
import com.example.food_buzzer_backend.model.Customer;
import com.example.food_buzzer_backend.model.Order;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.repository.CartRepository;
import com.example.food_buzzer_backend.repository.CustomerRepository;
import com.example.food_buzzer_backend.repository.OrderRepository;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.ProductRecipeRepository;
import com.example.food_buzzer_backend.repository.RecipeItemRepository;
import com.example.food_buzzer_backend.repository.InventoryMaterialRepository;
import com.example.food_buzzer_backend.model.ProductRecipe;
import com.example.food_buzzer_backend.model.RecipeItem;
import com.example.food_buzzer_backend.model.InventoryMaterial;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.example.food_buzzer_backend.repository.UserRepository;
import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.exception.ResourceNotFoundException;
import com.example.food_buzzer_backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRecipeRepository productRecipeRepository;

    @Autowired
    private RecipeItemRepository recipeItemRepository;

    @Autowired
    private InventoryMaterialRepository inventoryMaterialRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        if (user.getRestaurant() == null) {
            throw new IllegalArgumentException("User does not belong to any restaurant");
        }
        Long restaurantId = user.getRestaurant().getId();

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Find or create customer based on phone and restaurant
        Customer customer = customerRepository.findByPhoneAndRestaurantId(request.getCustomerPhone(), restaurant.getId())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setRestaurant(restaurant);
                    newCustomer.setPhone(request.getCustomerPhone());
                    newCustomer.setName(request.getCustomerName());
                    newCustomer.setEmailId(request.getCustomerEmail());
                    newCustomer.setLoyaltyPoints(0);
                    return customerRepository.save(newCustomer);
                });

        // Update name/email if necessary, but skipping for brevity
        if (request.getCustomerName() != null && !request.getCustomerName().isEmpty() && !request.getCustomerName().equals(customer.getName())) {
             customer.setName(request.getCustomerName());
        }

        double cartTotal = 0.0;
        if (request.getCartItems() != null) {
            for (CartItemDTO item : request.getCartItems()) {
                cartTotal += (item.getUnitPrice() * item.getQuantity());
                // ensure front-end total price matches or just use backend calculation
                item.setTotalPrice(item.getUnitPrice() * item.getQuantity());
            }
        }

        double discount = 0.0;
        
        // Loyalty logic: if cartTotal > 500, apply points as discount
        if (cartTotal > 500 && customer.getLoyaltyPoints() > 0) {
            discount = Math.min((double) customer.getLoyaltyPoints(), cartTotal);
            // Deduct points
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() - (int) discount);
        }

        // Earn fixed 5 points per order
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + 5);
        customerRepository.save(customer);

        double grandTotal = cartTotal - discount;
        if (grandTotal < 0) grandTotal = 0.0;

        // Save Cart
        Cart cart = new Cart();
        cart.setRestaurant(restaurant);
        cart.setCustomer(customer);
        try {
            cart.setCartItems(objectMapper.writeValueAsString(request.getCartItems()));
        } catch (Exception e) {
            throw new RuntimeException("Error mapping cart items to JSON", e);
        }
        cart = cartRepository.save(cart);

        // Save Order
        Order order = new Order();
        order.setCart(cart);
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setCartTotal(cartTotal);
        order.setDiscount(discount);
        order.setGrandTotal(grandTotal);
        order.setStatus(AppConstants.ORDER_STATUS_PENDING); // Initial status
        order.setTableId(request.getTableId());
        order.setRating(request.getRating());
        order = orderRepository.save(order);

        return new OrderResponse(order);
    }

    public List<OrderResponse> getOrdersByRestaurant(Long userId, List<String> statuses) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        if (user.getRestaurant() == null) {
            throw new IllegalArgumentException("User does not belong to any restaurant");
        }
        Long restaurantId = user.getRestaurant().getId();

        List<Order> orders;
        if (statuses != null && !statuses.isEmpty()) {
            List<String> upperStatuses = statuses.stream().map(String::toUpperCase).collect(Collectors.toList());
            orders = orderRepository.findByRestaurantIdAndStatusInOrderByCreatedAtDesc(restaurantId, upperStatuses);
        } else {
            orders = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        }
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        if (user.getRestaurant() == null) {
            throw new IllegalArgumentException("User does not belong to any restaurant");
        }
        Long restaurantId = user.getRestaurant().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
                
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("You do not have permission to access an order from outside your restaurant.");
        }
        
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long userId, Long orderId, String newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }
        if (user.getRestaurant() == null) {
            throw new IllegalArgumentException("User does not belong to any restaurant");
        }
        Long restaurantId = user.getRestaurant().getId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
                
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new IllegalArgumentException("You do not have permission to update an order from outside your restaurant.");
        }
        
        // Inventory Deduction Logic
        if (AppConstants.ORDER_STATUS_PENDING.equalsIgnoreCase(order.getStatus()) && AppConstants.ORDER_STATUS_ACCEPTED.equalsIgnoreCase(newStatus)) {
            boolean deductionSuccess = applyInventoryDeductions(order);
            if (!deductionSuccess) {
                order.setStatus(AppConstants.ORDER_STATUS_FAILED);
                order = orderRepository.save(order);
                return new OrderResponse(order);
            }
        }
        
        order.setStatus(newStatus.toUpperCase());
        order = orderRepository.save(order);
        return new OrderResponse(order);
    }
    
    private boolean applyInventoryDeductions(Order order) {
        try {
            List<CartItemDTO> cartItems = objectMapper.readValue(order.getCart().getCartItems(), new TypeReference<List<CartItemDTO>>() {});
            Map<Long, Double> materialNeeds = new HashMap<>();

            for (CartItemDTO item : cartItems) {
                if (item.getProductId() == null) continue; // Safety check

                List<ProductRecipe> productRecipes = productRecipeRepository.findByProductId(item.getProductId());
                for (ProductRecipe pr : productRecipes) {
                    List<RecipeItem> recipeItems = recipeItemRepository.findByRecipeId(pr.getRecipe().getId());
                    for (RecipeItem ri : recipeItems) {
                        Long materialId = ri.getRawMaterial().getId();
                        // Total requested quantity * ProductRecipe quantity * RecipeItem quantity
                        double requiredQty = item.getQuantity() * pr.getQuantity() * ri.getQuantity();
                        materialNeeds.put(materialId, materialNeeds.getOrDefault(materialId, 0.0) + requiredQty);
                    }
                }
            }

            // System.out.println("Material Needs for Order " + order.getId() + ": " + materialNeeds);

            if (materialNeeds.isEmpty()) {
                System.out.println("No materials needed for order " + order.getId() + ", cart parsing might have yielded no recipes.");
                return true; // nothing to deduct
            }

            // Verify stock
            List<InventoryMaterial> materialsToUpdate = new ArrayList<>();
            for (Map.Entry<Long, Double> entry : materialNeeds.entrySet()) {
                InventoryMaterial material = inventoryMaterialRepository.findById(entry.getKey()).orElse(null);
                if (material == null) {
                    System.out.println("Material ID " + entry.getKey() + " not found!");
                    return false; // Fail fast if material no longer exists
                }
                
                if (material.getCurrentStock() < entry.getValue()) {
                    System.out.println("Not enough stock for Material ID " + entry.getKey() + ". Needed: " + entry.getValue() + ", Current: " + material.getCurrentStock());
                    return false; // Not enough stock
                }
                
                material.setCurrentStock(material.getCurrentStock() - entry.getValue());
                materialsToUpdate.add(material);
            }

            // Save deducted materials
            inventoryMaterialRepository.saveAll(materialsToUpdate);
            System.out.println("Successfully deducted inventory for order " + order.getId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
