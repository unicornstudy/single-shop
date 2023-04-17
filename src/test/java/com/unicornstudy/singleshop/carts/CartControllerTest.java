package com.unicornstudy.singleshop.carts;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.carts.domain.repository.CartItemRepository;
import com.unicornstudy.singleshop.carts.domain.repository.CartRepository;
import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.carts.presentation.CartController;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.items.domain.repository.ItemsRepository;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.user.domain.Role;
import com.unicornstudy.singleshop.user.domain.User;
import com.unicornstudy.singleshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(CartController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
public class CartControllerTest {

    private MockMvc mvc;

    @MockBean
    private CartService cartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemsRepository itemsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private User user;
    private String name = "test";
    private String email = "testEmail";
    private String picture = "testPicture";
    private Integer price = 10;
    private String description = "description";
    private Integer quantity = 10;

    private Items item;
    private CartItem cartItem;
    private Cart cart;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        setMvc(restDocumentation);
        setUser();
        setItem();
        setCart();
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    public void 장바구니_조회_테스트() throws Exception {
        ReadCartResponseDto responseDto = ReadCartResponseDto.from(cartItem);
        List<ReadCartResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);

        when(cartService.findCartItemListByUser(any(String.class), any(Pageable.class))).thenReturn(responseDtoList);

        mvc
                .perform(RestDocumentationRequestBuilders.get("/api/carts").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andDo(document("장바구니_조회기능",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].cartItemId").description("상품 id"),
                                fieldWithPath("[].itemName").description("상품 이름"),
                                fieldWithPath("[].price").description("상품 가격"),
                                fieldWithPath("[].description").description("상품 설명")
                        )));
    }

    @Test
    public void 장바구니_조회_페이징_테스트() throws Exception {
        ReadCartResponseDto responseDto = ReadCartResponseDto.from(cartItem);
        List<ReadCartResponseDto> responseDtoList = new ArrayList<>();
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
    public void 장바구니_추가_테스트() throws Exception {
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(itemsRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item));
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(cart));

        mvc
                .perform(RestDocumentationRequestBuilders.post("/api/carts/" + item.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("장바구니_추가기능",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("itemId")
                        )));
    }

    @Test
    public void 장바구니_삭제_테스트() throws Exception {
        when(cartRepository.findCartByUserEmail(any(String.class))).thenReturn(Optional.ofNullable(cart));
        when(cartItemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(cartItem));

        mvc
                .perform(RestDocumentationRequestBuilders.delete("/api/carts/" + cartItem.getId()).session(session)
                        .with(csrf())
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("장바구니_삭제기능"));
    }

    private void setMvc(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    private void setUser() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }

    private void setItem() {
        item = Items.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .build();
    }

    private void setCart() {
        cartItem = CartItem.createCartItem(item);
        cartItem.createIdForTest(1L);
        cart = Cart.createCart(user, cartItem);
    }
}