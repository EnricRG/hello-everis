package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.domain.model.ShopList;

import java.util.List;

/**
 * @author EnricRG
 */
public interface UserListsUsecase {

    /**
     * Returns all the lists registered for a given user. If no lists match the search it returns empty list, even if
     * the search failed because this user is not registered.
     *
     * @param owner Owner of the lists.
     *
     * @return A list with the {@link ShopList}s of the user. Never null.
     */
    List<ShopList> getListsForUser(String owner);
}
