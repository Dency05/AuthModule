package com.example.sm.auth.model;

import com.example.sm.common.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document(collection= "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserModel {
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
    String fullName;

    String otp;

    @JsonIgnore
    boolean softDelete = false;

    public void setFullName() {

        this.firstName = StringUtils.normalizeSpace(this.firstName);
        this.middleName = StringUtils.normalizeSpace(this.middleName);
        this.lastName = StringUtils.normalizeSpace(this.lastName);
        List<String> fullNameList = new LinkedList<>();
        fullNameList.add(firstName);
        fullNameList.add(middleName);
        fullNameList.add(lastName);

        //loop over the full name list
        //check the element of list is empty or not
        //if not empty then add element to the variable
        StringBuilder name1 = new StringBuilder();
        for (String fullName : fullNameList) {
            if(!fullName.isEmpty()&& fullName != null){
                        name1.append(" ").append(fullName);
                       // String s = firstName + " " + middleName + " " + lastName;
                this.fullName = name1.toString();
                String[] names = name1.toString().split(" ");
                if (names.length == 1) {
                    this.firstName = names[0];
                } else if (names.length == 2) {
                    this.firstName = names[0];
                    this.lastName = names[1];
                } else if (names.length == 3) {
                    this.firstName = names[0];
                    this.middleName = names[1];
                    this.lastName = names[2];
                } else if (names.length > 3) {
                    this.firstName = names[0];
                    this.middleName = names[1];
                    StringBuilder name = new StringBuilder();
                    for (String value : names) {
                        if (!value.equals(firstName) && !value.equals(middleName)) {
                            name.append(" ").append(value);
                        }
                    }
                    this.lastName = name.toString().trim();
                }
            }

        }
    }
}