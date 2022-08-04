package com.example.sm.auth.decorator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFilter {
    //TODO RENAME Search To search
    String Search;
    String Id;

    @JsonIgnore
    boolean softDelete = false;

    public String getSearch(){
        if(Search !=null){
            return Search.trim();
        }
        return Search;
    }
}
