package com.example.sm.cc.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum MembershipPlan {
    LIFETIME("lifetime"),
    TWELVE_YEAR("12_year"),
    SIX_YEAR("6 Year"),
    Two_Months("2 months");

    private String value;
    MembershipPlan(String value){
        this.value= value;
    }
}
