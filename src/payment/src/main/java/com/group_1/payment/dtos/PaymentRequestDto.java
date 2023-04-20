package com.group_1.payment.dtos;

/**
 * com.group_1.payment.dtos
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:11 PM
 * Description: ...
 */
public record PaymentRequestDto(String planName, String cardNumber, String cvv, String expiredDate) {

}
