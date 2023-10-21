package com.everis.hello.shoplist.infrastructure.adapters.input.rest.model;

import com.everis.hello.shoplist.app.domain.ShopList;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * View model of a {@link ShopList}.
 * Bear in mind that modifying object fields imply modifying serialized representation.
 * @author EnricRG
 */
@AllArgsConstructor
@ToString
public class ShopListSimpleView {
    public String name;
}
