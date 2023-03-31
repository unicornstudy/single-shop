package com.unicornstudy.singleshop.subscription;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import com.unicornstudy.singleshop.subscription.application.dto.SubscriptionDto;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import com.unicornstudy.singleshop.subscription.presentation.SubscriptionController;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@WebMvcTest(SubscriptionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(roles = "USER")
public class SubscriptionControllerTest {

    private MockMvc mvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private User user;
    private Subscription subscription;
    private Payment payment;
    private Pageable pageable;

    @BeforeEach
    public void setUp() {
        setMvc();
        user = TestSetting.setUser(TestSetting.setAddress());
        pageable = TestSetting.setPageable();
        payment = TestSetting.setPayment();
        subscription = TestSetting.setSubscription(user, payment);
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    public void 조회_테스트() throws Exception {
        List<SubscriptionDto> result = new ArrayList<>();
        result.add(SubscriptionDto.from(subscription));

        when(subscriptionService.findSubscriptionByUser(user.getEmail(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/subscription").session(session))
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