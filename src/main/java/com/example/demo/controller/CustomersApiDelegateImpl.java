package com.example.demo.controller;

import com.example.demo.api.CustomersApiDelegate;
import com.example.demo.service.ICustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomersApiDelegateImpl implements CustomersApiDelegate {

    private final ICustomer customerService;

}
