package com.unicornstudy.singleshop.orders.query.domain.repository;

import com.unicornstudy.singleshop.orders.query.domain.OrderIndex;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.unicornstudy.singleshop.common.elasticsearch.ElasticSearchService;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomOrderSearchRepositoryImpl implements CustomOrderSearchRepository {

    private final ElasticSearchService elasticSearchService;
    @Override
    public List<OrderIndex> findByOrderDatePrefix(String startDate, String endDate, Pageable pageable) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("orderDate").gte(startDate).lt(endDate));

        return elasticSearchService.performSearch(queryBuilder, "orders", pageable, OrderIndex.class);
    }

    @Override
    public List<OrderIndex> findByOrderDateAndUserEmail(String startDate, String endDate, String userEmail, Pageable pageable) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("orderDate").gte(startDate).lt(endDate))
                .must(QueryBuilders.termQuery("userEmail", userEmail));

        return elasticSearchService.performSearch(queryBuilder, "orders", pageable, OrderIndex.class);
    }
}
