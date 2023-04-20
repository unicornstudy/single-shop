package com.unicornstudy.singleshop.user.domain;

import com.unicornstudy.singleshop.config.TestSetting;
import com.unicornstudy.singleshop.delivery.domain.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("회원 주소 수정 테스트")
    @Test
    void address_update_test() {
        Address address = TestSetting.setAddress();
        User user = TestSetting.setUser(address);

        Address newAddress = new Address("new city", "new street", "new zipcode");

        user.updateAddress(newAddress);

        assertThat(user.getAddress()).isEqualTo(newAddress);
        assertThat(user.getAddress().getCity()).isEqualTo(newAddress.getCity());
        assertThat(user.getAddress().getStreet()).isEqualTo(newAddress.getStreet());
        assertThat(user.getAddress().getZipcode()).isEqualTo(newAddress.getZipcode());
    }

    @DisplayName("회원정보 수정 테스트")
    @Test
    void userInfo_update_test() {
        Address address = TestSetting.setAddress();
        User user = TestSetting.setUser(address);

        String newName = "new name";
        String newPicture = "new picture";

        user.update(newName, newPicture);

        assertThat(user.getName()).isEqualTo(newName);
        assertThat(user.getPicture()).isEqualTo(newPicture);
    }
}