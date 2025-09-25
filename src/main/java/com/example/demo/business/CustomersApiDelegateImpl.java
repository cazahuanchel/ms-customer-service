package com.example.demo.business;

import com.example.demo.api.CustomersApiDelegate;
import com.example.demo.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomersApiDelegateImpl implements CustomersApiDelegate {

    @Override
    public Mono<ResponseEntity<Flux<Customer>>> getAllCustomers(ServerWebExchange exchange) {
        return CustomersApiDelegate.super.getAllCustomers(exchange);
    }
}
