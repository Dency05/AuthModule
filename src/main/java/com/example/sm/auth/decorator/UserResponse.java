package com.example.sm.auth.decorator;

import com.example.sm.common.enums.Role;
import com.example.sm.auth.model.Address;
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
    String fullName;
    @JsonIgnore
    String token;

    @JsonIgnore
    boolean softDelete =false;
}
