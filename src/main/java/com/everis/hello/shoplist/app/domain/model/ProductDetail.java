package com.everis.hello.shoplist.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author EnricRG
 */
@AllArgsConstructor
@Data
public class ProductDetail {
    private final Long id;
    private final String name;
    private final String size;
    private final String price;
    private final String color;
}
