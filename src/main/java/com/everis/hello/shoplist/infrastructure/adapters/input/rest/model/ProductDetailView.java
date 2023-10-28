package com.everis.hello.shoplist.infrastructure.adapters.input.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author EnricRG
 */
@AllArgsConstructor
@Data
public class ProductDetailView {
    private final Long id;
    private final String name;
    private final String size;
    private final String price;
    private final String color;
}
