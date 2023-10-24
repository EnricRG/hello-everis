package com.everis.hello.shoplist.infrastructure.config;

import com.everis.hello.shoplist.app.domain.ShopListService;
import com.everis.hello.shoplist.app.ports.output.ShopListRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Usually I would use @Component-derived annotations for DI even inside the business core, but this time I wanted to
// showcase a cleaner domain.
/**
 * @author EnricRG
 */
@Configuration
public class ShopListBeans {

    @Bean
    public ShopListService service(ShopListRepository repo) {
        return new ShopListService(repo);
    }
}
