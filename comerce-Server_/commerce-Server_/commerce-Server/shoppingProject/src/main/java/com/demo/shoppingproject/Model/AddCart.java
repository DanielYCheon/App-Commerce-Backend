package com.demo.shoppingproject.Model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Table(name = "AddCart")
@Getter
@Setter
public class AddCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //Owner is AddCart
    @OneToMany(mappedBy = "addCart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
}
