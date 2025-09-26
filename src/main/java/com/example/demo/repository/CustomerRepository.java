package com.example.demo.repository;


import com.example.demo.model.CustomerEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<CustomerEntity, String> {
    Mono<CustomerEntity> findByIdTypeAndIdNumber(String idType, String idNumber);
    Mono<Boolean> existsByIdTypeAndIdNumber(String idType, String idNumber);
}
