package com.everis.hello.shoplist.app.exception;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT,
    reason = "User has reached the limit of " + CreateShopListUsecase.MAX_LISTS_PER_USER + " lists per user!")
public class MaxShopListsPerUserException extends AppException {
    public MaxShopListsPerUserException(String username) {
        super("User '" + username + "' has reached the limit of " + CreateShopListUsecase.MAX_LISTS_PER_USER + " lists per user!");
    }
}
