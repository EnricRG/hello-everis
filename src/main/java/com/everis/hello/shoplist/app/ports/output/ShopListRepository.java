package com.everis.hello.shoplist.app.ports.output;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;

import javax.validation.constraints.NotNull;

/**
 * @author EnricRG
 */
public interface ShopListRepository {
    /**
     * Checks if the repository contains at least one list with a given list name for a given user.
     *
     * @param owner Owner of the list.
     * @param listName Name of the list.
     *
     * @return Whether the list exists for that user or not.
     */
    boolean listExists(@NotNull String owner, @NotNull String listName);

    /**
     * Retrieves the list with a given list name for a given user. If the list does not exist, it produces a
     * {@link ShopListNotFoundException}.
     *
     * @param owner Owner of the list.
     * @param listName Name of the list.
     *
     * @return Whether the list exists for that user or not.
     * @throws ShopListNotFoundException When the list does not exist.
     */
    ShopList getShopList(@NotNull String owner, @NotNull String listName) throws ShopListNotFoundException;

    /**
     * Persist a new list into the repository. If the list violates its uniqueness constraint, it produces a
     * {@link ShopListAlreadyExistsException}.
     *
     * @param shopList Shop list to persist.
     *
     * @return A potentially modified instance of the original object that may contain persistence-generated
     *         fields (like insertion dates or autogenerated sequence numbers). Users of this API should use the
     *         returned instance after this call.
     * @throws ShopListAlreadyExistsException When the list already exists.
     */
    ShopList create(@NotNull ShopList shopList) throws ShopListAlreadyExistsException;

    /**
     * Updates an already persisted {@link ShopList} in the repository. If the list does not exist, it throws a
     * {@link ShopListNotFoundException}
     *
     * @param shopList ShopList to persist/update.
     *
     * @return A potentially modified instance of the original object that may contain persistence-generated
     *         fields (like insertion dates or autogenerated sequence numbers). Users of this API should use the
     *         returned instance after this call.
     */
    ShopList update(ShopList shopList) throws ShopListNotFoundException;

    /**
     * Checks how many lists does a given user own in this repository.
     *
     * @param owner Owner of the lists.
     *
     * @return The number of lists this user owns.
     */
    int userListQuantity(@NotNull String owner);
}
