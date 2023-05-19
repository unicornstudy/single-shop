package com.unicornstudy.singleshop.items.query.domain.repository;

import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsSearchRepository extends ElasticsearchRepository<ItemsIndex, Long>, CrudRepository<ItemsIndex, Long> {

    @Query("{\"bool\": {\"must\": [{\"bool\": {\"should\": [{\"wildcard\": {\"name\":\"*?0*\"}},{\"wildcard\": {\"description\": \"*?0*\"}}]}}," +
            "{\"range\": {\"price\": {\"gte\": ?1,\"lte\": ?2}}}," +
            "{\"wildcard\": {\"parentCategory\": \"*?3*\"}}," +
            "{\"wildcard\": {\"childCategory\": \"*?4*\"}}]}}")
    List<ItemsIndex> searchItemsByNamePriceAndCategory(String name, Integer price1, Integer price2, String parentCategory, String childCategory, Pageable pageable);
}