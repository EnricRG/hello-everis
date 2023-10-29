package com.everis.hello.shoplist.integration;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.ShopListTestUtils;
import com.everis.hello.shoplist.app.exception.CannotCreateShopListException;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListEmptyException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListForm;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CreateShopListIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void createShopList_shouldReturnOk() throws AppException {
        String user = "user1";
        String listName = "list1";
        List<Long> products = List.of(1L,2L);

        Assertions.assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));

        ResponseEntity<ShopListSimpleView> response =
            controller.createShopList(user, listName, new ShopListForm(products));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(listName, response.getBody().getName());
        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
    }

    @Test
    @Transactional
    void createRepeatedShopList_shouldThrowAlreadyExistException() throws AppException {
        String user = "user1";
        String listName = "list1";
        List<Long> products = List.of(1L,2L);

        controller.createShopList(user, listName, new ShopListForm(products));
        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));

        List<Long> products2 = List.of(3L);
        assertThrows(
            ShopListAlreadyExistsException.class,
            () -> controller.createShopList(user, listName, new ShopListForm(products2))
        );
        // Check that persisted list does not contain elements of the second one (it hasn't changed).
        assertTrue(shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow().getItems().stream()
            .noneMatch(x -> x.getProductId().equals(3L)));
    }

    @Test
    @Transactional
    void create6thShopListForUser_shouldThrowMaxListException() {
        String user = "userWith5Lists";
        String listName = "list6";
        List<Long> products = List.of(1L,2L);

        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        assertThrows(
            MaxShopListsPerUserException.class,
            () -> controller.createShopList(user, listName, new ShopListForm(products))
        );
        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
    }

    @Test
    @Transactional
    void createEmptyShopList_shouldThrowEmptyShopListException() {
        String user = "user1";
        String listName = "list1";
        List<Long> products = List.of();

        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        assertThrows(
            ShopListEmptyException.class,
            () -> controller.createShopList(user, listName, new ShopListForm(products))
        );
        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
    }

    @Test
    @Transactional
    void create26ElementShopList_shouldThrowCannotCreateException() throws AppException {
        String user = "user1";
        String listName = "list1"; // List with 25 elements
        List<Long> products = LongStream.rangeClosed(1, 25).boxed().collect(Collectors.toList());

        assertFalse(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        ResponseEntity<ShopListSimpleView> response =
            controller.createShopList(user, listName, new ShopListForm(products));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(listName, response.getBody().getName());
        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));

        // 25 elements were ok, let's try 26.
        String listName2 = "list2";
        List<Long> products2 = LongStream.rangeClosed(1, 26).boxed().collect(Collectors.toList());

        assertFalse(shopListJpaRepo.findByOwnerAndListName(user, listName2).isPresent());
        Exception ex = assertThrows(
            CannotCreateShopListException.class,
            () -> controller.createShopList(user, listName2, new ShopListForm(products2))
        );
        assertTrue(ex.getMessage().contains("max product number exceeded"));
        assertFalse(shopListJpaRepo.findByOwnerAndListName(user, listName2).isPresent());
    }
}
