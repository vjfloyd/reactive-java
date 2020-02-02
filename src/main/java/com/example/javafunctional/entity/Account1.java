package com.example.javafunctional.entity;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account1 {
    String code;
    String type;
    String company;
}
