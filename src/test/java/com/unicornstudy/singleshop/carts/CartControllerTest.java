package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.application.dto.CartResponseDto;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.presentation.CartController;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.exception.carts.CartItemNotFoundException;
import com.unicornstudy.singleshop.exception.carts.CartNotFoundException;
import com.unicornstudy.singleshop.exception.carts.SessionExpiredException;
import com.unicornstudy.singleshop.exception.items.ItemsException;
import com.unicornstudy.singleshop.items.command.domain.Items;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.command.application.OrderService;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.unicornstudy.singleshop.exception.ErrorCode.BAD_REQUEST_ITEMS_READ;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
public class CartControllerTest {

    private MockMvc mvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private User user;

    private CartItem cartItem;

    private CartResponseDto responseDto;
    private Items items;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        user = TestSetting.setUser(TestSetting.setAddress());

        items = TestSetting.setItem();

        cartItem = TestSetting.setCartItem(items);

        responseDto = CartResponseDto.from(cartItem);

        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    public void 장바구니_조회_테스트() throws Exception {
        List<CartResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);

        when(cartService.findCartItemListByUser(any(String.class), any(Pageable.class))).thenReturn(responseDtoList);

        mvc
                .perform(get("/api/carts").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andDo(print())
                .andDo(document("cart/read/successful",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].cartItemId").description("Id of the item"),
                                fieldWithPath("[].itemName").description("Name of the item"),
                                fieldWithPath("[].price").description("Price of the item"),
                                fieldWithPath("[].description").description("Description of the item"),
                                fieldWithPath("[].quantity").description("Quantity of the item")
                        )));
    }

    @Test
    @DisplayName("장바구니 조회 실패 테스트: 장바구니가 없을 때")
    public void 장바구니_조회_실패_테스트() throws Exception {
        List<CartResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);

        doThrow(new CartNotFoundException()).when(cartService).findCartItemListByUser(any(String.class), any(Pageable.class));

        mvc
                .perform(get("/api/carts").session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andDo(print())
                .andDo(document("cart/read/failure",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }


    @Test
    public void 장바구니_조회_페이징_테스트() throws Exception {
        CartResponseDto responseDto = CartResponseDto.from(cartItem);
        List<CartResponseDto> responseDtoList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < 100; i++) {
            responseDtoList.add(responseDto);
        }
        when(cartService.findCartItemListByUser(any(String.class), any(Pageable.class))).thenReturn(responseDtoList.stream().limit(size).collect(Collectors.toList()));

        mvc
                .perform(get("/api/carts?page=0&size=10&sort=id,DESC").session(session)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(size)));
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    public void 장바구니_추가_테스트() throws Exception {
        when(cartService.addCart(any(String.class), any(Long.class))).thenReturn(responseDto);

        mvc
                .perform(post("/api/carts/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("cart/create/successful",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of the item")
                        ),
                        responseFields(
                                fieldWithPath("cartItemId").description("Id of the item"),
                                fieldWithPath("itemName").description("Name of the item"),
                                fieldWithPath("price").description("Price of the item"),
                                fieldWithPath("description").description("Description of the item"),
                                fieldWithPath("quantity").description("Quantity of the item")
                        )));
    }

    @Test
    @DisplayName("장바구니 추가 실패 테스트: 세션 만료됐을 때")
    public void 장바구니_추가_실패_테스트1() throws Exception {
        doThrow(new SessionExpiredException()).when(cartService).addCart(any(String.class), any(Long.class));

        mvc
                .perform(post("/api/carts/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("cart/create/failure/session",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of the item")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("장바구니 추가 실패 테스트: 상품 id를 잘못 입력했을 때")
    public void 장바구니_추가_실패_테스트2() throws Exception {
        doThrow(new ItemsException(BAD_REQUEST_ITEMS_READ)).when(cartService).addCart(any(String.class), any(Long.class));

        mvc
                .perform(post("/api/carts/{id}", items.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("cart/create/failure/null",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of the item")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Message of the error"),
                                fieldWithPath("code").description("Code of the error")
                        )));
    }

    @Test
    @DisplayName("장바구니 삭제 테스트")
    public void 장바구니_삭제_테스트() throws Exception {

        mvc
                .perform(delete("/api/carts/{id}", cartItem.getId()).session(session)
                        .with(csrf().asHeader())
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(document("cart/delete/successful",
                        pathParameters(
                                parameterWithName("id").description("Id of the CartItem")
                        )
                    ));
    }

    @Test
    @DisplayName("장바구니 삭제 실패 테스트: 장바구니가 존재하지 않을 때")
    public void 장바구니_삭제_실패_테스트1() throws Exception {
        doThrow(new CartNotFoundException()).when(cartService).removeCartItem(any(String.class), any(Long.class));

        mvc
                .perform(delete("/api/carts/{id}", cartItem.getId()).session(session)
                        .with(csrf().asHeader())
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("cart/delete/failure/null",
                        pathParameters(
                                parameterWithName("id").description("Id of the CartItem")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )
                ));
    }

    @Test
    @DisplayName("장바구니 삭제 실패 테스트: 장바구니안에 상품이 존재하지 않을 때")
    public void 장바구니_삭제_실패_테스트2() throws Exception {
        doThrow(new CartItemNotFoundException()).when(cartService).removeCartItem(any(String.class), any(Long.class));

        mvc
                .perform(delete("/api/carts/{id}", cartItem.getId()).session(session)
                        .with(csrf().asHeader())
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("cart/delete/failure/noitems",
                        pathParameters(
                                parameterWithName("id").description("Id of the CartItem")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )
                ));
    }
}