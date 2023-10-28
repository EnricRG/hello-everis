package com.everis.hello.shoplist.app.ports.output;

import com.everis.hello.shoplist.app.domain.model.ProductDetail;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Provider of shopping products' details given its IDs.
 *
 * @author EnricRG
 */
public interface ProductDetailProvider {

    /**
     * Returns the details of the products identified by the given IDs. If some products don't exist in the backing
     * service, it may cause the result of this call to be smaller than the provided ID list. Implementations must not
     * fail when a product is not found or cannot be retrieved for some reason. Also, implementations must not return
     * details of any product that is not included in the ID list.
     * <p>
     * So, in other words, the result must be less or equal in length and must not include details for products not
     * included in the ID list.
     *
     * @param ids IDs of products to fetch details of.
     *
     * @return A list of product details. Never null.
     */
    List<ProductDetail> getDetails(@NotNull List<Long> ids);
}
