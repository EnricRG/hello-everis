package com.everis.hello.shoplist.infrastructure.config;

import com.everis.hello.shoplist.infrastructure.adapters.input.rest.mapper.ShopListMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopListBeans {

    @Bean
    public ShopListMapper mapper() {
        return new ShopListMapper();
    }

}
