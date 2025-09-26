package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomerDTO {
    private String id;
    private String idType;
    private String idNumber;
    private String customerType; // PERSONAL | ENTERPRISE
    private String email;
    private String phone;
    private PersonDetailDTO personDetail;
    private CompanyDetailDTO companyDetail;
}
