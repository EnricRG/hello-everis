package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.mapper;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EnricRG
 */
@Component
@Slf4j
public class ShopListJpaMapper {
    public ShopList toDomain(ShopListEntity dbModel) {
        ShopList domain = new ShopList(dbModel.getName(), dbModel.getOwner());

        for (ShopListItem dbItem : dbModel.getItems()) {
            domain.addProductNoConstraint(dbItem.getProductId());
        }

        //TODO move to upper layer
        if (domain.getNumberOfItems() > ShopList.MAX_ITEMS_PER_LIST) {
            log.warn("Persistence provided more elements than the max allowed for the application for list '{}' " +
                "owned by user '{}'!", domain.getName(), domain.getOwner());
        }

        return domain;
    }

    public ShopListEntity toJpa(ShopList shopList) {
        ShopListEntity dbModel = new ShopListEntity();

        dbModel.setOwner(shopList.getOwner());
        dbModel.setName(shopList.getName());

        List<ShopListItem> dbItems = new ArrayList<>();
        for (Long productId : shopList.getItems()) {
            dbItems.add(new ShopListItem(dbModel, productId));
        }
        dbModel.setItems(dbItems);

        return dbModel;
    }
}
