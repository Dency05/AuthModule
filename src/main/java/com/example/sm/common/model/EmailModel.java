package com.example.sm.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailModel {
    String to;
    String subject;
    String Message;

   //List<String> bcc;
   // List<String> cc;
}
