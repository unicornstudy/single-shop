package com.unicornstudy.singleshop.subscription;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.command.application.OrderService;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.subscription.application.SubscriptionService;
import com.unicornstudy.singleshop.subscription.application.dto.SubscriptionDto;
import com.unicornstudy.singleshop.subscription.domain.Subscription;
import com.unicornstudy.singleshop.subscription.presentation.SubscriptionController;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
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
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        user = TestSetting.setUser(TestSetting.setAddress());
        pageable = TestSetting.setPageable();
        payment = TestSetting.setPayment();
        subscription = TestSetting.setSubscription(user, payment);
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    @DisplayName("현재까지 사용자가 결제한 구독 정보 조회 컨트롤러 테스트")
    public void 조회_테스트() throws Exception {
        List<SubscriptionDto> result = new ArrayList<>();
        result.add(SubscriptionDto.from(subscription));

        when(subscriptionService.findSubscriptionByUser(user.getEmail(), pageable)).thenReturn(result);

        mvc
                .perform(get("/api/subscription").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(result.size())))
                .andDo(print())
                .andDo(document("subscription/read/successful",
                        preprocessRequest(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].payDate").description("PayDate of the subscription"),
                                fieldWithPath("[].payment.tid").description("Payment tid of the subscription"),
                                fieldWithPath("[].payment.paymentKind").description("PaymentKind of the subscription"),
                                fieldWithPath("[].payment.sid").description("Payment sid of the subscription"),
                                fieldWithPath("[].payment.price").description("Price of the subscription")
                        )));

    }

}