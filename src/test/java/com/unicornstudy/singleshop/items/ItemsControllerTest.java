package com.unicornstudy.singleshop.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.items.dto.ItemsRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
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

        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saveDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.price").value(price2))
                .andExpect(jsonPath("$.description").value(description2))
                .andExpect(jsonPath("$.quantity").value(quantity2));
    }

    @Test
    void 상품조회테스트() throws Exception {

        Long id = itemsService.findAll().get(0).getId();

        mockMvc.perform(get("/api/items/{id}", id))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("상품1"))
                .andExpect(jsonPath("$.price").value(1))
                .andExpect(jsonPath("$.description").value("설명1"))
                .andExpect(jsonPath("$.quantity").value(1));
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

        mockMvc.perform(get("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemsService.findAll())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("상품1"))
                .andExpect(jsonPath("$.[0].price").value(1))
                .andExpect(jsonPath("$.[0].description").value("설명1"))
                .andExpect(jsonPath("$.[0].quantity").value(1))
                .andExpect(jsonPath("$.[1].name").value("상품2"))
                .andExpect(jsonPath("$.[1].price").value(2))
                .andExpect(jsonPath("$.[1].description").value("설명2"))
                .andExpect(jsonPath("$.[1].quantity").value(2));

    }

    @Test
    void 상품수정테스트() throws Exception {

        Long id = itemsService.findAll().get(0).getId();
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

        mockMvc.perform(put("/api/items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.price").value(price2))
                .andExpect(jsonPath("$.description").value(description2))
                .andExpect(jsonPath("$.quantity").value(quantity2));
    }

    @Test
    void 상품삭제테스트() throws Exception {

        Long id = itemsRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/api/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(itemsRepository.existsById(id)).isFalse();
    }

}