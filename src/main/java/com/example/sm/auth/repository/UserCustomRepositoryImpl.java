package com.example.sm.auth.repository;

import com.example.sm.common.decorator.*;
import com.example.sm.auth.decorator.UserFilter;
import com.example.sm.auth.decorator.UserResponse;
import com.example.sm.auth.enums.UserSortBy;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<UserResponse> findAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) {

        List<AggregationOperation> operations = userFilterAggregation(filter,sort,pagination,true);
        //Created Aggregation operation
        Aggregation aggregation = newAggregation(operations);

        List<UserResponse> users = mongoTemplate.aggregate(aggregation, "users",UserResponse.class).getMappedResults();

        // Find Count
        List<AggregationOperation> operationForCount = userFilterAggregation(filter,sort,pagination,false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(UserResponse.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount , "Department", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                users,
                pagination,
                () -> count);
    }

    //create list
    //match userentered value and databasevalue(use: getCriteria method)
    //if addpage true then perfom sorting
    //return list

    private List<AggregationOperation> userFilterAggregation(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination, boolean addPage) {
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(match(getCriteria(filter,operations)));

        if(addPage){
            //sorting
            if(sort!=null && sort.getSortBy() != null && sort.getOrderBy() != null) {
                operations.add(new SortOperation(Sort.by(sort.getOrderBy(), sort.getSortBy().getValue())));
            }
            if(pagination!=null) {
                operations.add(skip(pagination.getOffset()));
                operations.add(limit(pagination.getPageSize()));
            }
        }
        return operations;

    }


    private Criteria getCriteria(UserFilter userFilter, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(new CustomAggregationOperation(
                new Document("$addFields",
                        new Document("search",
                                new Document("$concat",Arrays.asList(
                                        new Document("$ifNull", Arrays.asList("$userName","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$password","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$email","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$lastName","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$middleName","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$firstName","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$age","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.address1","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.address2","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.address3","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.city","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.state","")),
                                        "|@|",new Document("$ifNull",Arrays.asList("$address.zipCode","")))
                                )
                        )
                ))
        );

        if (!StringUtils.isEmpty(userFilter.getSearch())) {
            userFilter.setSearch(userFilter.getSearch().replaceAll("\\|@\\|", ""));
            userFilter.setSearch(userFilter.getSearch().replaceAll("\\|@@\\|", ""));
            criteria = criteria.orOperator(
                    Criteria.where("search").regex(".*" + userFilter.getSearch() + ".*", "i")
            );
        }

        if(!StringUtils.isEmpty(userFilter.getId())){
            criteria= criteria.and("_id").in(userFilter.getId());
        }

        criteria = criteria.and("softDelete").is(false);
        return criteria;
    }
}







