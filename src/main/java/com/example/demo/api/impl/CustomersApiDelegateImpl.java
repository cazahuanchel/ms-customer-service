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

    // -----------------------
    // GET /customers
    // -----------------------
    @Override
    public Mono<ResponseEntity<Flux<Customer>>> getAllCustomers(ServerWebExchange exchange) {
        // Flowable<CustomerDTO> -> Flux<CustomerDTO>
        Flux<CustomerDTO> fluxDto = Flux.from(customerService.getAllCustomers());

        // Mapear DTO -> Modelo OpenAPI
        Flux<Customer> fluxModelOpenApi = fluxDto.map(CustomerMapper::toOpenApiCustomer);

        // Para devolver 204 cuando no haya elementos, recolectamos la lista una vez.
        return fluxModelOpenApi.collectList()
                .map(list -> {
                    if (list.isEmpty()) {
                        return ResponseEntity.noContent().<Flux<Customer>>build();
                    } else {
                        // devolvemos un Flux desde la lista (no streaming continuo, pero sencillo)
                        return ResponseEntity.ok(Flux.fromIterable(list));
                    }
                })
                .onErrorResume(ex -> {
                    // En caso de error inesperado, devolvemos 500
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Flux<Customer>>build());
                });
    }
}
