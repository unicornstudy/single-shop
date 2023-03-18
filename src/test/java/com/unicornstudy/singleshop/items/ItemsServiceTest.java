package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.items.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.exception.ItemsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ItemsServiceTest {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsRepository itemsRepository;

    @BeforeEach
    void setUp() {
        String name1 = "상품1";
        Integer price1 = 1;
        String description1 = "설명1";
        Integer quantity1 = 1;

        itemsService.save(ItemsRequestDto.builder()
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
    void 상품조회예외테스트() {
        Long id = itemsRepository.findAll().get(0).getId() + 1L;

        assertThatThrownBy(() -> itemsService.findById(id))
                .isInstanceOf(ItemsException.class);
    }

    @Test
    void 상품수정예외테스트() {
        Long id = itemsRepository.findAll().get(0).getId() + 1L;
        String updateName = "상품";
        Integer updatePrice = 5000;
        String updateDescription = "설명";
        Integer updateQuantity = 5;

        ItemsRequestDto itemUpdateDto = ItemsRequestDto.builder()
                .name(updateName)
                .price(updatePrice)
                .description(updateDescription)
                .quantity(updateQuantity)
                .build();

        assertThatThrownBy(() -> itemsService.update(id, itemUpdateDto))
                .isInstanceOf(ItemsException.class);
    }

    @Test
    void 상품삭제예외테스트() {
        Long id = itemsRepository.findAll().get(0).getId() + 1L;

        assertThatThrownBy(() -> itemsService.delete(id))
                .isInstanceOf(ItemsException.class);
    }

    @Test
    void 상품_증가_예외() {
        Long id = itemsRepository.findAll().get(0).getId() + 1L;

        assertThatThrownBy(() -> itemsService.addQuantity(id))
                .isInstanceOf(ItemsException.class);
    }

    @Test
    void 상품_감소_예외() {
        Long id = itemsRepository.findAll().get(0).getId() + 1L;

        assertThatThrownBy(() -> itemsService.subtractQuantity(id))
                .isInstanceOf(ItemsException.class);
    }
}