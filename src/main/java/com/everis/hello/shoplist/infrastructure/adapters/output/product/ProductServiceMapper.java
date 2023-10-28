package com.everis.hello.shoplist.infrastructure.adapters.output.product;

import com.everis.hello.shoplist.app.domain.model.ProductDetail;
import org.springframework.stereotype.Component;

/**
 * @author EnricRG
 */
@Component
public class ProductServiceMapper {

    public ProductDetail fromView(ProductView view) {
        if (view == null) return null;
        return new ProductDetail(view.getId(), view.getName(), view.getSize(), view.getPrice(), view.getColor());
    }
}
