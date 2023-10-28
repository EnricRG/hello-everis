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

import java.util.Optional;

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
    public boolean listExists(String owner, String listName) {
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

        if (this.listExists(owner, listName)) {
            throw new ShopListAlreadyExistsException(owner, listName);
        }

        ShopListEntity dbModel = this.mapper.toJpaModel(shopList, new ShopListEntity());
        dbModel = this.repo.save(dbModel);

        // toFullString is expensive performance-wise, only evaluate it when truly needed.
        if (log.isDebugEnabled()) {
            log.debug("Shop List persisted to DB: {}", dbModel.toStringFull());
        }

        return shopList; //Currently returning same instance, could change in the future.
    }

    @Override
    public ShopList update(ShopList shopList) throws ShopListNotFoundException {
        String listName = shopList.getName();
        String owner = shopList.getOwner();
        log.debug("Updating existing list with name '{}' for user '{}'", listName, owner);

        ShopListEntity list = this.repo.findByOwnerAndListName(owner, listName)
            .orElseThrow(() -> new ShopListNotFoundException(owner, listName));

        ShopListEntity dbModel = this.mapper.toJpaModel(shopList, list);
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

    @Override
    public boolean deleteList(String owner, String listName) {
        log.debug("Deleting list '{}' owned by '{}'.", listName, owner);
        boolean deleted = false;

        Optional<ShopListEntity> lOpt = this.repo.findByOwnerAndListName(owner, listName);
        if (lOpt.isPresent()) {
            this.repo.delete(lOpt.get());
            log.info("List '{}' owned by '{}' successfully deleted from the repository.", listName, owner);
            deleted = true;
        } else {
            log.warn("Called deletion on list '{}' owned by user '{}' that does not exist in the repository!",
                listName, owner);
        }

        return deleted;
    }
}
