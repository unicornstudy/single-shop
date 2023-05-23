package com.unicornstudy.singleshop.items.query;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.query.application.ItemsSearchService;
import com.unicornstudy.singleshop.items.query.application.dto.ItemsSearchDto;
import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import com.unicornstudy.singleshop.items.query.domain.repository.ItemsSearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemsSearchServiceTest {

    @Mock
    private ItemsSearchRepository itemsSearchRepository;

    private ItemsSearchService itemsSearchService;

    private Pageable pageable;

    private ItemsIndex item;

    private List<ItemsIndex> itemsList;

    private Page<ItemsIndex> expected;

    @BeforeEach
    void setUp() {
        itemsSearchService = new ItemsSearchService(itemsSearchRepository);
        pageable = TestSetting.setPageable();
        item = ItemsIndex.createElasticSearchItems(TestSetting.setItem());
        itemsList = Arrays.asList(item);
        expected = new PageImpl<>(itemsList, pageable, itemsList.size());
    }

    @Test
    @DisplayName("모든 상품 검색 테스트")
    void 모든_상품_검색_테스트() {
        when(itemsSearchRepository.findAll(any(Pageable.class))).thenReturn(expected);

        List<ItemsSearchDto> result = itemsSearchService.findAll(pageable);

        List<ItemsSearchDto> expectedDtoList = expected.stream().map(ItemsSearchDto::from).toList();
        assertThat(result.get(0).getId()).isEqualTo(expectedDtoList.get(0).getId());
    }

    @Test
    @DisplayName("상품 명, 내용, 가격 조회 테스트")
    void 복합_쿼리_테스트() {
        when(itemsSearchRepository.searchItemsByNamePriceAndCategory(item.getName(), item.getPrice(), item.getPrice(), item.getParentCategory().name(), item.getChildCategory().name(), pageable))
                .thenReturn(itemsList);
        List<ItemsSearchDto> result = itemsSearchService.searchItemsByNamePriceAndCategory(item.getName(), item.getPrice(), item.getPrice(), item.getParentCategory().name(), item.getChildCategory().name(), pageable);
        List<ItemsSearchDto> expectedDtoList = expected.stream().map(ItemsSearchDto::from).toList();
        assertThat(result.get(0).getId()).isEqualTo(expectedDtoList.get(0).getId());
    }
}