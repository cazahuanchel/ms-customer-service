package com.example.demo.api.impl;

import com.example.demo.api.CustomersApiDelegate;
import com.example.demo.dto.*;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.*;
import com.example.demo.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomersApiDelegateImpl implements CustomersApiDelegate {

    private final ICustomerService customerService;

    @Override
    public Mono<ResponseEntity<Flux<Customer>>> getAllCustomers(ServerWebExchange exchange) {

        Flux<CustomerDTO> fluxDto = Flux.from(customerService.getAllCustomers());
        Flux<Customer> fluxModelOpenApi = fluxDto.map(CustomerMapper::toOpenApiCustomer); // Mapear DTO -> Modelo OpenAPI

        return fluxModelOpenApi.collectList() // agrupa todos los elementos en una lista
                .map(list -> {
                    if (list.isEmpty()) {
                        return ResponseEntity.noContent().<Flux<Customer>>build(); // Para devolver 204 cuando no haya elementos
                    } else {
                        return ResponseEntity.ok(Flux.fromIterable(list)); // devolvemos un Flux
                    }
                })
                .onErrorResume(ex -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Flux<Customer>>build()); // En caso de error responde 500
                });
    }
}
