package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.DatabaseException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.CustomerEntity;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.ICustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Builder
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Flowable<CustomerDTO> getAllCustomers() {
        Flux<CustomerDTO> flux = customerRepository.findAll()
                .map(CustomerMapper::toDto)
                .onErrorMap(e -> new DatabaseException("Error al leer clientes desde MongoDB", e));
        return Flowable.fromPublisher(flux);
    }

    @Override
    public Single<CustomerDTO> getCustomerById(String id) {
        Mono<CustomerDTO> mono = customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado con id: " + id)))
                .map(CustomerMapper::toDto);
        return Single.fromPublisher(mono);
    }

    @Override
    public Completable createPersonalCustomer(PersonalCustomerInputDTO dto) {
        // Valida unicidad por idType + idNumber
        Mono<Void> flow = customerRepository.existsByIdTypeAndIdNumber(dto.getIdType(), dto.getIdNumber())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new ConflictException("Cliente ya existe con idType/idNumber"));
                    } else {
                        CustomerEntity entity = CustomerMapper.toEntityFromPersonalCustomerInputDTO(dto);
                        return customerRepository.save(entity).then();
                    }
                });
        return Completable.fromPublisher(flow);
    }

    @Override
    public Completable createEnterpriseCustomer(EnterpriseCustomerInputDTO dto) {
        Mono<Void> flow = customerRepository.existsByIdTypeAndIdNumber(dto.getIdType(), dto.getIdNumber())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new ConflictException("Cliente ya existe con idType/idNumber"));
                    } else {
                        CustomerEntity entity = CustomerMapper.toEntityFromEnterpriseInput(dto);
                        return customerRepository.save(entity).then();
                    }
                });
        return Completable.fromPublisher(flow);
    }

    @Override
    public Single<Boolean> updatePersonalCustomer(String id, PersonalCustomerInputDTO dto) {
        Mono<Boolean> mono = customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado con id: " + id)))
                .flatMap(entity -> {
                    CustomerMapper.updateEntityFromPersonalInput(entity, dto);
                    return customerRepository.save(entity).thenReturn(Boolean.TRUE);
                });
        return Single.fromPublisher(mono);
    }

    @Override
    public Single<Boolean> updateEnterpriseCustomer(String id, EnterpriseCustomerInputDTO dto) {
        Mono<Boolean> mono = customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado con id: " + id)))
                .flatMap(entity -> {
                    CustomerMapper.updateEntityFromEnterpriseInput(entity, dto);
                    return customerRepository.save(entity).thenReturn(Boolean.TRUE);
                });
        return Single.fromPublisher(mono);
    }

    @Override
    public Completable deleteCustomer(String id) {
        Mono<Void> mono = customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente no encontrado con id: " + id)))
                .flatMap(existing -> customerRepository.deleteById(id));
        return Completable.fromPublisher(mono);
    }
}
