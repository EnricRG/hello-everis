package com.everis.hello.shoplist.integration;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.ShopListTestUtils;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RemoveProductIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void removeProductFromListWithMoreThan1Element_shouldReturnOk() throws AppException {
        String user = "userWith5Lists";
        String listName = "list1";
        Long productId = 2L;

        ShopListEntity list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        int originalListSize = list.getItems().size();
        assertEquals(2, originalListSize);
        ResponseEntity<Integer> response = controller.removeProductFromList(user, listName, productId);

        list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        assertEquals(originalListSize - 1, list.getItems().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list.getItems().size(), response.getBody());
    }

    @Test
    @Transactional
    void removeProductFromListWith1Element_shouldReturnOkAndDeleteTheList() throws AppException {
        String user = "userWith5Lists";
        String listName = "list2";
        Long productId = 1L;

        ShopListEntity list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        assertEquals(1, list.getItems().size());
        ResponseEntity<Integer> response = controller.removeProductFromList(user, listName, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody());

        Assertions.assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
    }

    @Test
    @Transactional
    void removeProductFromNonExistingList_shouldThrowNotFoundException() {
        String user = "userWith5Lists";
        String listName = "nonExistingList";
        Long productId = 1L;

        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        assertThrows(ShopListNotFoundException.class, () -> controller.removeProductFromList(user, listName, productId));
    }
}
