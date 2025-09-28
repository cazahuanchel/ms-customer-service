package com.example.demo.api.impl;

import com.example.demo.api.CustomerApiDelegate;
import com.example.demo.dto.EnterpriseCustomerInputDTO;
import com.example.demo.dto.PersonalCustomerInputDTO;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.model.EnterpriseCustomerInput;
import com.example.demo.model.PersonalCustomerInput;
import com.example.demo.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.example.demo.mapper.CustomerMapper;

@Service
@AllArgsConstructor
public class CustomerApiDelegateImpl implements CustomerApiDelegate {

    private final ICustomerService customerService;

    @Override
    public Mono<ResponseEntity<Customer>> getCustomer(String id, ServerWebExchange exchange) {
        return Mono.<ResponseEntity<Customer>>create(sink ->
                        customerService.getCustomerById(id)
                                .subscribe(
                                        dto -> {
                                            Customer model = CustomerMapper.toOpenApiCustomer(dto);
                                            sink.success(ResponseEntity.ok(model));
                                        },
                                        err -> {
                                            if (err instanceof NotFoundException) {
                                                sink.success(ResponseEntity.status(HttpStatus.NOT_FOUND).<Customer>build());
                                            } else {
                                                sink.error(err);
                                            }
                                        }
                                ))
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Customer>build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> createPersonalCustomer(Mono<PersonalCustomerInput> personalCustomerInput,
                                                             ServerWebExchange exchange) {
        return personalCustomerInput
                .flatMap(input -> {
                    // Usar el método de mapeo movido
                    PersonalCustomerInputDTO dto = CustomerMapper.toPersonalInputDtoFromOpenApiPersonalCustomerInput(input);

                    // Bridge RxJava Completable -> Mono<ResponseEntity<Void>>
                    return Mono.<ResponseEntity<Void>>create(sink ->
                            customerService.createPersonalCustomer(dto)
                                    .subscribe(
                                            () -> sink.success(ResponseEntity.status(HttpStatus.CREATED).<Void>build()),
                                            err -> {
                                                if (err instanceof ConflictException) {
                                                    sink.success(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build());
                                                } else {
                                                    sink.error(err);
                                                }
                                            }
                                    ));
                })
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> createEnterpriseCustomer(Mono<EnterpriseCustomerInput> enterpriseCustomerInput,
                                                               ServerWebExchange exchange) {
        return enterpriseCustomerInput
                .flatMap(input -> {
                    // Usar el método de mapeo movido
                    EnterpriseCustomerInputDTO dto = CustomerMapper.toEnterpriseInputDtoFromOpenApiEnterpriseInputDto(input);

                    return Mono.<ResponseEntity<Void>>create(sink ->
                            customerService.createEnterpriseCustomer(dto)
                                    .subscribe(
                                            () -> sink.success(ResponseEntity.status(HttpStatus.CREATED).<Void>build()),
                                            err -> {
                                                if (err instanceof ConflictException) {
                                                    sink.success(ResponseEntity.status(HttpStatus.CONFLICT).<Void>build());
                                                } else {
                                                    sink.error(err);
                                                }
                                            }
                                    ));
                })
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> updatePersonalCustomer(Mono<PersonalCustomerInput> personalCustomerInput,
                                                             ServerWebExchange exchange) {
        return personalCustomerInput.flatMap(input -> {
            PersonalCustomerInputDTO dto = CustomerMapper.toPersonalInputDtoFromOpenApiPersonalCustomerInput(input);
            String id = dto.getId();
            if (id == null || id.trim().isEmpty()) {
                return Mono.just(ResponseEntity.badRequest().<Void>build());
            }
            return Mono.create(sink ->
                    customerService.updatePersonalCustomer(id, dto)
                            .subscribe(
                                    ok -> sink.success(ResponseEntity.ok().<Void>build()),
                                    err -> {
                                        if (err instanceof NotFoundException) {
                                            sink.success(ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build());
                                        } else {
                                            sink.error(err);
                                        }
                                    }
                            ));
        }).onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> updateEnterpriseCustomer(Mono<EnterpriseCustomerInput> enterpriseCustomerInput,
                                                               ServerWebExchange exchange) {
        return enterpriseCustomerInput.flatMap(input -> {
            EnterpriseCustomerInputDTO dto = CustomerMapper.toEnterpriseInputDtoFromOpenApiEnterpriseInputDto(input);
            String id = dto.getId();
            if (id == null || id.trim().isEmpty()) {
                return Mono.just(ResponseEntity.badRequest().<Void>build());
            }
            return Mono.create(sink ->
                    customerService.updateEnterpriseCustomer(id, dto)
                            .subscribe(
                                    ok -> sink.success(ResponseEntity.ok().<Void>build()),
                                    err -> {
                                        if (err instanceof NotFoundException) {
                                            sink.success(ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build());
                                        } else {
                                            sink.error(err);
                                        }
                                    }
                            ));
        }).onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(String id, ServerWebExchange exchange) {
        return Mono.<ResponseEntity<Void>>create(sink ->
                        customerService.deleteCustomer(id)
                                .subscribe(
                                        () -> sink.success(ResponseEntity.ok().build()),
                                        err -> {
                                            if (err instanceof NotFoundException) {
                                                sink.success(ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build());
                                            } else {
                                                sink.error(err);
                                            }
                                        }
                                ))
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build()));
    }
}
