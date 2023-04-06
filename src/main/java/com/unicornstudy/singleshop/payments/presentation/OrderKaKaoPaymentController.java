package com.unicornstudy.singleshop.payments.presentation;

import com.unicornstudy.singleshop.carts.application.CartService;
import com.unicornstudy.singleshop.carts.application.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.oauth2.LoginUser;
import com.unicornstudy.singleshop.oauth2.dto.SessionUser;
import com.unicornstudy.singleshop.orders.application.OrderService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.KaKaoPaymentService;
import com.unicornstudy.singleshop.payments.application.kakaoPay.dto.*;
import com.unicornstudy.singleshop.payments.domain.Payment;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.unicornstudy.singleshop.payments.application.kakaoPay.utils.CartToPaymentItemConverter.convertItemName;
import static com.unicornstudy.singleshop.payments.application.kakaoPay.utils.CartToPaymentItemConverter.convertTotalAmount;

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
    public ResponseEntity requestReady(@LoginUser SessionUser user, HttpSession httpSession) {
        List<ReadCartResponseDto> carts = cartService.findAllCartItemListByUser(user.getEmail());

        KaKaoReadyRequestDto readyRequestDto = KaKaoReadyRequestDto.of(kaKaoConfiguration.getCid(), kaKaoConfiguration.getApproval_url(), kaKaoConfiguration.getCancel_url(), kaKaoConfiguration.getFail_url(), "주문 결제", user.getEmail(), convertItemName(carts), String.valueOf(carts.size()), convertTotalAmount(carts, user.getRole()), "0");
        KaKaoReadyResponseDto readyResponse = paymentsService.requestKaKaoToReady(readyRequestDto).block();

        httpSession.setAttribute("tid", readyResponse.getTid());
        httpSession.setAttribute("readyRequestDto", readyRequestDto);

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(readyResponse.getNext_redirect_pc_url()))
                .build();
    }

    @GetMapping("/approve")
    public ResponseEntity<KaKaoApproveResponseDto> requestApprove(@LoginUser SessionUser user, HttpSession httpSession,
                                                                  @RequestParam("pg_token") String pg_token) {
        String tid = (String) httpSession.getAttribute("tid");
        KaKaoReadyRequestDto readyRequestDto = (KaKaoReadyRequestDto) httpSession.getAttribute("readyRequestDto");
        KaKaoApproveRequestDto approveRequestDto = KaKaoApproveRequestDto.of(readyRequestDto.getCid(), tid, readyRequestDto.getPartner_order_id(), readyRequestDto.getPartner_user_id(), pg_token );
        Long orderId = orderService.order(user.getEmail(),
                Payment.builder()
                .tid(tid)
                .paymentKind("kakao")
                .price(readyRequestDto.getTotal_amount())
                .build());
        KaKaoApproveResponseDto responseDto = paymentsService.requestKaKaoToApprove(approveRequestDto, orderId).block();
        httpSession.removeAttribute("tid");
        httpSession.removeAttribute("readyRequestDto");

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<KaKaoCancelResponseDto> requestCancel(@PathVariable("id") Long id) {
        Payment paymentInfo = orderService.cancel(id).getPayment();
        KaKaoCancelRequestDto cancelRequestDto = KaKaoCancelRequestDto.of(kaKaoConfiguration.getCid(), paymentInfo.getTid(), paymentInfo.getPrice(), "0");
        KaKaoCancelResponseDto responseDto = paymentsService.requestKaKaoToCancel(cancelRequestDto).block();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}