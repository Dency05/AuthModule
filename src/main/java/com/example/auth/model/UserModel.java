package com.example.auth.model;

import com.example.auth.decorator.UserResponse;
import com.example.auth.enumUser.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel extends UserResponse {
    @Id
    String id;
    String firstName;
    String middleName;
    String lastName;
    String age;
    String email;
    String userName;
    String password;
    Address address;
    Role role;

    @JsonIgnore
    boolean softDelete =false;
}
