package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Shop list not found.")
public class ShopListNotFoundException extends AppException {

    public ShopListNotFoundException(String username, String listName) {
        super("User '" + username + "' does not have a list with name '" + listName + "'!");
    }
}
