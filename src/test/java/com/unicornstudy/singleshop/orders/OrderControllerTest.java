package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.domain.Cart;
import com.unicornstudy.singleshop.carts.domain.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.delivery.domain.Delivery;
import com.unicornstudy.singleshop.exception.orders.OrderNotFoundException;
import com.unicornstudy.singleshop.items.domain.Items;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.orders.application.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.application.dto.OrderDto;
import com.unicornstudy.singleshop.orders.domain.OrderItem;
import com.unicornstudy.singleshop.orders.domain.Orders;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.orders.presentation.OrderController;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
public class OrderControllerTest {

    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    private User user;
    private Items item;
    private Cart cart;
    private Orders order;
    private OrderItem orderItem;
    private List<Orders> orders = new ArrayList<>();
    private CartItem cartItem;
    private Pageable pageable;
    private Payment payment;
    private Delivery delivery;
    private Address address;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        item = TestSetting.setItem();
        payment = TestSetting.setPayment();
        address = TestSetting.setAddress();
        pageable = TestSetting.setPageable();
        delivery = TestSetting.setDelivery(address);
        cartItem = TestSetting.setCartItem(item);
        orderItem = TestSetting.setOrderItem(item);
        user = TestSetting.setUser(address);
        order = TestSetting.setOrder(user, orderItem, payment, delivery);
        cart = TestSetting.setCart(user, cartItem);
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    @DisplayName("전체 주문 조회 테스트")
    public void 주문_조회_테스트() throws Exception {
        List<OrderDto> result = new ArrayList<>();
        result.add(OrderDto.from(order));

        when(orderService.findOrdersByUser(user.getEmail(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/orders").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(result.size())))
                .andDo(print())
                .andDo(document("order/read/successful",
                        preprocessRequest(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].orderDate").description("Date of the order"),
                                fieldWithPath("[].status").description("Name of the order"),
                                fieldWithPath("[].payment.tid").description("Payment tid of the order"),
                                fieldWithPath("[].payment.paymentKind").description("PaymentKind of the order"),
                                fieldWithPath("[].payment.sid").description("Payment sid of the order"),
                                fieldWithPath("[].payment.price").description("Price of the order")
                        )));
    }

    @Test
    @DisplayName("주문 상세 조회 테스트")
    public void 주문상세_조회_테스트() throws Exception {
        List<OrderDetailDto> result = new ArrayList<>();
        order.getOrderItems()
                .stream()
                .forEach(orderItem -> result.add(OrderDetailDto.from(orderItem)));

        when(orderService.findOrderDetailsById(order.getId(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/orders/{id}", order.getId()).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(result.size())))
                .andDo(print())
                .andDo(document("order/read/detail/successful",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                          parameterWithName("id").description("Id of the order")
                        ),
                        responseFields(
                                fieldWithPath("[].name").description("Date of the item"),
                                fieldWithPath("[].description").description("Name of the item"),
                                fieldWithPath("[].price").description("Payment tid of the item")
                        )));

    }

    @Test
    @DisplayName("재주문 테스트")
    public void 재주문_테스트() throws Exception {
        mvc
                .perform(post("/api/orders/{id}", order.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("order/create/reorder/successful",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of the order")
                        )));
    }

    @Test
    @DisplayName("재주문 실패 테스트")
    public void 재주문_실패_테스트() throws Exception {
        doThrow(new OrderNotFoundException()).when(orderService).reOrder(any(String.class), any(Long.class));

        mvc
                .perform(post("/api/orders/{id}", order.getId()).session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order/create/reorder/failure",
                        preprocessRequest(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of the order")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }
}