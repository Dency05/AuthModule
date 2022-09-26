package com.example.sm.cc.service;

import com.example.sm.cc.decorator.*;
import com.example.sm.cc.enums.MembershipPlan;
import com.example.sm.cc.enums.PaymentOption;
import com.example.sm.cc.model.ChapterName;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CCService {
    CCResponse addOrUpdateMembershipPlan(String id,CCAddRequest ccAddRequest) throws InvocationTargetException, IllegalAccessException;

    ChapterName addOrUpdateChapterName(ChapterNameAddRequest chapterNameAddRequest,String id) throws InvocationTargetException, IllegalAccessException;

    List<CCResponse> getMembershipPlan(MembershipPlan membershipPlan);

    List<ChapterName> getChapterName(MembershipPlan membershipPlan);


    String addPayment(PaymentOption paymentOption, CreditCardRequest creditCardRequest) throws InvocationTargetException, IllegalAccessException;

    List<CCResponse> getAllMembership() throws InvocationTargetException, IllegalAccessException;

    void deleteMembership(String id);

    void addE_CheckPayment(PaymentOption paymentOption, E_CheckRequest e_checkRequest);
}
