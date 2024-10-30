package com.demo.shoppingproject.Controller;
import com.demo.shoppingproject.Model.AddCart;
import com.demo.shoppingproject.Model.CartItem;
import com.demo.shoppingproject.Model.Image;
import com.demo.shoppingproject.Model.User;
import com.demo.shoppingproject.Repository.CartItemRepository;
import com.demo.shoppingproject.Repository.ImageRepository;
import com.demo.shoppingproject.Repository.UserRepository;
import com.demo.shoppingproject.Service.AddCartService;
import com.demo.shoppingproject.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AddCartController {
    private final AddCartService addCartService;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CartItemRepository cartItemRepository;
    private final ImageService imageService;

    //Initialize the repositories
    public AddCartController(AddCartService addCartService, UserRepository userRepository, ImageRepository imageRepository, CartItemRepository cartItemRepository, ImageService imageService) {
        this.addCartService = addCartService;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.cartItemRepository = cartItemRepository;
        this.imageService = imageService;

    }

    //Add an item to the cart
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try {
            // Extract the username, product name, and quantity from the request
            String username = request.getUsername();
            String productName = request.getProductName();
            int quantity = request.getQuantity();

            // Call the service method to add the item to the cart
            addCartService.addToCart(username, productName, quantity);

            // If everything goes well, return a success response
            return ResponseEntity.ok("Item added to cart successfully");
        } catch (Exception e) {
            // If anything goes wrong, return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item to cart");
        }
    }



    //Get the number of items in the cart
    //@RequestBody using with POST, PUT, and DELETE with POST method
    //@RequestParam using with GET method
    @PostMapping("/count")
    public ResponseEntity<?> getCartItemCount(@RequestBody AddToCartRequest request) {
        String username = request.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        AddCart addCart = user.getAddCart();


        //Check if the user exists
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        //Get the number of items in the cart


        int count = addCartService.getCartItemCount(user);
        List<CartItem> cartItems = addCart.getCartItems();
        List<Map<String, Object>> cartItemsDetails = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Image image = cartItem.getImage();
            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("productName", image.getProductName());
           itemDetails.put("price", image.getProductPrice());
            itemDetails.put("description", image.getProductDescription());
            itemDetails.put("quantity", cartItem.getQuantity());
            itemDetails.put("imageData", imageService.downloadImage(image.getName()));

            cartItemsDetails.add(itemDetails);
        }

        return ResponseEntity.ok(cartItemsDetails);
    }

}
