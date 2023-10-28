package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.domain.model.DetailedShopList;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;

/**
 * @author EnricRG
 */
public interface ShopListDetailsUsecase {

    DetailedShopList getDetails(String owner, String listName) throws ShopListNotFoundException;
}
