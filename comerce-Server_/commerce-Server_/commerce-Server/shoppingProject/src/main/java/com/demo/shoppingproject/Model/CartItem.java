package com.demo.shoppingproject.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // id is Primary Key for CartItem

    @ManyToOne
    @JoinColumn(name = "addCart_id", referencedColumnName = "id")
    private AddCart addCart;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    private int quantity;
}
