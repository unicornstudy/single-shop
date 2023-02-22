package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.orders.exception.OrderExceptionCheckFactory;
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
public class Items extends BaseTimeEntity {

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

    public void update(Long id, String name, Integer price, String description, Integer quantity, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.modifiedDate = modifiedDate;
    }

    public void addQuantity() {
        this.quantity += 1;
    }

    public void subtractQuantity() {
        OrderExceptionCheckFactory.checkQuantity(this);
        this.quantity -= 1;
    }
}
