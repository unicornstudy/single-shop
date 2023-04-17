package com.unicornstudy.singleshop.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.user.application.UserService;
import com.unicornstudy.singleshop.user.application.dto.FindAddressDto;
import com.unicornstudy.singleshop.user.application.dto.UpdateAddressDto;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser(roles = "USER")
class UserControllerTest {

    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session = new MockHttpSession();

    private User user;

    private UpdateAddressDto addressDto;

    private Address address;
    @BeforeEach
    public void setUp() {
        setMvc();
        address = TestSetting.setAddress();
        addressDto = new UpdateAddressDto(address.getCity(), address.getStreet(), address.getZipcode());
        user = TestSetting.setUser(address);
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    public void 주소_등록_테스트() throws Exception {
        mvc
                .perform(put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void 주소_조회_테스트() throws Exception {
        when(userService.findAddressByUser(user.getEmail())).thenReturn(FindAddressDto.from(user.getAddress()));

        mvc
                .perform(get("/api/user").session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.street").value("testStreet"))
                .andExpect(jsonPath("$.city").value("testCity"))
                .andExpect(jsonPath("$.zipcode").value("testZipcode"));
    }

    private void setMvc() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
}