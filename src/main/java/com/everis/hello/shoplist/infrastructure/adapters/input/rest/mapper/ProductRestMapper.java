package com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper;

import com.everis.hello.shoplist.app.domain.model.ProductDetail;
import com.everis.hello.shoplist.infrastructure.adapters.input.rest.model.ProductDetailView;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author EnricRG
 */
@Component
public class ProductRestMapper {

    public List<ProductDetailView> toDetailView(@NotNull List<ProductDetail> products) {
        List<ProductDetailView> views = new ArrayList<>();
        for (ProductDetail p : products) {
            views.add(this.toDetailView(p));
        }
        return views;
    }

    public ProductDetailView toDetailView(@NotNull ProductDetail p) {
        return new ProductDetailView(p.getId(), p.getName());
    }
}
