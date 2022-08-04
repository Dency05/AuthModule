package com.example.sm.auth.decorator;

import com.example.sm.auth.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddRequest {
    String firstName;
    String middleName;
    String lastName;
    String age;
    String email;
    String userName;
    String password;
    Address address;
    String confirmPassword;

}
