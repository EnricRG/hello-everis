package com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListView;

public class ShopListMapper {
    public ShopListView toView(ShopList shopList) {
        return new ShopListView(shopList.getName());
    }
}
