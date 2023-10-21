package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;

public interface CreateShopListUsecase {
    int MAX_LISTS_PER_USER = 5;
    ShopList createShopList(String username, String listName) throws ShopListAlreadyExistsException, MaxShopListsPerUserException;
}
