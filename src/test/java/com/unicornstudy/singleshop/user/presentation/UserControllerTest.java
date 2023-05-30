package com.unicornstudy.singleshop.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.command.application.OrderService;
import com.unicornstudy.singleshop.user.application.UserService;
import com.unicornstudy.singleshop.user.application.dto.AddressResponseDto;
import com.unicornstudy.singleshop.user.application.dto.AddressRequestDto;
import com.unicornstudy.singleshop.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser(roles = "USER")
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session = new MockHttpSession();

    private User user;

    private AddressRequestDto addressRequestDto;

    private Address address;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        address = TestSetting.setAddress();

        user = TestSetting.setUser(address);
        session.setAttribute("user", new SessionUser(user));
    }

    @Test
    @DisplayName("주소 등록 또는 변경 테스트")
    public void 주소_등록_또는_변경_테스트() throws Exception {

        when(userService.updateAddress(any(String.class), any(AddressRequestDto.class))).thenReturn(new AddressResponseDto("city", "street", "zipcode"));

        addressRequestDto = AddressRequestDto.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .build();

        mvc
                .perform(put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user/create/address/successful",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("city").description("City of the Address to update"),
                                fieldWithPath("street").description("Street of the Address to update"),
                                fieldWithPath("zipcode").description("Zipcode of the Address to update")
                        ),
                        responseFields(
                                fieldWithPath("city").description("City of the updated Address"),
                                fieldWithPath("street").description("Street tid of the updated Address"),
                                fieldWithPath("zipcode").description("Zipcode of the updated Address")
                        )));
    }

    @Test
    @DisplayName("주소 등록 또는 변경 실패 테스트: city가 비었을 때")
    public void 주소_등록_또는_변경_실패_테스트1() throws Exception {
        addressRequestDto = AddressRequestDto.builder()
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .build();

        mvc
                .perform(put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("user/create/address/failure/city",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("city").description("City of the Address to update"),
                                fieldWithPath("street").description("Street of the Address to update"),
                                fieldWithPath("zipcode").description("Zipcode of the Address to update")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("주소 등록 또는 변경 테스트 실패한 경우: street이 비었을 때")
    public void 주소_등록_또는_변경_실패_테스트2() throws Exception {
        addressRequestDto = AddressRequestDto.builder()
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .build();

        mvc
                .perform(put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("user/create/address/failure/street",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("city").description("City of the Address to update"),
                                fieldWithPath("street").description("Street of the Address to update"),
                                fieldWithPath("zipcode").description("Zipcode of the Address to update")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    @DisplayName("주소 등록 또는 변경 테스트 실패한 경우: zipcode가 5자리가 아닐 때 ")
    public void 주소_등록_또는_변경_실패_테스트3() throws Exception {
        addressRequestDto = AddressRequestDto.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode("123456")
                .build();

        mvc
                .perform(put("/api/user").session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequestDto))
                        .with(csrf().asHeader()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("user/create/address/failure/zipcode",
                        preprocessRequest(prettyPrint()),
                        requestFields(
                                fieldWithPath("city").description("City of the Address to update"),
                                fieldWithPath("street").description("Street of the Address to update"),
                                fieldWithPath("zipcode").description("Zipcode of the Address to update")
                        ),
                        responseFields(
                                fieldWithPath("error").description("Message of the error")
                        )));
    }

    @Test
    public void 주소_조회_테스트() throws Exception {
        addressRequestDto = AddressRequestDto.builder()
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .build();
        when(userService.findAddressByUser(user.getEmail())).thenReturn(AddressResponseDto.from(user.getAddress()));

        mvc
                .perform(get("/api/user").session(session))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.street").value("testStreet"))
                .andExpect(jsonPath("$.city").value("testCity"))
                .andExpect(jsonPath("$.zipcode").value("12345"))
                .andDo(document("user/read/address/successful",
                        preprocessRequest(prettyPrint()),
                        responseFields(
                                fieldWithPath("city").description("City of the updated Address"),
                                fieldWithPath("street").description("Street tid of the updated Address"),
                                fieldWithPath("zipcode").description("Zipcode of the updated Address")
                        )));
    }
}