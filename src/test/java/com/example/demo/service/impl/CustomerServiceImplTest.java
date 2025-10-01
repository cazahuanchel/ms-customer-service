package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.DatabaseException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CompanyDetailEntity;
import com.example.demo.model.CustomerEntity;
import com.example.demo.model.PersonDetailEntity;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerEntity personalEntity;
    private CustomerEntity enterpriseEntity;

    @BeforeEach
    void setUp() {
        // PERSONAL CUSTOMER
        personalEntity = new CustomerEntity();
        personalEntity.setId("dc659b24-492f-420e-a6f7-90edb0a507b8");
        personalEntity.setIdType("DNI");
        personalEntity.setIdNumber("12345678");
        personalEntity.setCustomerType("PERSONAL");
        personalEntity.setEmail("personal@example.com");
        personalEntity.setPhone("999999999");

        PersonDetailEntity pd = new PersonDetailEntity();
        pd.setFirstName("Juan");
        pd.setLastName("Perez");
        pd.setBirthDate(LocalDate.of(1990, 1, 1));
        pd.setNationality("Peruano");

        personalEntity.setPersonDetail(pd);
        personalEntity.setCompanyDetail(null);

        //ENTERPRISE CUSTOMER
        enterpriseEntity = new CustomerEntity();
        enterpriseEntity.setId("69788cac-d3a3-43c1-a36d-0a7ba62aa9de");
        enterpriseEntity.setIdType("RUC");
        enterpriseEntity.setIdNumber("20123456789");
        enterpriseEntity.setCustomerType("ENTERPRISE");
        enterpriseEntity.setEmail("enterprise@example.com");
        enterpriseEntity.setPhone("988888888");

        PersonDetailEntity pd2 = new PersonDetailEntity();
        pd2.setFirstName("Maria");
        pd2.setLastName("Gomez");
        pd2.setBirthDate(LocalDate.of(1985, 5, 5));
        pd2.setNationality("Peruano");
        enterpriseEntity.setPersonDetail(pd2);

        CompanyDetailEntity cd = new CompanyDetailEntity();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        enterpriseEntity.setCompanyDetail(cd);
    }

    @Test
    void getAllCustomers_shouldReturnList() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Flux.just(personalEntity, enterpriseEntity));

        // Act
        TestSubscriber<CustomerDTO> ts = customerService.getAllCustomers().test();

        // Assert
        ts.assertNoErrors();
        ts.assertComplete();
        ts.assertValueCount(2);
        ts.assertValueAt(0, dto -> "12345678".equals(dto.getIdNumber()) && dto.getCustomerType().toString().equals("PERSONAL"));
        ts.assertValueAt(1, dto -> "20123456789".equals(dto.getIdNumber()) && dto.getCustomerType().toString().equals("ENTERPRISE"));
    }

    @Test
    void getAllCustomers_shouldReturnListEmpty() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Flux.empty());

        // Act
        TestSubscriber<CustomerDTO> ts = customerService.getAllCustomers().test();

        // Assert
        ts.assertNoErrors();
        ts.assertNoValues();
        ts.assertComplete();
        ts.assertValueCount(0);
    }

    @Test
    void getAllCustomers_shouldReturnDataBaseException() {
        // Arrange
        DatabaseException dbEx = new DatabaseException("Error al leer clientes desde MongoDB", null);
        when(customerRepository.findAll()).thenReturn(Flux.error(dbEx));

        // Act
        TestSubscriber<CustomerDTO> ts = customerService.getAllCustomers().test();

        // Assert
        ts.assertNoValues();
        ts.assertError(DatabaseException.class);
        ts.assertNotComplete();
    }

    @Test
    void getCustomerById_shouldReturnCustomer_whenExists() {
        // Arrange
        String id = "dc659b24-492f-420e-a6f7-90edb0a507b8";
        String idNumber = "12345678";
        when(customerRepository.findById(id)).thenReturn(Mono.just(personalEntity));

        // Act
        TestObserver<CustomerDTO> to = customerService.getCustomerById(id).test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
        to.assertValue(dto -> id.equals(dto.getId()) && idNumber.equals(dto.getIdNumber()));
    }

    @Test
    void getCustomerById_shouldErrorNotFound_whenNotExists() {
        // Arrange
        when(customerRepository.findById("no-id")).thenReturn(Mono.empty());

        // Act
        TestObserver<CustomerDTO> to = customerService.getCustomerById("no-id").test();

        // Assert
        to.assertNoValues();
        to.assertError(NotFoundException.class);
    }

    @Test
    void createPersonalCustomer_shouldComplete_whenNotExists() {
        // Arrange
        PersonalCustomerInputDTO input = new PersonalCustomerInputDTO();
        input.setIdType("DNI");
        input.setIdNumber("12345678");
        input.setCustomerType("PERSONAL");
        input.setEmail("personal@example.com");
        input.setPhone("988888888");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Juan");
        pd.setLastName("Perez");
        pd.setBirthDate(LocalDate.of(1990, 1, 1));
        pd.setNationality("Peruano");
        input.setPersonDetail(pd);


        when(customerRepository.existsByIdTypeAndIdNumber("DNI", "12345678")).thenReturn(Mono.just(Boolean.FALSE));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(Mono.just(personalEntity));

        // Act
        TestObserver<Void> to = customerService.createPersonalCustomer(input).test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    void createPersonalCustomer_shouldErrorConflict_whenExists() {
        // Arrange
        PersonalCustomerInputDTO input = new PersonalCustomerInputDTO();
        input.setIdType("DNI");
        input.setIdNumber("12345678");

        when(customerRepository.existsByIdTypeAndIdNumber("DNI", "12345678")).thenReturn(Mono.just(Boolean.TRUE));

        // Act
        TestObserver<Void> to = customerService.createPersonalCustomer(input).test();

        // Assert
        to.assertError(ConflictException.class);
    }

    @Test
    void createEnterpriseCustomer_shouldComplete_whenNotExists() {
        // Arrange
        EnterpriseCustomerInputDTO input = new EnterpriseCustomerInputDTO();
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        input.setCustomerType("ENTERPRISE");
        input.setEmail("ent@test.com");
        input.setPhone("988888888");

        when(customerRepository.existsByIdTypeAndIdNumber("RUC", "20123456789")).thenReturn(Mono.just(Boolean.FALSE));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(Mono.just(enterpriseEntity));

        // Act
        TestObserver<Void> to = customerService.createEnterpriseCustomer(input).test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    void createEnterpriseCustomer_shouldErrorConflict_whenExists() {
        // Arrange
        EnterpriseCustomerInputDTO input = new EnterpriseCustomerInputDTO();
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        input.setCustomerType("ENTERPRISE");
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetailDTO pd2 = new PersonDetailDTO();
        pd2.setFirstName("Maria");
        pd2.setLastName("Gomez");
        pd2.setBirthDate(LocalDate.of(1985, 5, 5));
        pd2.setNationality("Peruano");
        input.setPersonDetailDTO(pd2);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);

        when(customerRepository.existsByIdTypeAndIdNumber("RUC", "20123456789")).thenReturn(Mono.just(Boolean.TRUE));

        // Act
        TestObserver<Void> to = customerService.createEnterpriseCustomer(input).test();

        // Assert
        to.assertError(ConflictException.class);
    }

    @Test
    void updatePersonalCustomer_shouldReturnTrue_whenExists() {
        // Arrange
        PersonalCustomerInputDTO input = new PersonalCustomerInputDTO();
        input.setIdType("DNI");
        input.setIdNumber("12345678");
        input.setCustomerType("PERSONAL");
        input.setEmail("updated@test.com");
        input.setPhone("966666666");

        when(customerRepository.findById("1")).thenReturn(Mono.just(personalEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(Mono.just(personalEntity));

        // Act
        TestObserver<Boolean> to = customerService.updatePersonalCustomer("1", input).test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
        to.assertValue(Boolean.TRUE);
    }

    @Test
    void updatePersonalCustomer_shouldErrorNotFound_whenNotExists() {
        // Arrange
        PersonalCustomerInputDTO input = new PersonalCustomerInputDTO();
        when(customerRepository.findById("no-id")).thenReturn(Mono.empty());

        // Act
        TestObserver<Boolean> to = customerService.updatePersonalCustomer("no-id", input).test();

        // Assert
        to.assertError(NotFoundException.class);
    }

    @Test
    void updateEnterpriseCustomer_shouldReturnTrue_whenExists() {
        // Arrange
        EnterpriseCustomerInputDTO input = new EnterpriseCustomerInputDTO();
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        input.setCustomerType("ENTERPRISE");
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetailDTO pd2 = new PersonDetailDTO();
        pd2.setFirstName("Maria");
        pd2.setLastName("Gomez");
        pd2.setBirthDate(LocalDate.of(1985, 5, 5));
        pd2.setNationality("Peruano");
        input.setPersonDetailDTO(pd2);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);

        when(customerRepository.findById("2")).thenReturn(Mono.just(enterpriseEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(Mono.just(enterpriseEntity));

        // Act
        TestObserver<Boolean> to = customerService.updateEnterpriseCustomer("2", input).test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
        to.assertValue(Boolean.TRUE);
    }

    @Test
    void updateEnterpriseCustomer_shouldErrorNotFound_whenNotExists() {
        // Arrange
        EnterpriseCustomerInputDTO input = new EnterpriseCustomerInputDTO();
        when(customerRepository.findById("no-id")).thenReturn(Mono.empty());

        // Act
        TestObserver<Boolean> to = customerService.updateEnterpriseCustomer("no-id", input).test();

        // Assert
        to.assertError(NotFoundException.class);
    }

    @Test
    void deleteCustomer_shouldComplete_whenExists() {
        // Arrange
        when(customerRepository.findById("1")).thenReturn(Mono.just(personalEntity));
        when(customerRepository.deleteById("1")).thenReturn(Mono.empty());

        // Act
        TestObserver<Void> to = customerService.deleteCustomer("1").test();

        // Assert
        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    void deleteCustomer_shouldErrorNotFound_whenNotExists() {
        // Arrange
        when(customerRepository.findById("no-id")).thenReturn(Mono.empty());

        // Act
        TestObserver<Void> to = customerService.deleteCustomer("no-id").test();

        // Assert
        to.assertError(NotFoundException.class);
    }
}
