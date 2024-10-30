package com.demo.shoppingproject.Controller;

import com.demo.shoppingproject.Model.Image;
import com.demo.shoppingproject.Repository.ImageRepository;
import com.demo.shoppingproject.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @PostMapping("/imageupload")
    //below code is for uploading image named image
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                         @RequestParam("productName") String productName,
                                         @RequestParam("productDescription") String productDescription,
                                         @RequestParam("productPrice") String productPrice,
                                         @RequestParam("gender") String gender) throws IOException {

        String uploadImage = imageService.uploadImage(file, productName, productDescription, productPrice, gender);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }
//below code is for downloading specific image
    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> downloadImage(@PathVariable String imageName) {
        byte[] imageData = imageService.downloadImage(imageName);
        //-------------------------------
        Optional<Image> dbImage = imageRepository.findByName(imageName);
        if(dbImage.isPresent()){
            String contentType = dbImage.get().getType();
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(contentType))
                    .contentLength(imageData.length)
                    .body(imageData);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }

    }
    //below code is for downloading specific image detail information,
    // product name, product description, product price
    @GetMapping("/image/detail/{imageName}")
    public ResponseEntity<?> downloadImageDetails(@PathVariable String imageName) {
        Optional<Image> dbImage = imageRepository.findByName(imageName);
        if(dbImage.isPresent()){
            Map<String, String> imageDetails = new HashMap<>();
            imageDetails.put("productName", dbImage.get().getProductName());
            imageDetails.put("productDescription", dbImage.get().getProductDescription());
            imageDetails.put("productPrice", dbImage.get().getProductPrice());
            imageDetails.put("gender", dbImage.get().getGender());
            return ResponseEntity.status(HttpStatus.OK).body(imageDetails);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
    }


    //below code is for getting information by Gender
    @GetMapping("/get/products/{gender}")
            public ResponseEntity<?> getImagesByGender(@PathVariable String gender){
        List<Image> images = imageRepository.findByGender(gender);
        List<Map<String, Object>> imageDetailsList = new ArrayList<>();
        for (Image image : images) {
            Map<String, Object> imageDetails = new HashMap<>();
            imageDetails.put("name", image.getName());
            imageDetails.put("productName", image.getProductName());
            imageDetails.put("productDescription", image.getProductDescription());
            imageDetails.put("productPrice", image.getProductPrice());
            imageDetails.put("gender", image.getGender());
            imageDetails.put("imageData", imageService.downloadImage(image.getName()));
            imageDetailsList.add(imageDetails);
    }
        return ResponseEntity.status(HttpStatus.OK).body(imageDetailsList);
    }

}
