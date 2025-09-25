package com.example.demo.business;

import com.example.demo.api.CustomerApiDelegate;
import com.example.demo.model.Customer;
import com.example.demo.model.EnterpriseCustomerInput;
import com.example.demo.model.PersonalCustomerInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CustomerApiDelegateImpl implements CustomerApiDelegate {

    @Override
    public Mono<ResponseEntity<Customer>> getCustomer(String id, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.getCustomer(id, exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> createPersonalCustomer(Mono<PersonalCustomerInput> personalCustomerInput, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.createPersonalCustomer(personalCustomerInput, exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> updatePersonalCustomer(Mono<PersonalCustomerInput> personalCustomerInput, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.updatePersonalCustomer(personalCustomerInput, exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> createEnterpriseCustomer(Mono<EnterpriseCustomerInput> enterpriseCustomerInput, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.createEnterpriseCustomer(enterpriseCustomerInput, exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> updateEnterpriseCustomer(Mono<EnterpriseCustomerInput> enterpriseCustomerInput, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.updateEnterpriseCustomer(enterpriseCustomerInput, exchange);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return CustomerApiDelegate.super.deleteCustomer(id, exchange);
    }
}
