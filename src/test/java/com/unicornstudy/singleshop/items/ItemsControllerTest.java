package com.unicornstudy.singleshop.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.items.command.application.ItemsService;
import com.unicornstudy.singleshop.items.command.application.dto.ItemsRequestDto;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ItemsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        String name1 = "상품1";
        Integer price1 = 1;
        String description1 = "설명1";
        Integer quantity1 = 1;
        pageable = TestSetting.setPageable();
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
    void 상품저장테스트() throws Exception {
        String name2 = "상품2";
        Integer price2 = 2;
        String description2 = "설명2";
        Integer quantity2 = 2;

        ItemsRequestDto saveDto = ItemsRequestDto.builder()
                .name(name2)
                .price(price2)
                .description(description2)
                .quantity(quantity2)
                .build();

        itemsService.save(saveDto);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.price").value(price2))
                .andExpect(jsonPath("$.description").value(description2))
                .andExpect(jsonPath("$.quantity").value(quantity2))
                .andDo(document("상품_저장기능",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("저장할 상품 이름"),
                                fieldWithPath("price").description("저장할 상품 가격"),
                                fieldWithPath("description").description("저장할 상품 설명"),
                                fieldWithPath("quantity").description("저장할 상품 재고")
                        ),
                        responseFields(
                                fieldWithPath("id").description("상품 ID"),
                                fieldWithPath("name").description("상품 이름"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("description").description("상품 설명"),
                                fieldWithPath("quantity").description("상품 재고"),
                                fieldWithPath("createdDate").description("상품 저장시간")
                        )));
    }

    @Test
    void 상품조회테스트() throws Exception {

        Long id = itemsService.findAll(pageable).get(0).getId();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/items/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("상품1"))
                .andExpect(jsonPath("$.price").value(1))
                .andExpect(jsonPath("$.description").value("설명1"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andDo(document("상품_조회기능",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("조회할 상품 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("조회할 상품 id"),
                                fieldWithPath("name").description("조회할 상품 이름"),
                                fieldWithPath("price").description("조회할 상품 가격"),
                                fieldWithPath("description").description("조회할 상품 설명"),
                                fieldWithPath("quantity").description("조회할 상품 재고"),
                                fieldWithPath("modifiedDate").description("조회할 상품 게시 시간")
                        )));
    }

    @Test
    void 상품리스트조회테스트() throws Exception {
        String name2 = "상품2";
        Integer price2 = 2;
        String description2 = "설명2";
        Integer quantity2 = 2;

        ItemsRequestDto saveDto = ItemsRequestDto.builder()
                .name(name2)
                .price(price2)
                .description(description2)
                .quantity(quantity2)
                .build();

        itemsService.save(saveDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsService.findAll(pageable))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("상품1"))
                .andExpect(jsonPath("$.[0].price").value(1))
                .andExpect(jsonPath("$.[0].description").value("설명1"))
                .andExpect(jsonPath("$.[0].quantity").value(1))
                .andExpect(jsonPath("$.[1].name").value("상품2"))
                .andExpect(jsonPath("$.[1].price").value(2))
                .andExpect(jsonPath("$.[1].description").value("설명2"))
                .andExpect(jsonPath("$.[1].quantity").value(2))
                .andDo(document("상품리스트_조회기능",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("조회할 상품 id"),
                                fieldWithPath("[].name").description("조회할 상품 이름"),
                                fieldWithPath("[].price").description("조회할 상품 가격"),
                                fieldWithPath("[].description").description("조회할 상품 설명"),
                                fieldWithPath("[].quantity").description("조회할 상품 재고"),
                                fieldWithPath("[].modifiedDate").description("조회할 상품 게시 시간")
                        )));

    }

    @Test
    void 상품수정테스트() throws Exception {

        Long id = itemsService.findAll(pageable).get(0).getId();
        String name2 = "상품2";
        Integer price2 = 2;
        String description2 = "설명2";
        Integer quantity2 = 2;

        ItemsRequestDto updateDto = ItemsRequestDto.builder()
                .name(name2)
                .price(price2)
                .description(description2)
                .quantity(quantity2)
                .build();

        itemsService.update(id, updateDto);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.price").value(price2))
                .andExpect(jsonPath("$.description").value(description2))
                .andExpect(jsonPath("$.quantity").value(quantity2))
                .andDo(document("상품_수정기능",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("수정할 상품 id")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 상품 이름"),
                                fieldWithPath("price").description("수정할 상품 가격"),
                                fieldWithPath("description").description("수정할 상품 설명"),
                                fieldWithPath("quantity").description("수정할 상품 재고")
                        ),
                        responseFields(
                                fieldWithPath("id").description("수정할 상품 id"),
                                fieldWithPath("name").description("수정할 상품 이름"),
                                fieldWithPath("price").description("수정할 상품 가격"),
                                fieldWithPath("description").description("수정할 상품 설명"),
                                fieldWithPath("quantity").description("수정할 상품 재고"),
                                fieldWithPath("modifiedDate").description("수정할 상품 게시 시간")
                        )));
    }

    @Test
    void 상품삭제테스트() throws Exception {

        Long id = itemsRepository.findAll().get(0).getId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(itemsRepository.existsById(id)).isFalse();
    }

}