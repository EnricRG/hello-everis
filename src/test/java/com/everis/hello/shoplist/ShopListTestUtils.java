package com.everis.hello.shoplist;

import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;

/**
 * @author EnricRG
 */
public class ShopListTestUtils {
    public static boolean shopListExists(ShopListJpaRepository repo, String owner, String listName) {
        return repo.findByOwnerAndListName(owner, listName).isPresent();
    }
}
