package com.everis.hello.shoplist.infrastructure.adapters.input.rest.model;

import com.everis.hello.shoplist.app.domain.model.ShopList;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * View model of a {@link ShopList}.
 * Bear in mind that modifying object fields imply modifying serialized representation.
 * @author EnricRG
 */
@Data
@AllArgsConstructor
public class ShopListSimpleView {
    private String name;
    private Integer productNumber;
}
