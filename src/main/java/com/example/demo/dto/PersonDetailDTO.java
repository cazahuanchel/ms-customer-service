package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class PersonDetailDTO {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String nationality;
}
