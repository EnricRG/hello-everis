package com.everis.hello.shoplist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Shop list is full.")
public class ShopListFullException extends Exception {

    public ShopListFullException(String listName) {
        super("List '" + listName + "' is full!");
    }
}
