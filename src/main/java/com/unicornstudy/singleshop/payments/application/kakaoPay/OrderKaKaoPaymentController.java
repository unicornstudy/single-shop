package com.unicornstudy.singleshop.payments.application.kakaoPay;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.payments.domain.Payment;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/kakaopay")
@Slf4j
public class OrderKaKaoPaymentController {

    private final KaKaoPaymentService paymentsService;
    private final CartService cartService;
    private final OrderService orderService;
    private final KaKaoConfiguration kaKaoConfiguration;

    @GetMapping
    public ResponseEntity requestReady(@LoginUser SessionUser user, HttpSession httpSession) throws URISyntaxException {
        List<ReadCartResponseDto> carts = cartService.findAllCartItemListByUser(user.getEmail());

        KaKaoReadyRequestDto readyRequestDto = KaKaoReadyRequestDto.createKaKaoReadyRequestDtoForOrder(user.getEmail(), carts, user.getRole(),
                kaKaoConfiguration.getCid(), kaKaoConfiguration.getApproval_url(), kaKaoConfiguration.getCancel_url(), kaKaoConfiguration.getFail_url());
        KaKaoReadyResponseDto readyResponse = paymentsService.requestKaKaoToReady(readyRequestDto).block();

        httpSession.setAttribute("tid", readyResponse.getTid());
        httpSession.setAttribute("readyRequestDto", readyRequestDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(readyResponse.getNext_redirect_pc_url()));

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .headers(httpHeaders)
                .build();
    }

    @GetMapping("/approve")
    public ResponseEntity<KaKaoApproveResponseDto> requestApprove(@LoginUser SessionUser user, HttpSession httpSession,
                                                                  @RequestParam("pg_token") String pg_token) {
        String tid = (String) httpSession.getAttribute("tid");
        KaKaoReadyRequestDto readyRequestDto = (KaKaoReadyRequestDto) httpSession.getAttribute("readyRequestDto");
        KaKaoApproveRequestDto approveRequestDto = KaKaoApproveRequestDto.createApproveRequestDto(tid, pg_token, readyRequestDto);
        Long orderId = orderService.order(user.getEmail(), Payment.builder().tid(tid).paymentKind("kakao").price(readyRequestDto.getBody().get("total_amount").get(0)).build());
        KaKaoApproveResponseDto responseDto = paymentsService.requestKaKaoToApprove(approveRequestDto, orderId).block();
        httpSession.removeAttribute("tid");
        httpSession.removeAttribute("readyRequestDto");

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<KaKaoCancelResponseDto> requestCancel(@PathVariable("id") Long id) {
        KaKaoCancelRequestDto cancelRequestDto = KaKaoCancelRequestDto.createKaKaoCancelRequestDtoForOrder(orderService.cancel(id), kaKaoConfiguration.getCid());
        KaKaoCancelResponseDto responseDto = paymentsService.requestKaKaoToCancel(cancelRequestDto).block();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}