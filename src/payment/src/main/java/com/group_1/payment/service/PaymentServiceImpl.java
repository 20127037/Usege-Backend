package com.group_1.payment.service;

import com.group_1.payment.exception.InvalidCardException;
import com.group_1.payment.exception.InvalidPlanException;
import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.PaymentHistory;
import com.group_1.sharedDynamoDB.model.StoragePlan;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.DynamoDbRepository;
import com.group_1.sharedDynamoDB.repository.PaymentHistoryRepository;
import com.group_1.sharedDynamoDB.repository.StoragePlanRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import com.group_1.utilities.MemoryUtilities;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * com.group_1.payment.service
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:15 PM
 * Description: ...
 */
@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserRepository userRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final StoragePlanRepository storagePlanRepository;

    private static final int MIN_CARD_LENGTH = 13;
    private static final int MAX_CARD_LENGTH = 16;
    private static final int CVV_LENGTH = 3;
    private final Pattern allNumberPattern = Pattern.compile("-?[0-9]+");

    private boolean checkCard(String cardNumber, String cvv, String expiredDate)
    {
        int cardNumberLength = cardNumber.length();
        if (cardNumberLength < MIN_CARD_LENGTH || cardNumberLength > MAX_CARD_LENGTH)
            return false;
        if (cvv.length() != CVV_LENGTH)
            return false;
        if (!allNumberPattern.matcher(cardNumber).matches())
            return false;
        if (!allNumberPattern.matcher(cvv).matches())
            return false;
        return true;
    }

    @Override
    public boolean payment(String userId, String plan, String cardNumber, String cvv, String expiredDate) {

        StoragePlan storagePlan = storagePlanRepository.getRecordByKey(DynamoDbRepository.getKey(plan));
        if (storagePlan == null)
            throw new NoSuchElementFoundException(plan, "storagePlans");
        if (!checkCard(cardNumber, cvv, expiredDate))
            throw new InvalidCardException(cardNumber);
        UserInfo userInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        StoragePlan currentPlan = storagePlanRepository.getRecordByKey(DynamoDbRepository.getKey(userInfo.getPlan()));
        if (currentPlan.getOrder() >= storagePlan.getOrder())
            throw new InvalidPlanException(storagePlan.getName());
        String now = LocalDateTime.now().toString();
        userRepository.updateRecord(DynamoDbRepository.getKey(userId), i -> {
            i.setPlan(storagePlan.getName());
            i.setPlanOrder(storagePlan.getOrder());
            i.setMaxSpace(MemoryUtilities.gbToKb(storagePlan.getMaximumSpaceInGB()));
            i.setPurchasedPlanDate(now);
        });
        paymentHistoryRepository.saveRecord(PaymentHistory.builder()
                .userId(userId)
                .planName(plan)
                .cardNumber(cardNumber)
                .expiredDate(expiredDate)
                .purchasedDate(now)
            .build());
        return true;
    }
}
