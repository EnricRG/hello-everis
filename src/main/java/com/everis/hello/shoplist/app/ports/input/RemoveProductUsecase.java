package com.everis.hello.shoplist.app.ports.input;

import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;

/**
 * @author EnricRG
 */
public interface RemoveProductUsecase {

    /**
     * Removes a given product from the list owned by user, returning the size of the updated list. Throws a
     * {@link ShopListNotFoundException} if the list does not exist. If the list becomes empty because of this operation,
     * it gets deleted. This is indicated by a return value of 0.
     * <p>
     * If this method ends successfully it can be assumed that the list no longer contains the product. The method must
     * not fail if the list does not contain the product, and it should return as usual. This can be identified by
     * checking the size of the list before and after this operation.
     *
     * @param owner Owner of the list.
     * @param listName List name. Must exist a list with this name for the given user.
     * @param productId Product ID. Assumed to be valid.
     *
     * @return The current number of products that the updated list contains after the execution.
     * @throws ShopListNotFoundException If the list does not exist.
     */
    int removeProduct(String owner, String listName, Long productId) throws ShopListNotFoundException;
}
