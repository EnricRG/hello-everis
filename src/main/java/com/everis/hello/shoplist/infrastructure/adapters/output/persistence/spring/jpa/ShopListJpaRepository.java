package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.spring.jpa;

import com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa.ShopListEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author EnricRG
 */
public interface ShopListJpaRepository extends JpaRepository<ShopListEntity, Long> {

    @Query("SELECT l FROM ShopListEntity l " +
        "WHERE l.owner = :owner AND l.name = :listName")
    Optional<ShopListEntity> findByOwnerAndListName(
        @Param("owner") @NotNull String owner,
        @Param("listName") @NotNull String listName
    );

    @Query("SELECT l FROM ShopListEntity l WHERE l.owner = :owner")
    List<ShopListEntity> findByOwner(@Param("owner") String owner, Sort sort);
}