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
public interface ShopListItemJpaRepository extends JpaRepository<ShopListEntity, Long> { }