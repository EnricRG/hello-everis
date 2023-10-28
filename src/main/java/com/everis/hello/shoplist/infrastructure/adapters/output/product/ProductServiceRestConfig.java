package com.everis.hello.shoplist.infrastructure.adapters.output.product;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author EnricRG
 */
@Configuration
public class ProductServiceRestConfig {

    public static final String REST_TEMPLATE_BEAN = "ProductRestTemplate";

    @NotEmpty
    @Value("${everis.products.url}")
    private String serviceUrl; //Base service URL (scheme://host:port)

    @NotNull
    @Value("${everis.products.timeout:30000}")
    private Integer timeout; // Timeout in milliseconds.

    // Source: https://www.baeldung.com/rest-template
    @Bean(REST_TEMPLATE_BEAN)
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate(getClientHttpRequestFactory());

        template.setUriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl));

        // Circumvent the fact that the product service returns application/octet-stream instead of application/json
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        template.setMessageConverters(List.of(converter));

        return template;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();

        CloseableHttpClient client = HttpClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }
}
