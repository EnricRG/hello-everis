package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.mapper;

import com.everis.hello.shoplist.app.domain.model.ShopList;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<ShopList> toDomain(@NotNull List<ShopListEntity> dbModels) {
        List<ShopList> lists = new ArrayList<>();
        for (ShopListEntity dbModel : dbModels) {
            lists.add(this.toDomain(dbModel));
        }
        return lists;
    }

    /** Updates the given {@link ShopListEntity} instance to match the provided {@link ShopList} domain object. */
    public ShopListEntity toJpaModel(@NotNull ShopList shopList, @NotNull ShopListEntity dbModel) {
        dbModel.setOwner(shopList.getOwner());
        dbModel.setName(shopList.getName());

        this.synchronizeItems(shopList, dbModel);

        return dbModel;
    }

    // This method takes advantage of the fact that both ShopListEntity and ShopListItem entities have redefined their
    // equals() and hashCode() implementations to match business key uniqueness. This allows for an elegant implementation
    // of this Set operations.
    private void synchronizeItems(ShopList shopList, ShopListEntity dbModel) {
        //This set contains all domain products mapped to its JPA object
        Set<ShopListItem> mappedJpaItems = new HashSet<>();
        for (Long productId : shopList.getItems()) {
            mappedJpaItems.add(new ShopListItem(dbModel, productId));
        }

        Set<ShopListItem> dbItems = dbModel.getItems() == null ? new HashSet<>() : dbModel.getItems();
        dbItems.retainAll(mappedJpaItems); // Drop those items that are not present in the domain object.

        dbItems.addAll(mappedJpaItems); // Add the remaining elements that were not present in the db.

        dbModel.setItems(dbItems);
    }

    public String stringifyFull(@NotNull List<ShopListEntity> dbModels) {
        return dbModels.stream()
            .map(ShopListEntity::toStringFull)
            .collect(Collectors.joining(", ", "List<ShopListEntity>[", "]"));
    }
}
