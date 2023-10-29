package com.everis.hello.shoplist.infrastructure.adapters.input.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author EnricRG
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class DetailedShopListView {
    private final String owner;
    private final String name;
    private final List<ProductDetailView> products;
}
