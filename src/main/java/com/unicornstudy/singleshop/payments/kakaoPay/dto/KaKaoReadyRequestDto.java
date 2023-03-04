package com.unicornstudy.singleshop.payments.kakaoPay.dto;

import com.unicornstudy.singleshop.carts.dto.ReadCartResponseDto;
import com.unicornstudy.singleshop.user.Role;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Getter
public class KaKaoReadyRequestDto {

    private MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

    public static KaKaoReadyRequestDto createKaKaoReadyRequestDtoForOrder(String userId, List<ReadCartResponseDto> cart, Role role, String cid,
                                                                          String approval_url, String cancel_url, String fail_url) {
        KaKaoReadyRequestDto readyRequestDto = new KaKaoReadyRequestDto();
        readyRequestDto.initializeForOrder(userId, cart, role, cid, approval_url, cancel_url, fail_url);

        return readyRequestDto;
    }

    private void initializeForOrder(String userId, List<ReadCartResponseDto> cart, Role role, String cid,
                                   String approval_url, String cancel_url, String fail_url) {
        body.add("cid", cid);
        body.add("approval_url", approval_url);
        body.add("cancel_url", cancel_url);
        body.add("fail_url", fail_url);
        body.add("partner_order_id", "주문 결제");
        body.add("partner_user_id", userId);
        body.add("item_name", cart.size() == 1 ? cart.get(0).getItemName() : cart.get(0).getItemName() + "외" + (cart.size() - 1) + "개의 상품");
        body.add("quantity", String.valueOf(cart.size()));
        body.add("total_amount", String.valueOf(cart.stream().mapToInt(ReadCartResponseDto :: getPrice).sum() + checkSubscriber(role)));
        body.add("tax_free_amount", String.valueOf(0));
    }

    private int checkSubscriber(Role role) {
        return (role == Role.USER) ? 3000 : 0;
    }
}
