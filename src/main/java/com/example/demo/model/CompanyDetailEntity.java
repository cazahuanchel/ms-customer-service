package com.example.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class CompanyDetailEntity {
    private String ruc;
    private String companyName;
    private String registrationNumber;
    private Date incorporationDate;
    private Boolean authorizedSigner;
}
