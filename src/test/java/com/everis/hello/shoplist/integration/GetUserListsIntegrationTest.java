package com.everis.hello.shoplist.integration;

import com.everis.hello.shoplist.infrastructure.adapters.input.rest.controller.ShopListController;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ShopListSimpleView;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GetUserListsIntegrationTest {

    @Autowired
    private ShopListController controller;

    @Autowired
    private ShopListJpaRepository shopListJpaRepo;

    @Test
    @Transactional
    void getListsForUserWithLists_shouldReturnOk() {
        String user = "userWith5Lists";

        // Same sort as implementation
        List<ShopListEntity> dbModels = shopListJpaRepo.findByOwner(user, Sort.by(Sort.Direction.DESC, "id"));
        int numberOfLists = dbModels.size();
        assertTrue(numberOfLists > 0);

        ResponseEntity<List<ShopListSimpleView>> response = controller.getUserLists(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<ShopListSimpleView> views = response.getBody();
        assertEquals(numberOfLists, views.size());
        for (int i = 0; i < views.size(); i++) {
            ShopListEntity dbModel = dbModels.get(i);
            ShopListSimpleView view = views.get(i);
            assertEquals(view.getName(), dbModel.getName());
            assertEquals(view.getProductNumber(), dbModel.getItems().size());
        }
    }

    // Equivalent to a user with no lists.
    @Test
    @Transactional
    void getListsForUnknownUser_shouldReturnOkEmptyList() {
        String user = "unknownUser";

        // Same sort as implementation
        List<ShopListEntity> dbModels = shopListJpaRepo.findByOwner(user, Sort.by(Sort.Direction.DESC, "id"));
        assertTrue(dbModels.isEmpty());

        ResponseEntity<List<ShopListSimpleView>> response = controller.getUserLists(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}
