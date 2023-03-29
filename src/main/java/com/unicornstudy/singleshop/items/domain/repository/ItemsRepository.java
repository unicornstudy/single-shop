package com.unicornstudy.singleshop.items.domain.repository;

import com.unicornstudy.singleshop.items.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, Long> {
}
