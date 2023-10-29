package com.everis.hello.shoplist.infrastructure.adapters.output.product;

import lombok.*;

/**
 * @author EnricRG
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductView {
    private Long id;
    private String name;
    private String size;
    private String price;
    private String color;
}
