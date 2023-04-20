package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.exception.ErrorCode;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ItemsRepositoryTest {

    @Autowired
    private ItemsRepository itemsRepository;

    private Items item;

    @BeforeEach
    void setUp() {
        item = TestSetting.setItem();

        itemsRepository.save(item);
    }

    @AfterEach
    void delete() {
        itemsRepository.deleteAll();
    }

    @DisplayName("상품 저장 테스트")
    @Test
    void save_item_test() {
        Items item2 = TestSetting.setItem();

        itemsRepository.save(item2);

        Items foundItem = itemsRepository.findById(item2.getId())
                .orElseThrow(() -> new ItemsException(ErrorCode.BAD_REQUEST_ITEMS_READ));

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(item2.getId());
        assertThat(foundItem.getName()).isEqualTo(item2.getName());
    }

    @DisplayName("상품 저장 시 생성 날짜 주입 테스트")
    @Test
    void save_item_withCreatedDate_test() {
        Items item2 = TestSetting.setItem();

        itemsRepository.save(item2);

        Items foundItem = itemsRepository.findById(item2.getId())
                .orElseThrow(() -> new ItemsException(ErrorCode.BAD_REQUEST_ITEMS_READ));

        assertThat(foundItem.getCreatedDate()).isNotNull();
        assertThat(foundItem.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @DisplayName("Id를 통한 상품 조회 테스트")
    @Test
    void findById_item_test() {
        Items foundItem = itemsRepository.findById(item.getId())
                .orElseThrow(() -> new ItemsException(ErrorCode.BAD_REQUEST_ITEMS_READ));

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(item.getId());
        assertThat(foundItem.getName()).isEqualTo(item.getName());
    }

    @DisplayName("전체 상품 조회 테스트")
    @Test
    void findAll_test() {
        Items item2 = TestSetting.setItem();

        itemsRepository.save(item2);

        List<Items> items = itemsRepository.findAll();
        Items foundItem = items.get(0);
        Items foundItem2 = items.get(1);

        assertThat(items.size()).isEqualTo(2);
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(item.getId());

        assertThat(foundItem2).isNotNull();
        assertThat(foundItem2.getId()).isEqualTo(item2.getId());
    }

    @DisplayName("상품 데이터 수정 테스트")
    @Test
    void update_item_test() {
        Items foundItem = itemsRepository.findById(item.getId())
                .orElseThrow(() -> new ItemsException(ErrorCode.BAD_REQUEST_ITEMS_READ));

        String updatedName = "Test2";
        Integer updatedPrice = 3000;
        String updatedDescription = "Test2";
        Integer updatedQuantity = 30;

        foundItem.update(foundItem.getId(), updatedName,
                updatedPrice, updatedDescription, updatedQuantity, LocalDateTime.now());

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(foundItem.getId());
        assertThat(foundItem.getName()).isEqualTo(updatedName);
        assertThat(foundItem.getPrice()).isEqualTo(updatedPrice);
        assertThat(foundItem.getDescription()).isEqualTo(updatedDescription);
        assertThat(foundItem.getQuantity()).isEqualTo(updatedQuantity);
        assertThat(foundItem.getModifiedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @DisplayName("상품 데이터 삭제 테스트")
    @Test
    void delete_item_test() {
        Items foundItem = itemsRepository.findById(item.getId())
                .orElseThrow(() -> new ItemsException(ErrorCode.BAD_REQUEST_ITEMS_READ));

        itemsRepository.delete(foundItem);

        assertThat(itemsRepository.findById(item.getId())).isNotPresent();
    }
}