package com.everis.hello.shoplist.app.domain;

import com.everis.hello.shoplist.app.exception.*;
import com.everis.hello.shoplist.app.ports.input.AddProductUsecase;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author EnricRG
 */
@Slf4j
@Validated
public class ShopListService implements CreateShopListUsecase, AddProductUsecase {

    private final ShopListRepository repo;

    @Autowired
    public ShopListService(ShopListRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public ShopList createShopList(String owner, String listName, List<Long> products)
        throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException, ShopListEmptyException
    {
        log.trace("Creating ShopList '{}' for user '{}'...", listName, owner);

        this.validateForCreation(owner,listName,products);

        ShopList s = repo.create(newShopList(listName, owner, products));
        log.info("ShopList '{}' successfully created for user '{}'.", listName, owner);
        log.debug("ShopList created: {}", s);
        return s;
    }

    private void validateForCreation(String owner, String listName, List<Long> products)
        throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException,
        ShopListEmptyException
    {
        if (this.repo.existsList(owner, listName)) {
            log.error("User '{}' already has a list with name '{}'.", owner, listName);
            throw new ShopListAlreadyExistsException(owner, listName);
        } else if (this.userListLimitReached(owner)) {
            log.error("User '{}' has reached the limit of shop lists per user!", owner);
            throw new MaxShopListsPerUserException(owner);
        } else if (products.isEmpty()) {
            log.error("Cannot create shop list '{}' for user '{}' with no products", listName, owner);
            throw new ShopListEmptyException(owner);
        } else if (products.size() > ShopList.MAX_ITEMS_PER_LIST) {
            log.error("Cannot create a shop list for user '{}' with {} products because it exceeds the limit of " +
                "{} products per list", owner, products.size(), ShopList.MAX_ITEMS_PER_LIST);
            throw new CannotCreateShopListException(owner, "max product number exceeded.");
        }
    }

    private ShopList newShopList(String listName, String owner, List<Long> products) {
        return new ShopList(listName, owner, products);
    }

    private boolean userListLimitReached(String owner) {
        return this.repo.userListQuantity(owner) >= CreateShopListUsecase.MAX_LISTS_PER_USER;
    }

    @Override
    @Transactional
    public boolean addProduct(String owner, String listName, Long productId) throws ShopListNotFoundException, ShopListFullException {
        log.trace("Adding product '{}' to list '{}' owned by user '{}'...", productId, listName, owner);

        ShopList shopList = this.repo.getShopList(owner, listName);
        log.debug("ShopList found: {}", shopList);

        boolean productAdded = shopList.addProduct(productId);
        this.repo.save(shopList);

        if (productAdded) log.info("Product '{}' added to list '{}' owned by user '{}'", productId, listName, owner);
        else log.info("Product '{}' was already in list '{}' owned by user '{}'", productId, listName, owner);

        return productAdded;
    }
}
