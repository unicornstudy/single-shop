package com.unicornstudy.singleshop.orders;

import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.dto.OrderDto;
import com.unicornstudy.singleshop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = "USER")
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
    public void setUp() {
        setMvc();
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
    public void 주문_조회_테스트() throws Exception {
        List<OrderDto> result = new ArrayList<>();
        result.add(OrderDto.createOrderDto(order));

        when(orderService.findOrdersByUser(user.getEmail(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/orders").session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(result.size())));
    }

    @Test
    public void 주문상세_조회_테스트() throws Exception {
        List<OrderDetailDto> result = new ArrayList<>();
        order.getOrderItems()
                .stream()
                .forEach(orderItem -> result.add(OrderDetailDto.createOrderDetailDto(orderItem)));

        when(orderService.findOrderDetailsById(order.getId(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/orders/" + order.getId()).session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(result.size())));
    }

    private void setMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
}