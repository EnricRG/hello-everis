package com.everis.hello.shoplist.app.domain;

import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

/**
 * @author EnricRG
 */
@Slf4j
public class ShopListService implements CreateShopListUsecase {

    private final ShopListRepository repo;

    @Autowired
    public ShopListService(ShopListRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public ShopList createShopList(String owner, String listName) throws ShopListAlreadyExistsException, MaxShopListsPerUserException {
        log.trace("Creating ShopList '{}' for user '{}'...", listName, owner);
        if (this.repo.existsList(owner, listName)) {
            log.error("User '{}' already has a list with name '{}'.", owner, listName);
            throw new ShopListAlreadyExistsException(owner, listName);
        } else if (this.userListLimitReached(owner)) {
            log.error("User '{}' has reached the limit of shop lists per user!", owner);
            throw new MaxShopListsPerUserException(owner);
        }

        ShopList s = repo.create(newShopList(owner, listName));
        log.info("ShopList '{}' successfully created for user '{}'.", listName, owner);
        log.debug("ShopList created: {}", s);
        return s;
    }

    private ShopList newShopList(String owner, String listName) {
        return new ShopList(owner, listName);
    }

    private boolean userListLimitReached(String owner) {
        return this.repo.userListQuantity(owner) >= CreateShopListUsecase.MAX_LISTS_PER_USER;
    }
}
