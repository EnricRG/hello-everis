package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author EnricRG
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Shop list already exists.")
public class ShopListAlreadyExistsException extends AppException {
    public ShopListAlreadyExistsException(String owner, String listName) {
        super("List '" + listName + "' already exists for user '" + owner + "'!");
    }
}
