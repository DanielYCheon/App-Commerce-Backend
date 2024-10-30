package com.demo.shoppingproject.Repository;

import com.demo.shoppingproject.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Find by username
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

