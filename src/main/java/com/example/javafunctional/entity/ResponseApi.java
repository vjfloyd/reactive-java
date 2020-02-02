package com.example.javafunctional.entity;

import com.example.javafunctional.JavaFunctionalApplication;
import lombok.*;

import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApi {
    String company;
    List<JavaFunctionalApplication.Account3> accounts;


}
