package com.unicornstudy.singleshop.domain.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    @Query("select i from Items i order by i.id desc ")
    List<Items> findAllDesc();
}
