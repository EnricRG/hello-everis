package com.everis.hello.shoplist.app.domain.service;

import com.everis.hello.shoplist.app.domain.model.DetailedShopList;
import com.everis.hello.shoplist.app.domain.model.ProductDetail;
import com.everis.hello.shoplist.app.domain.model.ShopList;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;
import com.everis.hello.shoplist.app.ports.input.ShopListDetailsUsecase;
import com.everis.hello.shoplist.app.ports.output.ProductDetailProvider;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author EnricRG
 */
@Service
@Validated
@Slf4j
public class ShopListDetailsService implements ShopListDetailsUsecase {

    private final ShopListRepository repo;
    private final ProductDetailProvider productDetailProvider;

    @Autowired
    public ShopListDetailsService(ShopListRepository repo, ProductDetailProvider productDetailProvider) {
        this.repo = repo;
        this.productDetailProvider = productDetailProvider;
    }

    @Override
    public DetailedShopList getDetails(String owner, String listName) throws ShopListNotFoundException {
        log.trace("Retrieving details from list '{}' owned by user '{}'...", listName, owner);

        ShopList shopList = this.repo.getShopList(owner, listName);
        log.debug("ShopList found: {}", shopList);

        List<ProductDetail> productDetails = this.productDetailProvider.getDetails(shopList.getItems());
        if (productDetails.size() < shopList.getItems().size()) {
            log.info("Shop list '{}' owned by user '{}' contains products that are not present in the product service. " +
                "Those products will be removed.", listName, owner);
            this.removeUnknownProducts(shopList, productDetails);
        }

        DetailedShopList details = this.createDetailedShopList(shopList, productDetails);
        log.info("Details successfully fetched for list '{}' owned by user '{}'.", listName, owner);
        log.debug("Detailed Shop List: {}", details);

        return details;
    }

    private void removeUnknownProducts(ShopList shopList, List<ProductDetail> productDetails) {
        String owner = shopList.getOwner();
        String listName = shopList.getName();
        log.debug("Removing products on list '{}' owned by user '{}' that are not present in the product service...",
            listName, owner);

        Set<Long> itemsToRemove = new HashSet<>(shopList.getItems());
        Set<Long> detailItems = productDetails.stream()
            .map(ProductDetail::getId)
            .collect(Collectors.toSet());

        itemsToRemove.removeAll(detailItems);

        for (Long productId : itemsToRemove) {
            shopList.removeProduct(productId);
            log.debug("Product '{}' removed from list '{}' owned by user '{}'.", productId, listName, owner);
        }
        log.info("{} products have been removed from list '{}' owned by user '{}'.", itemsToRemove.size(), listName, owner);

        shopList = this.repo.save(shopList);
        log.debug("Shop List after unknown product removal: {}", shopList);
    }

    private DetailedShopList createDetailedShopList(ShopList shopList, List<ProductDetail> productDetails) {
        return new DetailedShopList(shopList.getOwner(), shopList.getName(), productDetails);
    }
}
