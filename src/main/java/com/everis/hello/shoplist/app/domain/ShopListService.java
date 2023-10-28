package com.everis.hello.shoplist.app.domain;

import com.everis.hello.shoplist.app.exception.*;
import com.everis.hello.shoplist.app.ports.input.AddProductUsecase;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.app.ports.input.DeleteShopListUsecase;
import com.everis.hello.shoplist.app.ports.input.RemoveProductUsecase;
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
public class ShopListService implements CreateShopListUsecase, AddProductUsecase, DeleteShopListUsecase, RemoveProductUsecase {

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

        ShopList s = this.repo.save(newShopList(listName, owner, products));
        log.info("ShopList '{}' successfully created for user '{}'.", listName, owner);
        log.debug("ShopList created: {}", s);
        return s;
    }

    @Override
    @Transactional
    public boolean addProduct(String owner, String listName, Long productId) throws ShopListNotFoundException, ShopListFullException {
        log.trace("Adding product '{}' to list '{}' owned by user '{}'...", productId, listName, owner);

        ShopList shopList = this.repo.getShopList(owner, listName);
        log.debug("ShopList found: {}", shopList);

        boolean productAdded = shopList.addProduct(productId);
        shopList = this.repo.save(shopList);
        log.debug("Updated shop list: {}", shopList);

        if (productAdded) log.info("Product '{}' added to list '{}' owned by user '{}'", productId, listName, owner);
        else log.info("Product '{}' was already in list '{}' owned by user '{}'", productId, listName, owner);

        return productAdded;
    }

    @Override
    public void deleteList(String owner, String listName) throws ShopListNotFoundException {
        log.trace("Starting deletion of list '{}' owned by user '{}'...", listName, owner);

        boolean listExists = this.repo.listExists(owner, listName);
        if (!listExists) {
            log.error("Cannot delete list '{}' owned by user '{}' because it does not exist.", listName, owner);
            throw new ShopListNotFoundException(owner, listName);
        }
        log.debug("ShopList '{}' owned by user '{}' exists.", listName, owner);

        this.repo.deleteList(owner, listName);
        log.info("ShopList '{}' owned by user '{}' has been deleted.", listName, owner);
    }

    @Override
    public int removeProduct(String owner, String listName, Long productId) throws ShopListNotFoundException {
        log.trace("Adding product '{}' to list '{}' owned by user '{}'...", productId, listName, owner);

        ShopList shopList = this.repo.getShopList(owner, listName);
        log.debug("ShopList found: {}", shopList);

        int listSize = shopList.removeProduct(productId);
        if (shopList.needsDeletion()) {
            this.repo.deleteList(shopList.getOwner(), shopList.getName());
            log.info("ShopList '{}' owned by user '{}' has been deleted as a result of becoming empty.", listName, owner);
        } else {
            this.repo.save(shopList);
        }

        log.debug("ShopList after product removal: {}", shopList);
        return listSize;
    }

    private void validateForCreation(String owner, String listName, List<Long> products)
        throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException,
        ShopListEmptyException
    {
        if (this.repo.listExists(owner, listName)) {
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
            throw new CannotCreateShopListException(owner, "max product number exceeded on creation.");
        }
    }

    private ShopList newShopList(String listName, String owner, List<Long> products) {
        return new ShopList(listName, owner, products);
    }

    private boolean userListLimitReached(String owner) {
        return this.repo.userListQuantity(owner) >= CreateShopListUsecase.MAX_LISTS_PER_USER;
    }
}
