package com.demo.shoppingproject.Controller;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddToCartRequest {
    private String username;
    private String productName;
    private int quantity;
}
