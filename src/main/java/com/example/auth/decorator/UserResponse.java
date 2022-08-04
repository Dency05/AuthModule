package com.example.auth.decorator;

import com.example.auth.enumUser.Role;
import com.example.auth.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    String Id;
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
