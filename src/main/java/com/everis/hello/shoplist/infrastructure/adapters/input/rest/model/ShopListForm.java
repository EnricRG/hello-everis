package com.everis.hello.shoplist.infrastructure.adapters.input.rest.model;

import com.everis.hello.shoplist.app.domain.ShopList;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Input model for {@link ShopList} creation.
 * Bear in mind that modifying object fields imply modifying expected deserialization format.
 * @author EnricRG
 */
@NoArgsConstructor
@AllArgsConstructor
public class ShopListForm {
    @NotEmpty @Size(max = ShopList.MAX_ITEMS_PER_LIST)
    public List<Long> products;
}
