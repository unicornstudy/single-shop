package com.unicornstudy.singleshop.items.command.domain.repository;

import com.unicornstudy.singleshop.items.command.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items, Long> {
}
