package com.everis.hello.shoplist;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
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
class AddProductIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void addProductToList_shouldReturnOk() throws AppException {
        String user = "userWith5Lists";
        String listName = "list1";
        Long product = 2L;

        ShopListEntity list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElse(null);
        assertNotNull(list);
        assertFalse(list.getItems().stream().anyMatch(x -> x.getProductId().equals(product))); //List does not contain the product

        ResponseEntity<String> response = controller.addProductToList(user, listName, product);

        // Although the instance may be the same, it's better to ask for fetch again.
        list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElse(null);
        assertNotNull(list);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Product added successfully"));
        assertEquals(1, list.getItems().size());
        assertTrue(list.getItems().stream().anyMatch(x -> x.getProductId().equals(product))); // List now contains the product.

        // Adding the same product again, should work ok and the list content should not change
        response = controller.addProductToList(user, listName, product);

        list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElse(null);
        assertNotNull(list);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Product was already in the list"));
        assertEquals(1, list.getItems().size());
        assertTrue(list.getItems().stream().anyMatch(x -> x.getProductId().equals(product))); // List now contains the product.
    }

}
