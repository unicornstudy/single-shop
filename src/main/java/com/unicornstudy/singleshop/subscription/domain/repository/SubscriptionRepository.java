package com.unicornstudy.singleshop.subscription.domain.repository;

import com.unicornstudy.singleshop.subscription.domain.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findSubscriptionByUserEmail(String userEmail, Pageable pageable);
}
