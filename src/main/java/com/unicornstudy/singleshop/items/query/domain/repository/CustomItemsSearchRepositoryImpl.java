package com.unicornstudy.singleshop.items.query.domain.repository;

import com.unicornstudy.singleshop.common.elasticsearch.ElasticSearchService;
import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomItemsSearchRepositoryImpl implements CustomItemsSearchRepository {

    private final ElasticSearchService elasticSearchService;

    @Override
    public List<ItemsIndex> findItems(String name, Integer price1, Integer price2, String parentCategory, String childCategory, Pageable pageable) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(
                        QueryBuilders.boolQuery()
                                .should(QueryBuilders.wildcardQuery("name", "*" + name + "*"))
                                .should(QueryBuilders.wildcardQuery("description", "*" + name + "*"))
                )
                .must(QueryBuilders.rangeQuery("price").gte(price1).lte(price2))
                .must(QueryBuilders.wildcardQuery("parentCategory", "*" + parentCategory + "*"))
                .must(QueryBuilders.wildcardQuery("childCategory", "*" + childCategory + "*"));

        return elasticSearchService.performSearch(queryBuilder, "items", pageable, ItemsIndex.class);
    }
}
