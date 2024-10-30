package com.demo.shoppingproject.Repository;

import com.demo.shoppingproject.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image>findByName(String name);

    Optional<Image>findByProductName(String productName);
    List<Image> findByGender(String gender);

}
