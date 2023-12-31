package com.everis.hello.shoplist.app.domain.model;


import com.everis.hello.shoplist.app.exception.ShopListFullException;
import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author EnricRG
 */
@Getter
@ToString
@Validated
public class ShopList {

    public static final int MAX_ITEMS_PER_LIST = 25;

    private final String name;
    private final String owner;
    private final List<Long> items = new ArrayList<>();

    // Only for delayed construction purposes, should not be used in business logic.
    public ShopList(@NotNull String name, @NotNull String owner) {
        this.name = name;
        this.owner = owner;
    }

    public ShopList(@NotNull String name, @NotNull String owner, @NotEmpty @Size(max = MAX_ITEMS_PER_LIST) List<Long> products) {
        this.name = name;
        this.owner = owner;
        this.items.addAll(products);
    }

    /** Empty shop lists must be deleted. */
    public boolean needsDeletion() {
        return this.items.isEmpty();
    }

    public boolean isFull() {
        return this.items.size() >= MAX_ITEMS_PER_LIST;
    }

    public List<Long> getItems() {
        // Returns an unmodifiable copy of the array (does not copy the elements itself)
        return Collections.unmodifiableList(this.items);
    }

    public int getNumberOfItems() {
        return this.items.size();
    }

    // You must call this from business logic.
    public boolean addProduct(Long productId) throws ShopListFullException {
        if (this.isFull()) {
            throw new ShopListFullException(this.getName());
        }
        return this.addProductNoConstraint(productId);
    }

    // You should call this from database operations.
    public boolean addProductNoConstraint(Long productId) {
        boolean added = false;
        if (!this.items.contains(productId)) {
            this.items.add(productId);
            added = true;
        }
        return added;
    }

    public int removeProduct(Long productId) {
        this.items.remove(productId);
        return this.items.size();
    }
}
