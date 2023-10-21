package com.everis.hello.shoplist.app.domain;


import com.everis.hello.shoplist.app.exception.ShopListFullException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class ShopList {

    private static final int MAX_ITEMS_PER_LIST = 25;

    private final String name;
    private final String user;
    private final List<Long> items = new ArrayList<>();

    private boolean inGracePeriod; // Differentiates recently created lists that are empty and must not be deleted.

    public ShopList(String name, String user) {
        this.name = name;
        this.user = user;
        this.inGracePeriod = true;
    }

    /** Empty shop lists must be deleted. */
    public boolean needsDeletion() {
        return !this.inGracePeriod && this.items.isEmpty();
    }

    public boolean isFull() {
        return this.items.size() >= MAX_ITEMS_PER_LIST;
    }

    public List<Long> getItems() {
        // Returns an unmodifiable copy of the array (does not copy the elements itself)
        return Collections.unmodifiableList(this.items);
    }

    public void addProduct(Long product) throws ShopListFullException {
        if (this.isFull()) {
            throw new ShopListFullException(this.getName());
        }
        this.inGracePeriod = false;
        this.items.add(product);
    }
}
