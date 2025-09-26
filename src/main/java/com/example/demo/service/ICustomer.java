package com.example.demo.service;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.EnterpriseCustomerInputDTO;
import com.example.demo.dto.PersonalCustomerInputDTO;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface ICustomer {

    public Flowable<CustomerDTO> getAllCustomers();
    public Single<CustomerDTO> getCustomerById(String id);
    public Completable createPersonalCustomer(PersonalCustomerInputDTO dto);
    public Completable createEnterpriseCustomer(EnterpriseCustomerInputDTO dto);
    public Single<Boolean> updatePersonalCustomer(String id, PersonalCustomerInputDTO dto);
    public Single<Boolean> updateEnterpriseCustomer(String id, EnterpriseCustomerInputDTO dto);
    public Completable deleteCustomer(String id);
}
