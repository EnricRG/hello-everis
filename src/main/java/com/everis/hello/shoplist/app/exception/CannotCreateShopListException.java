package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author EnricRG
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cannot create shop list.")
public class CannotCreateShopListException extends AppException {
    public CannotCreateShopListException(String owner, String detail) {
        super("Cannot create shop list for user '" + owner + "': " + detail);
    }
}
