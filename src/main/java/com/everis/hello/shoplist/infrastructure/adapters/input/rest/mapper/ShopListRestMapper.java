package com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper;

import com.everis.hello.shoplist.app.domain.model.DetailedShopList;
import com.everis.hello.shoplist.app.domain.model.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.DetailedShopListView;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ProductDetailView;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ShopListRestMapper {

    private final ProductRestMapper productMapper;

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

    public DetailedShopListView toDetailedView(@NotNull DetailedShopList shopListDetail) {
        List<ProductDetailView> productViews = this.productMapper.toDetailView(shopListDetail.getProducts());
        return new DetailedShopListView(shopListDetail.getOwner(), shopListDetail.getName(), productViews);
    }
}
