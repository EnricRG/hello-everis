package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.mapper;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

        if (domain.getNumberOfItems() > ShopList.MAX_ITEMS_PER_LIST) {
            log.warn("Persistence provided more elements than the max allowed for the application for list '{}' " +
                "owned by user '{}'!", domain.getName(), domain.getOwner());
        }

        return domain;
    }

    /** Updates the given {@link ShopListEntity} instance to match the provided {@link ShopList} domain object. */
    public ShopListEntity toJpaModel(@NotNull ShopList shopList, @NotNull ShopListEntity dbModel) {
        dbModel.setOwner(shopList.getOwner());
        dbModel.setName(shopList.getName());

        Set<ShopListItem> dbItems = dbModel.getItems() == null ? new HashSet<>() : dbModel.getItems();
        for (Long productId : shopList.getItems()) {
            boolean added = dbItems.add(new ShopListItem(dbModel, productId));
            if (!added) log.debug("Product {} was already present in db for list '{}' for user '{}'.",
                productId, shopList.getName(), shopList.getOwner());
        }
        dbModel.setItems(dbItems);

        return dbModel;
    }
}
