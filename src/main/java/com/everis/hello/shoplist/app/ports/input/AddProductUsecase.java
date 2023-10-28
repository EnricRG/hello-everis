package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.ShopListFullException;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;

/**
 * @author EnricRG
 */
public interface AddProductUsecase {

    /**
     * Adds a product to an existing {@link ShopList}.
     *
     * @param owner Owner of the list.
     * @param listName List name. Must exist a list with this name for the given user.
     * @param productId Product ID. Assumed to be valid.
     *
     * @return Whether the product was added to the list ({@code true}) or it was already present in it {@code false}.
     *         In other words: returns whether the list increased in size after this operation or not.
     *
     * @throws ShopListNotFoundException If the list does not exist.
     * @throws ShopListFullException If the list has reached the max number of elements allowed.
     */
    boolean addProduct(String owner, String listName, Long productId) throws ShopListNotFoundException, ShopListFullException;
}
