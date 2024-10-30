package com.demo.shoppingproject.Service;

import com.demo.shoppingproject.Model.AddCart;
import com.demo.shoppingproject.Model.CartItem;
import com.demo.shoppingproject.Model.Image;
import com.demo.shoppingproject.Model.User;
import com.demo.shoppingproject.Repository.AddCartRepository;
import com.demo.shoppingproject.Repository.CartItemRepository;
import com.demo.shoppingproject.Repository.ImageRepository;
import com.demo.shoppingproject.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
/* @Transactional : This annotation is used to perform operations in a single transaction,
if any one of the operation fails, the whole transaction fails
*/


public class AddCartService
{
    private final AddCartRepository addCartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ImageRepository imageRepository;

    /* --- Initialize the repositories ---*/
    public AddCartService(AddCartRepository addCartRepository, UserRepository userRepository, CartItemRepository cartItemRepository, ImageRepository imageRepository) {
        this.addCartRepository = addCartRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.imageRepository = imageRepository;
    }
    public void addToCart(String username, String productName, int  quantity)
    {
        //Get the user's cart
        //AddCart addCart = user.getAddCart();
        System.out.println("ProductName: " + productName);
        User user = userRepository.findByUsername(username).orElseThrow();
        Image image = imageRepository.findByProductName(productName).orElseThrow();

        //If the user has no cart, create a new cart
        AddCart addCart = user.getAddCart();
        if (addCart == null) {
            addCart = new AddCart();
            addCart.setUser(user);
            addCart = addCartRepository.save(addCart);
            user.setAddCart(addCart);
            userRepository.save(user);
        }

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }


        // Fetch image by productName
        Optional<Image> imageOptional = imageRepository.findByProductName(productName);
        if (imageOptional.isEmpty()) {
            throw new EntityNotFoundException("Image not found with productName: " + productName);
        }



        CartItem cartItem = new CartItem();
        cartItem.setAddCart(addCart);
        cartItem.setImage(image);
        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);


        //Save the cart item
        //cartItemRepository.save(cartItem);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Log the saved cart item
        System.out.println("Saved CartItem id: " + savedCartItem.getId() + ", AddCart id: " + savedCartItem.getAddCart().getId() + ", Quantity: " + savedCartItem.getQuantity());



    }
    public int getCartItemCount(User user) {
        if (user.getAddCart() == null) {
            //If the user has no cart, return 0; (for displaying the cart item count)
            System.out.println("User" + user.getUsername() + "has no cart");
            return 0;
        } else {
            //Return the number of items in the user's cart
            return user.getAddCart().getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        }
    }


}



