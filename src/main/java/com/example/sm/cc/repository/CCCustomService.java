package com.example.sm.cc.repository;

import com.example.sm.cc.decorator.CCResponse;
import com.example.sm.cc.enums.MembershipPlan;

import java.util.List;

public interface CCCustomService {

    List<CCResponse> getMembership(MembershipPlan membershipPlan);
    List<CCResponse> getAllMembership();
}
