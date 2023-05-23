package com.unicornstudy.singleshop.items.query.domain.repository;

import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemsSearchRepository extends ElasticsearchRepository<ItemsIndex, Long>, CrudRepository<ItemsIndex, Long>, CustomItemsSearchRepository {
}