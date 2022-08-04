package com.example.auth.repository;

import com.example.auth.decorator.UserResponse;
import com.example.auth.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel,String> ,UserCustomRepository{
    List<UserResponse> findAllBySoftDeleteFalse();
    Optional<UserModel> findByIdAndSoftDeleteIsFalse(String id);

}
