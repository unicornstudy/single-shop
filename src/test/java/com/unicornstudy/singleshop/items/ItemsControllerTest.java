package com.unicornstudy.singleshop.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.items.command.application.ItemsService;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsResponseDto;
import com.unicornstudy.singleshop.items.command.presentation.ItemsController;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemsController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
public class ItemsControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ItemsService itemsService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private User user;

    private Items items;

    private ItemsRequestDto itemsRequestDto;

    private ItemsResponseDto itemsResponseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        items = TestSetting.setItem();

        itemsResponseDto = ItemsResponseDto.from(items);

        user = TestSetting.setUser(TestSetting.setAddress());
        session.setAttribute("user", new SessionUser(user));
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("상품 저장 테스트")
    void 상품저장테스트() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("items/create/successful",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ID of the created item"),
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("modifiedDate").description("ModifiedDate of the item"),
                                fieldWithPath("createdDate").description("CreatedDate of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: name이 null일 때")
    void 상품저장실패테스트1() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: price가 0보다 작을 때")
    void 상품저장실패테스트2() throws Exception {

        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(-1)
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }
    @Test
    @DisplayName("상품저장실패테스트: price가 null일 때")
    void 상품저장실패테스트3() throws Exception {

        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }
    @Test
    @DisplayName("상품저장실패테스트: 설명이 null일 때")
    void 상품저장실패테스트4() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }
    @Test
    @DisplayName("상품저장실패테스트: 재고가 null일 때")
    void 상품저장실패테스트5() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: 재고가 0보다 작을 때")
    void 상품저장실패테스트6() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(-1)
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: 상위 카테고리가 null 일 때")
    void 상품저장실패테스트7() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: 상위 카테고리가 존재하지 않는 카테고리 일 때")
    void 상품저장실패테스트8() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory("test")
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: 하위 카테고리가 null일 때")
    void 상품저장실패테스트9() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품저장실패테스트: 하위 카테고리가 존재하지 않는 카테고리 일 때")
    void 상품저장실패테스트10() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory("test")
                .build();

        when(itemsService.save(any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(post("/api/items").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/create/failure",
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("상품 변경 테스트")
    void 상품변경테스트() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        when(itemsService.update(any(Long.class), any(ItemsRequestDto.class))).thenReturn(itemsResponseDto);

        mockMvc.
                perform(put("/api/items/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("items/update/successful",
                        pathParameters(
                                parameterWithName("id").description("The id of the items to update")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ID of the created item"),
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("modifiedDate").description("ModifiedDate of the item"),
                                fieldWithPath("createdDate").description("CreatedDate of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        )));
    }

    @Test
    @DisplayName("상품 변경 실패 테스트: 존재하지 않는 상품을 조회했을 때")
    void 상품변경실패테스트() throws Exception {
        itemsRequestDto = ItemsRequestDto.builder()
                .name(items.getName())
                .price(items.getPrice())
                .description(items.getDescription())
                .quantity(items.getQuantity())
                .parentCategory(items.getParentCategory().name())
                .childCategory(items.getChildCategory().name())
                .build();

        doThrow(new ItemsException(BAD_REQUEST_ITEMS_READ)).when(itemsService).update(any(Long.class), any(ItemsRequestDto.class));

        mockMvc.
                perform(put("/api/items/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsRequestDto))
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/update/failure",
                        pathParameters(
                                parameterWithName("id").description("The id of the items to update")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("quantity").description("Quantity of the item"),
                                fieldWithPath("parentCategory").description("ParentCategory of the item"),
                                fieldWithPath("childCategory").description("ChildCategory of the item")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Message of the error"),
                                fieldWithPath("code").description("Code of the error")
                        )));
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void 상품삭제테스트() throws Exception {

        when(itemsService.delete(any(Long.class))).thenReturn(items.getId());

        mockMvc.
                perform(delete("/api/items/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("items/delete/successful",
                        pathParameters(
                                parameterWithName("id").description("The id of the items to update")
                        )));
    }

    @Test
    @DisplayName("상품 삭제 실패 테스트: 존재하지 않는 상품일 때")
    void 상품삭제실패테스트() throws Exception {

        doThrow(new ItemsException(BAD_REQUEST_ITEMS_READ)).when(itemsService).delete(any(Long.class));
        mockMvc.
                perform(delete("/api/items/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("items/delete/failure",
                        pathParameters(
                                parameterWithName("id").description("The id of the items to update")
                        ),responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Message of the error"),
                                fieldWithPath("code").description("Code of the error")
                        )));
    }

}