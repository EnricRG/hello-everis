package com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.CannotCreateShopListException;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListEmptyException;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper.ShopListRestMapper;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListForm;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author EnricRG
 */
@RestController
@RequestMapping("/user/{owner}/lists")
public class ShopListController {

    private final ShopListRestMapper shopListMapper;

    private final CreateShopListUsecase createUsecase;

    @Autowired
    public ShopListController(ShopListRestMapper shopListMapper, CreateShopListUsecase createUsecase) {
        this.shopListMapper = shopListMapper;
        this.createUsecase = createUsecase;
    }

    @PostMapping(value = "/{listName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ShopListSimpleView> createShopList(
        @PathVariable("owner") String owner,
        @PathVariable("listName") String listName,
        @RequestBody @Valid ShopListForm form
    ) throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException, ShopListEmptyException
    {
        ShopList shopList = this.createUsecase.createShopList(owner, listName, form.products);
        return ResponseEntity.ok(this.shopListMapper.toSimpleView(shopList));
    }
}
