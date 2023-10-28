package com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.*;
import com.everis.hello.shoplist.app.ports.input.*;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper.ShopListRestMapper;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListForm;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author EnricRG
 */
@RestController
@RequestMapping("/user/{owner}/lists")
@Slf4j
public class ShopListController {

    private final ShopListRestMapper shopListMapper;

    private final CreateShopListUsecase createUsecase;
    private final AddProductUsecase addProductUsecase;
    private final DeleteShopListUsecase deleteUsecase;
    private final RemoveProductUsecase removeProductUsecase;
    private final UserListsUsecase userListsUsecase;

    @Autowired
    public ShopListController(ShopListRestMapper shopListMapper,
                              CreateShopListUsecase createUsecase,
                              AddProductUsecase addProductUsecase,
                              DeleteShopListUsecase deleteUsecase,
                              RemoveProductUsecase removeProductUsecase, UserListsUsecase userListsUsecase) {
        this.shopListMapper = shopListMapper;
        this.createUsecase = createUsecase;
        this.addProductUsecase = addProductUsecase;
        this.deleteUsecase = deleteUsecase;
        this.removeProductUsecase = removeProductUsecase;
        this.userListsUsecase = userListsUsecase;
    }

    @PostMapping(value = "/{listName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ShopListSimpleView> createShopList(
        @PathVariable("owner") String owner,
        @PathVariable("listName") String listName,
        @RequestBody @Valid ShopListForm form
    ) throws ShopListAlreadyExistsException, MaxShopListsPerUserException, CannotCreateShopListException, ShopListEmptyException
    {
        log.debug("Request received on createShopList. Owner: '{}', listName: '{}', form: {}", owner, listName, form);
        ShopList shopList = this.createUsecase.createShopList(owner, listName, form.getProducts());
        ShopListSimpleView view = this.shopListMapper.toSimpleView(shopList);
        log.debug("createShopList result view: {}", view);
        return ResponseEntity.ok(view);
    }

    @PutMapping(value = "/{listName}/products/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> addProductToList(
        @PathVariable("owner") String owner,
        @PathVariable("listName") String listName,
        @PathVariable("productId") Long productId
    ) throws ShopListFullException, ShopListNotFoundException
    {
        log.debug("Request received on addProductToList. Owner: '{}', listName: '{}', productId: {}", owner, listName, productId);
        boolean productAdded = this.addProductUsecase.addProduct(owner, listName, productId);
        String result = productAdded ?
            "Product added successfully to list." :
            "Product was already in the list.";
        log.debug("addProductToList result view: {}", result);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/{listName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> deleteShopList(
        @PathVariable("owner") String owner,
        @PathVariable("listName") String listName
    ) throws ShopListNotFoundException {
        log.debug("Request received on deleteShopList. Owner: '{}', listName: '{}'", owner, listName);
        this.deleteUsecase.deleteList(owner, listName);
        log.debug("addProductToList successfully ran.");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{listName}/products/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Integer> removeProductFromList(
        @PathVariable("owner") String owner,
        @PathVariable("listName") String listName,
        @PathVariable("productId") Long productId
    ) throws ShopListNotFoundException
    {
        log.debug("Request received on removeProductFromList. Owner: '{}', listName: '{}', productId: {}", owner, listName, productId);
        int listSize = this.removeProductUsecase.removeProduct(owner, listName, productId);
        log.debug("removeProductFromList items remaining: {}", listSize);
        return ResponseEntity.ok().body(listSize);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ShopListSimpleView>> getUserLists(
        @PathVariable("owner") String owner
    ) {
        log.debug("Request received on getUserLists. Owner: '{}'.", owner);
        List<ShopList> shopLists = this.userListsUsecase.getListsForUser(owner);
        List<ShopListSimpleView> view = this.shopListMapper.toSimpleView(shopLists);
        log.debug("removeProductFromList shop lists for user '{}': {}", owner, view);
        return ResponseEntity.ok().body(view);
    }
}
