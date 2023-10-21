package com.everis.hello.shoplist.app.domain;

import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public class ShopListService implements CreateShopListUsecase {

    private static final Logger LOG = LoggerFactory.getLogger(ShopListService.class);

    private final ShopListRepository repo;

    @Autowired
    public ShopListService(ShopListRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public ShopList createShopList(String username, String listName) throws ShopListAlreadyExistsException, MaxShopListsPerUserException {
        LOG.trace("Creating ShopList '{}' for user '{}'...", listName, username);
        if (this.repo.existsList(username, listName)) {
            LOG.error("User '{}' already has a list with name '{}'.", username, listName);
            throw new ShopListAlreadyExistsException(username, listName);
        } else if (this.userListLimitReached(username)) {
            LOG.error("User '{}' has exceeded the limit of shop lists per user!", username);
            throw new MaxShopListsPerUserException(username);
        }

        ShopList s = repo.create(newShopList(username, listName));
        LOG.info("ShopList '{}' successfully created for user '{}'.", listName, username);
        LOG.debug("ShopList created: {}", s);
        return s;
    }

    private ShopList newShopList(String username, String listName) {
        return new ShopList(username, listName);
    }

    private boolean userListLimitReached(String username) {
        return this.repo.userListQuantity(username) >= CreateShopListUsecase.MAX_LISTS_PER_USER;
    }
}
