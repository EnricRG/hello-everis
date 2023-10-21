package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.repository;

import com.everis.hello.shoplist.app.domain.ShopList;
import com.everis.hello.shoplist.app.exception.ShopListAlreadyExistsException;
import com.everis.hello.shoplist.app.exception.ShopListNotFoundException;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.mapper.ShopListJpaMapper;
import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa.ShopListJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

/**
 * @author EnricRG
 */
@Repository
@Slf4j
public class ShopListSpringRepository implements ShopListRepository {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "id");

    private final ShopListJpaRepository repo;
    private final ShopListJpaMapper mapper;

    @Autowired
    public ShopListSpringRepository(ShopListJpaRepository repo, ShopListJpaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public boolean existsList(String owner, String listName) {
        log.debug("Checking if list '{}' exists for user '{}'", listName, owner);

        // Can be optimized: query to retrieve only the existence of the element without fetching its fields.
        return this.repo.findByOwnerAndListName(owner, listName).isPresent();
    }

    @Override
    public ShopList getShopList(String owner, String listName) throws ShopListNotFoundException {
        log.debug("Retrieving shop list with name '{}' for user '{}'", listName, owner);

        ShopListEntity dbModel = this.repo.findByOwnerAndListName(owner, listName)
            .orElseThrow(() -> new ShopListNotFoundException(owner, listName));

        // toFullString is expensive performance-wise, only evaluate it when truly needed.
        if (log.isDebugEnabled()) {
            log.debug("Shop List retrieved from DB: {}", dbModel.toStringFull());
        }

        return this.mapper.toDomain(dbModel);
    }

    @Override
    public ShopList create(ShopList shopList) throws ShopListAlreadyExistsException {
        String listName = shopList.getName();
        String owner = shopList.getOwner();
        log.debug("Creating new shop list with name '{}' for user '{}'", listName, owner);

        if (this.existsList(owner, listName)) {
            throw new ShopListAlreadyExistsException(owner, listName);
        }

        ShopListEntity dbModel = this.mapper.toJpa(shopList);
        dbModel = this.repo.save(dbModel);

        // toFullString is expensive performance-wise, only evaluate it when truly needed.
        if (log.isDebugEnabled()) {
            log.debug("Shop List persisted to DB: {}", dbModel.toStringFull());
        }

        return shopList; //Currently returning same instance, could change in the future.
    }

    @Override
    public int userListQuantity(String owner) {
        log.debug("Checking how many lists does user '{}' own.", owner);
        return this.repo.findByOwner(owner, DEFAULT_SORT).size();
    }
}
