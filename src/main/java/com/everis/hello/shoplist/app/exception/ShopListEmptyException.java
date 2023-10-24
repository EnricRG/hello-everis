package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author EnricRG
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cannot create an empty shop list.")
public class ShopListEmptyException extends AppException {
    public ShopListEmptyException(String owner) {
        super("Cannot create an empty shop list for user '" + owner + "'.");
    }
}
