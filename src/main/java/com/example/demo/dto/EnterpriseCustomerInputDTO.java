package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EnterpriseCustomerInputDTO {
    private String idType;
    private String idNumber;
    private String customerType; // ENTERPRISE
    private String email;
    private String phone;
    private CompanyDetailDTO companyDetail;
}
