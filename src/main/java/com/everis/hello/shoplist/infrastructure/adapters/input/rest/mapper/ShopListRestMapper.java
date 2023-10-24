package com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import org.springframework.stereotype.Component;

/**
 * Mapping class for {@link ShopList}-related rest input ports.
 *
 * @author EnricRG
 */
@Component
public class ShopListRestMapper {
    public ShopListSimpleView toSimpleView(ShopList shopList) {
        return new ShopListSimpleView(shopList.getName());
    }
}
