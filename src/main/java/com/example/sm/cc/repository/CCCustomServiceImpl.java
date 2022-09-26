package com.example.sm.cc.repository;

import com.example.sm.cc.decorator.CCResponse;
import com.example.sm.cc.enums.MembershipPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class CCCustomServiceImpl implements  CCCustomService{

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public List<CCResponse> getMembership(MembershipPlan membershipPlan) {
        Criteria criteria = new Criteria();
        List<CCResponse> ccResponses;
        if (membershipPlan != null) {
            criteria = criteria.and("membershipPlan").is(membershipPlan);
        }
        criteria = criteria.and("softDelete").is(false);
        Query query = new Query();
        query.addCriteria(criteria);
        ccResponses = mongoTemplate.find(query,CCResponse.class, "cc_membership");
        System.out.println(ccResponses.size());
        return ccResponses;
    }

    public List<CCResponse> getAllMembership() {
        Criteria criteria = new Criteria();
        criteria = criteria.and("softDelete").is(false);
        criteria = criteria.and("active").is(true);
        Query query = new Query();
        query.addCriteria(criteria);
        List<CCResponse> ccResponses = mongoTemplate.find(query,CCResponse.class, "cc     _membership");
        System.out.println(ccResponses.size());
        return ccResponses;
    }
}
