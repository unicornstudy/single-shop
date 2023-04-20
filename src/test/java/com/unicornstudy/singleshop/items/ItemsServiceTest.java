package com.unicornstudy.singleshop.items;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.application.ItemsService;
import com.unicornstudy.singleshop.items.application.dto.ItemsReadUpdateResponseDto;
import com.unicornstudy.singleshop.items.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.application.dto.ItemsSaveResponseDto;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemsServiceTest {

    @InjectMocks
    private ItemsService itemsService;

    @Mock
    private ItemsRepository itemsRepository;

    private Items item;

    @BeforeEach
    void setUp() {
        item = TestSetting.setItem();
    }

    @DisplayName("상품 데이터 저장 테스트")
    @Test
    void save_item_test() {
        ItemsRequestDto requestDto = new ItemsRequestDto(
                item.getName(), item.getPrice(), item.getDescription(), item.getQuantity());

        given(itemsRepository.save(any())).willReturn(item);

        ItemsSaveResponseDto newItem = itemsService.save(requestDto);

        assertThat(newItem).isNotNull();
        assertThat(newItem.getId()).isEqualTo(item.getId());
    }

    @DisplayName("상품 데이터 삭제 테스트")
    @Test
    void delete_item_test() {
        given(itemsRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));

        itemsService.delete(item.getId());

        verify(itemsRepository).delete(any());
    }

    @DisplayName("상품 데이터 삭제 예외 테스트")
    @Test
    void delete_item_exception_test() {
        given(itemsRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> itemsService.delete(2L))
                .isInstanceOf(ItemsException.class);
    }

    @DisplayName("상품 데이터 수정 테스트")
    @Test
    void update_item_test() {
        ItemsRequestDto updateRequestDto = new ItemsRequestDto(
                "updated name", 2000, "updated description", 20);

        given(itemsRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));

        itemsService.update(item.getId(), updateRequestDto);

        assertThat(item.getName()).isEqualTo(updateRequestDto.getName());
        assertThat(item.getPrice()).isEqualTo(updateRequestDto.getPrice());
        assertThat(item.getDescription()).isEqualTo(updateRequestDto.getDescription());
        assertThat(item.getQuantity()).isEqualTo(updateRequestDto.getQuantity());
    }

    @DisplayName("상품 데이터 수정 예외 테스트")
    @Test
    void update_item_exception_test() {
        ItemsRequestDto updateRequestDto = new ItemsRequestDto(
                "updated name", 2000, "updated description", 20);

        given(itemsRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> itemsService.update(2L, updateRequestDto))
                .isInstanceOf(ItemsException.class);
    }

    @DisplayName("Id를 통한 상품 조회 테스트")
    @Test
    void findById_item_test() {
        given(itemsRepository.findById(anyLong())).willReturn(Optional.ofNullable(item));

        ItemsReadUpdateResponseDto foundItem = itemsService.findById(item.getId());

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(item.getId());
    }

    @DisplayName("Id를 통한 상품 조회 예외 테스트")
    @Test
    void findById_item_exception_test() {
        given(itemsRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> itemsService.findById(2L))
                .isInstanceOf(ItemsException.class);
    }

    @DisplayName("전체 상품 조회 테스트")
    @Test
    void findAll_test() {
        Items item2 = TestSetting.setItem();
        List<Items> foundList = new ArrayList<>();

        foundList.add(item);
        foundList.add(item2);

        given(itemsRepository.findAll()).willReturn(foundList);

        List<ItemsReadUpdateResponseDto> responseDtos = itemsService.findAll();

        assertThat(responseDtos.size()).isEqualTo(foundList.size());
    }
}