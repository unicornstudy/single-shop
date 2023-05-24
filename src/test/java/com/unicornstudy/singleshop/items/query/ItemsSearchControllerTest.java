package com.unicornstudy.singleshop.items.query;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.query.application.ItemsSearchService;
import com.unicornstudy.singleshop.items.query.application.dto.ItemsSearchDto;
import com.unicornstudy.singleshop.items.query.domain.ItemsIndex;
import com.unicornstudy.singleshop.items.query.presentation.ItemsSearchController;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

@WebMvcTest(ItemsSearchController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
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
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        user = TestSetting.setUser(TestSetting.setAddress());
        items = TestSetting.setItem();
        pageable = TestSetting.setPageable();
        expected.add(ItemsSearchDto.from(ItemsIndex.createElasticSearchItems(items)));
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    @DisplayName("전체 상품 조회 테스트")
    void 전체_상품_조회_테스트() throws Exception {
        when(itemsSearchService.findAll(any(Pageable.class))).thenReturn(expected);

        mockMvc
                .perform(get("/api/items").session(session)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(expected.size())))
                .andDo(print())
                .andDo(document("items/read/successful/all",
                        preprocessRequest(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("ID of the item"),
                                fieldWithPath("[].name").description("Name of the item"),
                                fieldWithPath("[].description").description("Description of the item"),
                                fieldWithPath("[].price").description("Price of the item"),
                                fieldWithPath("[].quantity").description("Quantity of the item"),
                                fieldWithPath("[].modifiedDate").description("ModifiedDate of the item"),
                                fieldWithPath("[].createdDate").description("CreatedDate of the item"),
                                fieldWithPath("[].parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("[].childCategory").description("ChildCategory of the item")
                        )));

    }

    @Test
    @DisplayName("상품 명, 내용, 가격 조회 테스트")
    void 복합_쿼리_테스트() throws Exception {
        when(itemsSearchService.searchItemsByNamePriceAndCategory(any(String.class), any(Integer.class),
                any(Integer.class), any(String.class), any(String.class), any(Pageable.class))).thenReturn(expected);

        mockMvc
                .perform(get("/api/items/{name}", items.getName()).session(session)
                        .characterEncoding("utf-8")
                        .param("minPrice", items.getPrice().toString())
                        .param("maxPrice", items.getPrice().toString())
                        .param("parentCategory", items.getParentCategory().name())
                        .param("childCategory", items.getChildCategory().name()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(expected.size())))
                .andDo(document("items/read/successful",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("name").description("The name of the items to search")
                        ),
                        queryParameters(
                                parameterWithName("minPrice").optional().description("Minimum price to search (default: 0)"),
                                parameterWithName("maxPrice").optional().description("Maximum price to search (default: 2147483647)"),
                                parameterWithName("parentCategory").optional().description("Parent category to search (default: '')"),
                                parameterWithName("childCategory").optional().description("Child category to search (default: '')")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("ID of the item"),
                                fieldWithPath("[].name").description("Name of the item"),
                                fieldWithPath("[].description").description("Description of the item"),
                                fieldWithPath("[].price").description("Price of the item"),
                                fieldWithPath("[].quantity").description("Quantity of the item"),
                                fieldWithPath("[].modifiedDate").description("ModifiedDate of the item"),
                                fieldWithPath("[].createdDate").description("CreatedDate of the item"),
                                fieldWithPath("[].parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("[].childCategory").description("ChildCategory of the item")
                        )));
    }
}
