package com.everis.hello.shoplist.integration;

import com.everis.hello.shoplist.ShopListTestUtils;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.DetailedShopListView;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListItem;
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
class GetListDetailsIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void getDetailsForExistingListWithNoUnknownProducts_shouldReturnOkWithNoProductRemovals() throws ShopListNotFoundException {
        String user = "userWith5Lists";
        String listName = "list1";

        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        ShopListEntity list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        int initialProductNumber = list.getItems().size();

        ResponseEntity<DetailedShopListView> response = controller.getListDetails(user, listName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        assertEquals(initialProductNumber, list.getItems().size()); // Check that no products have been deleted

        DetailedShopListView detailView = response.getBody();
        assertEquals(detailView.getProducts().size(), list.getItems().size());
        // Check that all items stored in db are represented in the view.
        for (ShopListItem dbItem : list.getItems()) {
            assertTrue(detailView.getProducts().stream().anyMatch(x -> x.getId().equals(dbItem.getProductId())));
        }
    }

    @Test
    @Transactional
    void getDetailsForExistingListWithUnknownProducts_shouldReturnOkWithSomeProductRemovals() throws ShopListNotFoundException {
        String user = "userWithFullShopList";
        String listName = "fullShopList"; // List with 25 elements ranging from 1 to 25, some do not exist in the product service.

        assertTrue(ShopListTestUtils.shopListExists(shopListJpaRepo, user, listName));
        ShopListEntity list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        int initialProductNumber = list.getItems().size();

        ResponseEntity<DetailedShopListView> response = controller.getListDetails(user, listName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        list = shopListJpaRepo.findByOwnerAndListName(user, listName).orElseThrow();
        assertTrue(initialProductNumber > list.getItems().size()); // Check that some products have been deleted

        DetailedShopListView detailView = response.getBody();
        assertEquals(detailView.getProducts().size(), list.getItems().size());
        // Check that all items stored in db are represented in the view.
        for (ShopListItem dbItem : list.getItems()) {
            assertTrue(detailView.getProducts().stream().anyMatch(x -> x.getId().equals(dbItem.getProductId())));
        }

        // We know mock service contains products ranging from IDs [1,15] and the list contained IDs up to 25
        // so 10 products should have been deleted.
        assertEquals(15, list.getItems().size());
        assertTrue(list.getItems().stream().noneMatch(x -> x.getProductId() > 15));
    }
}
