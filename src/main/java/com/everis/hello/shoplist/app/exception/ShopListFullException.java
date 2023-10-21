package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author EnricRG
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Shop list is full.")
public class ShopListFullException extends AppException {

    public ShopListFullException(String listName) {
        super("List '" + listName + "' is full!");
    }
}
