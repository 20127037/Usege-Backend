package com.group_1.payment.service;

/**
 * com.group_1.payment.service
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:09 PM
 * Description: ...
 */
public interface PaymentService {
    boolean payment(String userId, String plan, String cardNumber, String cvv, String expiredDate);
}