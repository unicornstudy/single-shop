package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.items.exception.ItemsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemsRepositoryTest {

    @Autowired
    ItemsRepository itemsRepository;

    @BeforeEach
    void setUp() {
        String name1 = "상품1";
        Integer price1 = 1;
        String description1 = "설명1";
        Integer quantity1 = 1;

        itemsRepository.save(Items.builder()
                .name(name1)
                .price(price1)
                .description(description1)
                .quantity(quantity1)
                .build());
    }

    @AfterEach
    void cleanup() {
        itemsRepository.deleteAll();
    }

    @Test
    void 상품저장테스트() {
        String name2 = "상품2";
        Integer price2 = 2;
        String description2 = "설명2";
        Integer quantity2 = 2;

        itemsRepository.save(Items.builder()
                .name(name2)
                .price(price2)
                .description(description2)
                .quantity(quantity2)
                .build());

        List<Items> itemsList = itemsRepository.findAll();
        Items items = itemsList.get(1);

        assertThat(items.getName()).isEqualTo(name2);
        assertThat(items.getPrice()).isEqualTo(price2);
        assertThat(items.getDescription()).isEqualTo(description2);
        assertThat(items.getQuantity()).isEqualTo(quantity2);
    }

    @Test
    void 상품조회테스트() {
        String name1 = "상품1";
        Integer price1 = 1;
        String description1 = "설명1";
        Integer quantity1 = 1;

        List<Items> itemsList = itemsRepository.findAll();
        Items items = itemsList.get(0);

        assertThat(items.getName()).isEqualTo(name1);
        assertThat(items.getPrice()).isEqualTo(price1);
        assertThat(items.getDescription()).isEqualTo(description1);
        assertThat(items.getQuantity()).isEqualTo(quantity1);
    }

    @Test
    void 상품조회리스트테스트() {
        String name1 = "상품1";
        Integer price1 = 1;
        String description1 = "설명1";
        Integer quantity1 = 1;

        String name2 = "상품2";
        Integer price2 = 2;
        String description2 = "설명2";
        Integer quantity2 = 2;

        itemsRepository.save(Items.builder()
                .name(name2)
                .price(price2)
                .description(description2)
                .quantity(quantity2)
                .build());

        List<Items> itemsList = itemsRepository.findAll();
        Items items1 = itemsList.get(0);
        Items items2 = itemsList.get(1);

        assertThat(items1.getName()).isEqualTo(name1);
        assertThat(items1.getPrice()).isEqualTo(price1);
        assertThat(items1.getDescription()).isEqualTo(description1);
        assertThat(items1.getQuantity()).isEqualTo(quantity1);

        assertThat(items2.getName()).isEqualTo(name2);
        assertThat(items2.getPrice()).isEqualTo(price2);
        assertThat(items2.getDescription()).isEqualTo(description2);
        assertThat(items2.getQuantity()).isEqualTo(quantity2);
    }

    @Test
    void 상품수정테스트() {
        List<Items> itemsList = itemsRepository.findAll();
        Items items = itemsList.get(0);

        String updateName = "상품";
        Integer updatePrice = 5000;
        String updateDescription = "설명";
        Integer updateQuantity = 5;
        LocalDateTime now = LocalDateTime.now();

        items.update(items.getId(), updateName, updatePrice, updateDescription, updateQuantity, now);

        assertThat(items.getName()).isEqualTo(updateName);
        assertThat(items.getPrice()).isEqualTo(updatePrice);
        assertThat(items.getDescription()).isEqualTo(updateDescription);
        assertThat(items.getQuantity()).isEqualTo(updateQuantity);
        assertThat(items.getModifiedDate()).isEqualTo(now);
    }

    @Test
    void 상품삭제테스트() {
        List<Items> itemsList = itemsRepository.findAll();
        Items items = itemsList.get(0);

        itemsRepository.deleteById(items.getId());

        assertThat(itemsRepository.findById(items.getId())).isNotPresent();
    }

    @Test
    void 상품_생성_날짜_테스트() {
        Items foundItem = itemsRepository.findById(1L).orElseThrow(() -> new ItemsException(BAD_REQUEST_ITEMS_READ));

        assertThat(foundItem.getCreatedDate()).isNotNull();
        assertThat(foundItem.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}