package com.unicornstudy.singleshop.common.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Pageable;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public <T> List<T> performSearch(BoolQueryBuilder queryBuilder, String indexName, Pageable pageable, Class<T> resultClass) {
        SearchSourceBuilder searchSourceBuilder = createSearchSourceBuilder(queryBuilder, pageable);
        SearchRequest searchRequest = createSearchRequest(searchSourceBuilder, indexName);
        SearchHits hits = createSearchHits(searchRequest);

        return Arrays.stream(hits.getHits())
                .map(hit -> objectMapper.convertValue(hit.getSourceAsMap(), resultClass))
                .toList();
    }

    private SearchRequest createSearchRequest(SearchSourceBuilder searchSourceBuilder, String indexName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    private SearchHits createSearchHits(SearchRequest searchRequest)  {
        SearchHits hits = null;
        try {
            hits = client.search(searchRequest, RequestOptions.DEFAULT).getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hits;
    }

    private SearchSourceBuilder createSearchSourceBuilder(BoolQueryBuilder queryBuilder, Pageable pageable) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.from((int) pageable.getOffset());
        searchSourceBuilder.size(pageable.getPageSize());

        return searchSourceBuilder;
    }
}
