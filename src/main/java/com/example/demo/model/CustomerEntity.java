package com.example.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "customers")
public class CustomerEntity {
    @Id
    private String id;
    private String idType;
    private String idNumber;
    private String customerType;
    private String email;
    private String phone;
    private PersonDetailEntity personDetail;
    private CompanyDetailEntity companyDetail;
}