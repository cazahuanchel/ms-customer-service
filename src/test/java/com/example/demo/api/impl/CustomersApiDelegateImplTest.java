package com.example.demo.api.impl;

import com.example.demo.dto.CompanyDetailDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.CustomerTypeEnum;
import com.example.demo.dto.PersonDetailDTO;
import com.example.demo.model.Customer;
import com.example.demo.service.ICustomerService;
import io.reactivex.rxjava3.core.Flowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomersApiDelegateImplTest {

    @Mock
    private ICustomerService customerService;

    private CustomersApiDelegateImpl delegate;

    @BeforeEach
    void setUp() {
        delegate = new CustomersApiDelegateImpl(customerService);
    }

    @Test
    void getAllCustomers_returnsOkAndBodyWithCustomers() {
        // Arrange
        CustomerDTO dto1 = new CustomerDTO();
        dto1.setId("dc659b24-492f-420e-a6f7-90edb0a507b8");
        dto1.setIdType("DNI");
        dto1.setIdNumber("12345678");
        dto1.setCustomerType(CustomerTypeEnum.PERSONAL);
        dto1.setEmail("personal@example.com");
        dto1.setPhone("999999999");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Juan");
        pd.setLastName("Perez");
        pd.setBirthDate(LocalDate.of(1990, 1, 1));
        pd.setNationality("Peruano");
        dto1.setPersonDetail(pd);

        CustomerDTO dto2 = new CustomerDTO();
        dto2.setId("69788cac-d3a3-43c1-a36d-0a7ba62aa9de");
        dto2.setIdType("RUC");
        dto2.setIdNumber("20123456789");
        dto2.setCustomerType(CustomerTypeEnum.ENTERPRISE);
        dto2.setEmail("enterprise@example.com");
        dto2.setPhone("988888888");

        PersonDetailDTO pd2 = new PersonDetailDTO();
        pd2.setFirstName("Maria");
        pd2.setLastName("Gomez");
        pd2.setBirthDate(LocalDate.of(1985, 5, 5));
        pd2.setNationality("Peruano");
        dto2.setPersonDetail(pd2);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        dto2.setCompanyDetail(cd);

        when(customerService.getAllCustomers()).thenReturn(Flowable.fromArray(dto1, dto2));

        // Act
        ResponseEntity<Flux<Customer>> response = delegate.getAllCustomers(null).block();

        // Assert
        assertNotNull(response, "El ResponseEntity no debe ser nulo");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debe devolver 200 OK");

        // El body es un Flux creado a partir de la lista
        Flux<Customer> bodyFlux = response.getBody();
        assertNotNull(bodyFlux, "El body Flux no debe ser nulo cuando hay elementos");
        List<Customer> list = bodyFlux.collectList().block();
        assertNotNull(list, "La lista recolectada no debe ser nula");
        assertEquals(2, list.size(), "Deben devolverse 2 elementos");

        // Verificar interacción el mock
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getAllCustomers_returnsNoContentWhenEmpty() {
        // Arrange
        when(customerService.getAllCustomers()).thenReturn(Flowable.empty());

        // Act
        ResponseEntity<Flux<Customer>> response = delegate.getAllCustomers(null).block();

        // Assert
        assertNotNull(response, "La ResponseEntity no debe ser nula");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Debe devolver 204 No Content");
        assertNull(response.getBody(), "El body debe ser nulo en 204 No Content");

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getAllCustomers_returnsInternalServerErrorOnException() {
        // Arrange
        when(customerService.getAllCustomers()).thenReturn(Flowable.error(new RuntimeException("boom")));

        // Act
        ResponseEntity<Flux<Customer>> response = delegate.getAllCustomers(null).block();

        // Assert
        assertNotNull(response, "La ResponseEntity no debe ser nula");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Debe devolver 500 Internal Server Error");
        assertNull(response.getBody(), "El body debe ser nulo en caso de error");

        verify(customerService, times(1)).getAllCustomers();
    }
}
