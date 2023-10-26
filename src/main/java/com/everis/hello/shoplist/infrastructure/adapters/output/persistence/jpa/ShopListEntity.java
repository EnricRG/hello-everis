package com.everis.hello.shoplist.infrastructure.adapters.output.persistence.jpa;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author EnricRG
 */
@Entity
@Table(
    catalog = "shoplist",
    name = "shop_lists",
    indexes = {
        @Index(name = "UK_shop_lists", columnList = "name,owner", unique = true)
    })
@Getter
@Setter
@RequiredArgsConstructor
public class ShopListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner", nullable = false)
    private String owner;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShopListItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopListEntity)) return false;
        ShopListEntity that = (ShopListEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            ", items=[...]" +
            '}';
    }

    public String toStringFull() {
        return this.getClass().getSimpleName() + "{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            ", items=" + items.stream().map(ShopListItem::getId).collect(Collectors.toList()) +
            '}';
    }
}
