package com.unicornstudy.singleshop.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.carts.Cart;
import com.unicornstudy.singleshop.carts.CartItem;
import com.unicornstudy.singleshop.delivery.Address;
import com.unicornstudy.singleshop.delivery.Delivery;
import com.unicornstudy.singleshop.items.Items;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.dto.OrderDetailDto;
import com.unicornstudy.singleshop.orders.dto.OrderDto;
import com.unicornstudy.singleshop.user.Role;
import com.unicornstudy.singleshop.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final String name = "test";
    private final String email = "testEmail";
    private final String picture = "testPicture";
    private final Integer price = 10;
    private final String description = "description";
    private final Integer quantity = 1;

    private User user;
    private Items item;
    private Cart cart;
    private Orders order;
    private OrderItem orderItem;
    private List<Cart> cartList = new ArrayList<>();
    private List<OrderItem> orderItems = new ArrayList<>();
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
        setItem();
        setPayment();
        setAddress();
        setPageable();
        setDelivery();
        setUser();
        setOrder();
        setCart();
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
    private void setItem() {
        item = Items.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .quantity(quantity)
                .build();
    }

    private void setUser() {
        user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .address(address)
                .orders(orders)
                .cart(cart)
                .build();
    }

    private void setCart() {
        cartItem = CartItem.createCartItem(item);
        cart = Cart.createCart(user, cartItem);
        cartList.add(cart);
    }

    private void setPageable() {
        pageable = PageRequest.of(0, 10, Sort.by("id").descending());
    }

    private void setAddress() {
        address = new Address("testCity", "testStreet", "testZipcode");
    }

    private void setDelivery() {
        delivery = Delivery.createDelivery(address);
    }

    private void setOrder() {
        orderItem = OrderItem.createOrderItem(item);
        orderItems.add(orderItem);
        order = Orders.createOrder(user, orderItems, payment, delivery);
        order.createIdForTest(1L);
    }

    private void setMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private void setPayment() {
         payment = Payment.builder().paymentKind("test").tid("test").build();
    }
}