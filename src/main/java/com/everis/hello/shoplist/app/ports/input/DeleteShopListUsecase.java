package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;

/**
 * @author EnricRG
 */
public interface DeleteShopListUsecase {

    /**
     * Deletes the list owned by user if it exists. Throws a {@link ShopListNotFoundException} if the list does not
     * exist.
     * <p>
     * If this method ends successfully it can be assumed that the list is no longer present in the repository.
     *
     * @param owner Owner of the list.
     * @param listName List name. Must exist a list with this name for the given user.
     *
     * @throws ShopListNotFoundException If the list does not exist.
     */
    void deleteList(String owner, String listName) throws ShopListNotFoundException;
}
