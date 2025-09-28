package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class CompanyDetailDTO {
    private String ruc;
    private String companyName;
    private String registrationNumber;
    private LocalDate incorporationDate;
    private Boolean authorizedSigner;

}
