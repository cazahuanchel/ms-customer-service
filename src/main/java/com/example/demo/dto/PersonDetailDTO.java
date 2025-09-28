package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class PersonDetailDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String nationality;
}
