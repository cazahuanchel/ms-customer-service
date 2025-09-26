package com.example.demo.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "customer")
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
