package com.everis.hello.shoplist;

import com.everis.hello.AppException;
import com.everis.hello.shoplist.app.exception.CannotCreateShopListException;
import com.everis.hello.shoplist.app.exception.MaxShopListsPerUserException;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListEmptyException;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListForm;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CreateShopListIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @BeforeEach
    protected void onBefore() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShopList_shouldReturnOk() throws AppException {
        String user = "user1";
        String listName = "list1";
        List<Long> products = List.of(1L,2L);

        assertFalse(shopListJpaRepo.findByOwnerAndListName(user, listName).isPresent());

        ResponseEntity<ShopListSimpleView> response =
            controller.createShopList(user, listName, new ShopListForm(products));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(listName, response.getBody().name);

        assertTrue(shopListJpaRepo.findByOwnerAndListName(user, listName).isPresent());
    }
}
