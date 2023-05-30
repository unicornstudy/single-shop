package com.unicornstudy.singleshop.common.infrastructure.elasticsearch;

import com.unicornstudy.singleshop.items.query.domain.repository.ItemsSearchRepository;
import com.unicornstudy.singleshop.orders.query.domain.repository.OrderSearchRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackageClasses = {ItemsSearchRepository.class, OrderSearchRepository.class})
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${elasticsearch-hostAndPort}")
    String elasticsearchHostAndPort;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchHostAndPort)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
