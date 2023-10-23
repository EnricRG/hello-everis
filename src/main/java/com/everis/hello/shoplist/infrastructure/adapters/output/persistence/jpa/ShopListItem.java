package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author EnricRG
 */
@Entity
@Table(
    catalog = "shoplist",
    name = "shop_lists_items",
    indexes = {
        @Index(name = "UK_shop_lists_items", columnList = "shopList_id,product_id", unique = true)
    })
@Getter
@Setter
@NoArgsConstructor
public class ShopListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopList_id", nullable = false)
    private ShopListEntity shopList;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    public ShopListItem(ShopListEntity shopList, Long productId) {
        this.shopList = shopList;
        this.productId = productId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "id=" + id +
            ", shopList=" + shopList.getId() +
            '}';
    }
}
