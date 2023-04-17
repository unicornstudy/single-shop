package com.unicornstudy.singleshop.items.domain;

import com.unicornstudy.singleshop.exception.orders.OrderExceptionCheckFactory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Items {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Integer price;

    private String description;

    private Integer quantity;

    private LocalDateTime modifiedDate;

    private LocalDateTime createdDate;

    public void update(Long id, String name, Integer price, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.modifiedDate = LocalDateTime.now();
    }

    public void increaseQuantity() {
        this.quantity += 1;
    }

    public void decreaseQuantity() {
        OrderExceptionCheckFactory.checkQuantity(this);
        this.quantity -= 1;
    }
}
