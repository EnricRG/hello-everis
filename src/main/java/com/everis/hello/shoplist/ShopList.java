package com.everis.hello.shoplist;


import com.everis.hello.shoplist.exception.ShopListFullException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class ShopList {

    private static final int MAX_ITEMS_PER_LIST = 25;

    private final String name;
    private final String user;
    private final List<Long> items = new ArrayList<>(); //TODO add domain model

    public ShopList(String name, String user) {
        this.name = name;
        this.user = user;
    }

    /** Empty shop lists must be deleted. */
    public boolean needsDeletion() {
        return items.isEmpty();
    }

    public boolean isFull() {
        return items.size() == MAX_ITEMS_PER_LIST;
    }

    public void addProduct(Long product) throws ShopListFullException {
        if (this.isFull()) {
            throw new ShopListFullException(this.getName());
        }
    }
}
