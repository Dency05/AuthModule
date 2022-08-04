package com.example.sm.auth.repository;

import com.example.sm.auth.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel,String> , UserCustomRepository {

    List<UserModel> findAllBySoftDeleteFalse();

    Optional<UserModel> findByIdAndSoftDeleteIsFalse(String id);

    boolean existsByEmailAndSoftDeleteFalse(String email);

    Optional<UserModel> findByEmailAndSoftDeleteIsFalse(String email);

    Optional<UserModel> findByEmailAndPasswordAndSoftDeleteIsFalse(String email,String password);

    boolean existsByIdAndOtpAndSoftDeleteFalse(String id,String otp);

    boolean existsByIdAndSoftDeleteFalse(String id);


}
