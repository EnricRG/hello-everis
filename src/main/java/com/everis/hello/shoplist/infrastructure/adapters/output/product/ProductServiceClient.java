package com.everis.hello.shoplist.infrastructure.adapters.output.product;

import com.everis.hello.shoplist.app.domain.model.ProductDetail;
import com.everis.hello.shoplist.app.ports.output.ProductDetailProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author EnricRG
 */
@Component
@Slf4j
public class ProductServiceClient implements ProductDetailProvider {

    private static final String PRODUCT_DETAIL_ENDPOINT_TEMPLATE = "/product/{productId}";

    private final RestTemplate restTemplate;

    @Autowired
    public ProductServiceClient(@Qualifier(ProductServiceRestConfig.REST_TEMPLATE_BEAN) RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProductDetail> getDetails(@NotNull List<Long> ids) {
        return ids.stream()
            .map(this::getDetails)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private Optional<ProductDetail> getDetails(@NotNull Long id) {
        log.debug("Retrieving product data for product {}.", id);

        ProductDetail details = null;
        try {
            ResponseEntity<ProductDetail> response = restTemplate.getForEntity(PRODUCT_DETAIL_ENDPOINT_TEMPLATE, ProductDetail.class,
                "productId", id);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
            if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("Product {} not found on product service.", id);
            }
            details = response.getBody();
            log.debug("Product retrieved: {}", details);
        } catch (ResourceAccessException rae) {
            log.error("Problem connecting with product service.", rae);
        } catch (Exception e) {
            log.error("Unexpected exception fetching information from product service.", e);
        }

        return Optional.ofNullable(details);
    }


}
