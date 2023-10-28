package com.everis.hello.shoplist.app.domain.model;

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
public class DetailedShopList {
    private final String owner;
    private final String name;
    private final List<ProductDetail> products;

    public DetailedShopList(String owner, String name, List<ProductDetail> products) {
        this.owner = owner;
        this.name = name;
        this.products = List.copyOf(products);
    }
}
