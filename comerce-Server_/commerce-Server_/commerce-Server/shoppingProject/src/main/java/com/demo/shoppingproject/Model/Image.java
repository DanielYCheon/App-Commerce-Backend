package com.demo.shoppingproject.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private  String type;
    @Column(name = "productName")
    private String productName;
    @Column(name = "productDescription", length = 1000) //String length is 1000
    private String productDescription;
    @Column(name = "productPrice")
    private String productPrice;
    @Column(name = "gender")
    private  String gender;

    @Lob
    @Column(length = 20971520) //length = 20971520 -> 20MB
    // JPA provides @Column(length = n); n(size) to specify the length of the column in the database.
    private byte[]  imageData;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
