package com.everis.hello.shoplist;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
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
class DeleteListIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void deleteExistingList_shouldReturnOk() throws AppException {
        String user = "userWith5Lists";
        String listName = "list1";

        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName)); // Shop list exists.
        ResponseEntity<Object> response = controller.deleteShopList(user, listName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName)); // Shop list does not exist anymore.
    }

    @Test
    @Transactional
    void deleteNonExistingList_shouldThrowNotFoundException() {
        String user = "userWith5Lists";
        String listName = "nonExistingList";

        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName)); // Shop list does not exist.
        assertThrows(ShopListNotFoundException.class, () -> controller.deleteShopList(user, listName));
    }
}
