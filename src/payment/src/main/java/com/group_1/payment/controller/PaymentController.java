package com.group_1.payment.controller;

import com.group_1.payment.dtos.PaymentRequestDto;
import com.group_1.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.group_1.payment.controller
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:10 PM
 * Description: ...
 */
@RestController
@Slf4j
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("{id}")
    public ResponseEntity payment(@PathVariable String id, @RequestBody PaymentRequestDto requestDto)
    {
        if (paymentService.payment(id, requestDto.planName(), requestDto.cardNumber(), requestDto.cvv(), requestDto.expiredDate()))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
