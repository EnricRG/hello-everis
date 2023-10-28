package com.everis.hello.shoplist.app.domain;

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
}
