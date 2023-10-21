package com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.ports.input.CreateShopListUsecase;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper.ShopListMapper;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/{username}/lists")
public class ShopListController {

    private final ShopListMapper shopListMapper;

    private final CreateShopListUsecase createUsecase;

    @Autowired
    public ShopListController(ShopListMapper shopListMapper, CreateShopListUsecase createUsecase) {
        this.shopListMapper = shopListMapper;
        this.createUsecase = createUsecase;
    }

    @PostMapping(value = "/{listName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ShopListView> createShopList(
        @PathVariable("username") String username,
        @PathVariable("listName") String listName
    ) throws ShopListAlreadyExistsException, MaxShopListsPerUserException
    {
        ShopList shopList = this.createUsecase.createShopList(username, listName);
        return ResponseEntity.ok(this.shopListMapper.toView(shopList));
    }
}
