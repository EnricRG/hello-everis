package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.CannotCreateShopListException;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListEmptyException;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author EnricRG
 */
public interface CreateShopListUsecase {
    int MAX_LISTS_PER_USER = 5;

    /**
     * Creates a shop list with given products for a given user if user does not have already a list with the same name
     * and the user has not reached the limit of lists per user.
     *
     * @param owner User that owns the new list.
     * @param listName List name.
     * @param products Products that will be initially present in the new list.
     *
     * @return The new list.
     *
     * @throws ShopListAlreadyExistsException If the user already has a {@link ShopList} with the same name.
     * @throws MaxShopListsPerUserException If the user has reached the limit of {@link CreateShopListUsecase#MAX_LISTS_PER_USER}.
     */
    ShopList createShopList(@NotNull String owner, @NotNull String listName, @NotNull List<Long> products)
        throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException, ShopListEmptyException;
}
