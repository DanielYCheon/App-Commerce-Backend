package com.demo.shoppingproject.Repository;

import com.demo.shoppingproject.Model.AddCart;

import com.demo.shoppingproject.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AddCartRepository extends JpaRepository<AddCart, Long> {
   // Optional<AddCart>findByUser_Id( Long Id);

    @Query("SELECT a FROM AddCart a JOIN FETCH a.cartItems WHERE a.user.id = :userId")
    Optional<AddCart> findByUser_IdWithCartItems(Long userId);
}
