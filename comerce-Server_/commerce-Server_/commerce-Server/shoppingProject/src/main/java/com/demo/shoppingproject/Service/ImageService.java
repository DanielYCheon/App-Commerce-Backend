package com.demo.shoppingproject.Service;

import com.demo.shoppingproject.Config.ImageUtils;
import com.demo.shoppingproject.Model.Image;
import com.demo.shoppingproject.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    public String uploadImage(MultipartFile imageFile, String productName, String productDescription, String productPrice, String gender)
        throws IOException{
        Image image = imageRepository.save(Image.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                        .productName(productName)
                        .productDescription(productDescription)
                        .productPrice(productPrice)
                        .gender(gender)
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .build());
        return "Image uploaded successfully : " + imageFile.getOriginalFilename();
    }
    public byte[] downloadImage(String imageName) {
        Optional<Image> dbImage = imageRepository.findByName(imageName);
        return dbImage.map(image->{
            try{
                return ImageUtils.decompressImage(image.getImageData());

            }catch (DataFormatException | IOException exception){
                throw new RuntimeException("Error decompressing image");


            }
        }).orElse(null);
    }

}
