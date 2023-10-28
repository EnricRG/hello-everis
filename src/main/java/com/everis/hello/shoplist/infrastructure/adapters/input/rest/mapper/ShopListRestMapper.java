package com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapping class for {@link ShopList}-related rest input ports.
 *
 * @author EnricRG
 */
@Component
public class ShopListRestMapper {
    public ShopListSimpleView toSimpleView(@NotNull ShopList shopList) {
        return new ShopListSimpleView(shopList.getName(), shopList.getItems().size());
    }

    public List<ShopListSimpleView> toSimpleView(@NotNull List<ShopList> shopLists) {
        List<ShopListSimpleView> view = new ArrayList<>();
        for (ShopList s : shopLists) {
            view.add(this.toSimpleView(s));
        }
        return view;
    }
}
