package com.unicornstudy.singleshop.items.query;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.query.application.ItemsSearchService;
import com.unicornstudy.singleshop.items.query.application.dto.ItemsSearchDto;
import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import com.unicornstudy.singleshop.items.query.presentation.ItemsSearchController;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemsSearchController.class)
@WithMockUser(roles = "USER")
@Disabled
public class ItemsSearchControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ItemsSearchService itemsSearchService;

    @MockBean
    private OrderService orderService;

    private MockHttpSession session = new MockHttpSession();

    private User user;

    private Items items;

    private Pageable pageable;

    private List<ItemsSearchDto> expected = new ArrayList<>();


    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        user = TestSetting.setUser(TestSetting.setAddress());
        items = TestSetting.setItem();
        pageable = TestSetting.setPageable();
        expected.add(ItemsSearchDto.from(ItemsIndex.createElasticSearchItems(items)));
    }

    @Test
    @DisplayName("전체 상품 조회 테스트")
    void 전체_상품_조회_테스트() throws Exception {
        when(itemsSearchService.findAll(any(Pageable.class))).thenReturn(expected);

        mockMvc
                .perform(get("/api/items").session(session)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(expected.size())));

    }

    @Test
    @DisplayName("상품 명, 내용, 가격 조회 테스트")
    void 복합_쿼리_테스트() throws Exception {
        when(itemsSearchService.searchItemsByNamePriceAndCategory(any(String.class), any(Integer.class),
                any(Integer.class), any(String.class), any(String.class), any(Pageable.class))).thenReturn(expected);

        mockMvc
                .perform(get("/api/items/test").session(session)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(expected.size())));
    }
}
