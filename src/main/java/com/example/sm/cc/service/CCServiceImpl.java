package com.example.sm.cc.service;

import com.example.sm.cc.decorator.*;
import com.example.sm.cc.enums.MembershipPlan;
import com.example.sm.cc.enums.PaymentOption;
import com.example.sm.cc.model.CCModel;
import com.example.sm.cc.model.ChapterName;
import com.example.sm.cc.model.CreditCardModel;
import com.example.sm.cc.model.E_CheckModel;
import com.example.sm.cc.repository.CCRepository;
import com.example.sm.cc.repository.ChapterNameRepository;
import com.example.sm.cc.repository.CreditCardRepository;
import com.example.sm.cc.repository.E_CheckRepository;
import com.example.sm.common.constant.MessageConstant;
import com.example.sm.common.decorator.NullAwareBeanUtilsBean;
import com.example.sm.common.exception.InvaildRequestException;
import com.example.sm.common.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CCServiceImpl implements CCService {

    @Autowired
    CCRepository ccRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    E_CheckRepository e_checkRepository;
    @Autowired
    ChapterNameRepository chapterNameRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @Override
    public CCResponse addOrUpdateMembershipPlan(String id,CCAddRequest ccAddRequest) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            CCModel ccModel= getCCModel(id);
            nullAwareBeanUtilsBean.copyProperties(ccModel,ccAddRequest);
            /*if (ccAddRequest.getBenefits().isEmpty()){
                ccModel.setBenefits(null);
            }*/
            ccRepository.save(ccModel);
            return modelMapper.map(ccModel,CCResponse.class);
        }
        else {
            CCModel ccModel = modelMapper.map(ccAddRequest, CCModel.class);
            if (!CollectionUtils.isEmpty(ccModel.getMembershipPlan())){
                for (MembershipPlans membershipPlans : ccModel.getMembershipPlan()) {
                    membershipPlans.setDate(new Date());
                    membershipPlans.setActive(true);
                }
            }
            ccModel.setDate(new Date());
            ccModel.setActive(true);
            ccRepository.save(ccModel);
            return modelMapper.map(ccModel, CCResponse.class);
        }
    }
    @Override
    public ChapterName addOrUpdateChapterName(ChapterNameAddRequest chapterNameAddRequest, String id) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            ChapterName chapterName= getChapterName(id);
            nullAwareBeanUtilsBean.copyProperties(chapterName, chapterNameAddRequest);
            chapterNameRepository.save(chapterName);
            return chapterName;
        }
        ChapterName chapterName = modelMapper.map(chapterNameAddRequest, ChapterName.class);
        chapterNameRepository.save(chapterName);
        return chapterName;
    }

    @Override
    public List<CCResponse> getMembershipPlan(MembershipPlan membershipPlan) {
        return ccRepository.getMembership(membershipPlan);
    }
    @Override
    public List<ChapterName> getChapterName(MembershipPlan membershipPlan) {
        List<ChapterName> chapterNames = chapterNameRepository.findByMembershipPlansInAndSoftDeleteIsFalse(membershipPlan);
        return chapterNames;
    }

    @Override
    public String addPayment(PaymentOption paymentOption, CreditCardRequest creditCardRequest) throws InvocationTargetException, IllegalAccessException {
       if(paymentOption == (PaymentOption.CREDIT_CARD)){
           CreditCardModel creditCardModel= new CreditCardModel();
           nullAwareBeanUtilsBean.copyProperties(creditCardModel,creditCardRequest);
           System.out.printf("creditmodel"+creditCardModel);
           creditCardRepository.save(creditCardModel);
       }
       else{
          throw new InvaildRequestException(MessageConstant.INVAILD_PAYMENT_OPTION);
       }
      return "Information Added Successfully...";
    }

    @Override
    public List<CCResponse> getAllMembership() throws InvocationTargetException, IllegalAccessException {
        List<CCModel> ccModels= ccRepository.findAllBySoftDeleteFalseAndActiveTrue();
        List<CCResponse> ccResponses= new ArrayList<>();
        log.info("CCmodel is: {}",ccModels);
        if (!CollectionUtils.isEmpty(ccModels)) {
            for (CCModel ccModel : ccModels) {
                CCResponse ccResponse = new CCResponse();
                nullAwareBeanUtilsBean.copyProperties(ccResponse, ccModel);
                ccResponses.add(ccResponse);
            }
        }
        return ccResponses;
    }

    @Override
    public void deleteMembership(String id) {
        CCModel ccModel= getCCModel(id);
        ccModel.setSoftDelete(true);
        ccModel.setActive(true);
        ccRepository.save(ccModel);
    }

    @Override
    public void addE_CheckPayment(PaymentOption paymentOption, E_CheckRequest e_checkRequest) {
        if(paymentOption==(PaymentOption.E_CHECK)){
            E_CheckModel e_checkModel= modelMapper.map(e_checkRequest,E_CheckModel.class);
            log.info("echeck :{}",e_checkModel);
            e_checkRepository.save(e_checkModel);
        }
    }

    private CCModel getCCModel(String id) {
        return ccRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }
    private ChapterName getChapterName(String id) {
        return chapterNameRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.USER_ID_NOT_FOUND));
    }
}
