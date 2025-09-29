package com.example.demo.api.impl;

import com.example.demo.dto.*;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.*;
import com.example.demo.service.ICustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerApiDelegateImplTest {

    @Mock
    private ICustomerService customerService;

    @InjectMocks
    private CustomerApiDelegateImpl delegate;

    @Test
    void getCustomer_success_returnsOkAndCustomer() {
        // Arrange
        String id = "dc659b24-492f-420e-a6f7-90edb0a507b8";
        CustomerDTO dto = new CustomerDTO();
        dto.setId("dc659b24-492f-420e-a6f7-90edb0a507b8");
        dto.setIdType("DNI");
        dto.setIdNumber("12345678");
        dto.setCustomerType(CustomerTypeEnum.PERSONAL);
        dto.setEmail("personal@example.com");
        dto.setPhone("999999999");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Juan");
        pd.setLastName("Perez");
        pd.setBirthDate(LocalDate.of(1990, 1, 1));
        pd.setNationality("Peruano");
        dto.setPersonDetail(pd);
        when(customerService.getCustomerById(id)).thenReturn(Single.just(dto));

        // Act
        Mono<ResponseEntity<Customer>> responseMono = delegate.getCustomer(id, null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> {
                    assertEquals(HttpStatus.OK, resp.getStatusCode());
                    assertNotNull(resp.getBody());
                    assertEquals(id, resp.getBody().getId());
                    assertEquals("DNI", resp.getBody().getIdType());
                })
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(id);
    }

    @Test
    void getCustomer_notFound_returns404() {
        // Arrange
        String id = "not-exist";
        when(customerService.getCustomerById(id)).thenReturn(Single.error(new NotFoundException("no")) );

        // Act
        Mono<ResponseEntity<Customer>> responseMono = delegate.getCustomer(id, null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(id);
    }

    @Test
    void getCustomer_unexpectedError_returns500() {
        // Arrange
        String id = "err";
        when(customerService.getCustomerById(id)).thenReturn(Single.error(new RuntimeException("error")));

        // Act
        Mono<ResponseEntity<Customer>> responseMono = delegate.getCustomer(id, null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).getCustomerById(id);
    }

    @Test
    void createPersonalCustomer_success_returns201() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId("39aafcec-68bc-42c3-b84b-50330a20f189");
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        when(customerService.createPersonalCustomer(any(PersonalCustomerInputDTO.class))).thenReturn(Completable.complete());

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.createPersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.CREATED, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).createPersonalCustomer(any(PersonalCustomerInputDTO.class));
    }

    @Test
    void createPersonalCustomer_conflict_returns409() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId("39aafcec-68bc-42c3-b84b-50330a20f189");
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        when(customerService.createPersonalCustomer(any(PersonalCustomerInputDTO.class))).thenReturn(Completable.error(new ConflictException("exists")));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.createPersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.CONFLICT, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).createPersonalCustomer(any(PersonalCustomerInputDTO.class));
    }

    @Test
    void createPersonalCustomer_unexpectedError_returns500() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId("39aafcec-68bc-42c3-b84b-50330a20f189");
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        when(customerService.createPersonalCustomer(any(PersonalCustomerInputDTO.class))).thenReturn(Completable.error(new RuntimeException("error")));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.createPersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).createPersonalCustomer(any(PersonalCustomerInputDTO.class));
    }

    @Test
    void createEnterpriseCustomer_success_returns201() {
        // Arrange
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        input.setId("69788cac-d3a3-43c1-a36d-0a7ba62aa9de");
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        EnterpriseCustomerInput.CustomerTypeEnum ct = EnterpriseCustomerInput.CustomerTypeEnum.valueOf("ENTERPRISE");
        input.setCustomerType(ct);
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetail pd = new PersonDetail();
        pd.setFirstName("Maria");
        pd.setLastName("Gomez");
        pd.setBirthDate(LocalDate.of(1985, 5, 5));
        pd.setNationality("Peruano");
        input.setPersonDetail(pd);

        CompanyDetail cd = new CompanyDetail();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        input.setCompanyDetail(cd);

        when(customerService.createEnterpriseCustomer(any(EnterpriseCustomerInputDTO.class))).thenReturn(Completable.complete());

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.createEnterpriseCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.CREATED, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).createEnterpriseCustomer(any(EnterpriseCustomerInputDTO.class));
    }

    @Test
    void createEnterpriseCustomer_conflict_returns409() {
        // Arrange
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        input.setId("69788cac-d3a3-43c1-a36d-0a7ba62aa9de");
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        EnterpriseCustomerInput.CustomerTypeEnum ct = EnterpriseCustomerInput.CustomerTypeEnum.valueOf("ENTERPRISE");
        input.setCustomerType(ct);
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetail pd = new PersonDetail();
        pd.setFirstName("Maria");
        pd.setLastName("Gomez");
        pd.setBirthDate(LocalDate.of(1985, 5, 5));
        pd.setNationality("Peruano");
        input.setPersonDetail(pd);

        CompanyDetail cd = new CompanyDetail();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        input.setCompanyDetail(cd);

        when(customerService.createEnterpriseCustomer(any(EnterpriseCustomerInputDTO.class))).thenReturn(Completable.error(new ConflictException("exists")));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.createEnterpriseCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.CONFLICT, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).createEnterpriseCustomer(any(EnterpriseCustomerInputDTO.class));
    }

    @Test
    void updatePersonalCustomer_noId_returnsBadRequest() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId(null); // ID NULL
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.updatePersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode()))
                .verifyComplete();

        verifyNoInteractions(customerService);
    }

    @Test
    void updatePersonalCustomer_success_returnsOk() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId("39aafcec-68bc-42c3-b84b-50330a20f189");
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        when(customerService.updatePersonalCustomer(eq("39aafcec-68bc-42c3-b84b-50330a20f189"), any(PersonalCustomerInputDTO.class)))
                .thenReturn(Single.just(Boolean.TRUE));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.updatePersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.OK, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).updatePersonalCustomer(eq("39aafcec-68bc-42c3-b84b-50330a20f189"), any(PersonalCustomerInputDTO.class));
    }

    @Test
    void updatePersonalCustomer_notFound_returns404() {
        // Arrange
        PersonalCustomerInput input = new PersonalCustomerInput();
        input.setId("aaa");
        input.setIdType("DNI");
        input.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        input.setCustomerType(typeEnum);
        input.setEmail("gersoncampos@example.com");
        input.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        input.setPersonDetail(inPd);

        when(customerService.updatePersonalCustomer(eq("aaa"), any(PersonalCustomerInputDTO.class))).thenReturn(Single.error(new NotFoundException("not found")));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.updatePersonalCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).updatePersonalCustomer(eq("aaa"), any(PersonalCustomerInputDTO.class));
    }

    @Test
    void updateEnterpriseCustomer_success_returnsOk() {
        // Arrange
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        input.setId("69788cac-d3a3-43c1-a36d-0a7ba62aa9de");
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        EnterpriseCustomerInput.CustomerTypeEnum ct = EnterpriseCustomerInput.CustomerTypeEnum.valueOf("ENTERPRISE");
        input.setCustomerType(ct);
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetail pd = new PersonDetail();
        pd.setFirstName("Maria");
        pd.setLastName("Gomez");
        pd.setBirthDate(LocalDate.of(1985, 5, 5));
        pd.setNationality("Peruano");
        input.setPersonDetail(pd);

        CompanyDetail cd = new CompanyDetail();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        input.setCompanyDetail(cd);

        when(customerService.updateEnterpriseCustomer(eq("69788cac-d3a3-43c1-a36d-0a7ba62aa9de"), any(EnterpriseCustomerInputDTO.class)))
                .thenReturn(Single.just(Boolean.TRUE));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.updateEnterpriseCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.OK, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).updateEnterpriseCustomer(eq("69788cac-d3a3-43c1-a36d-0a7ba62aa9de"), any(EnterpriseCustomerInputDTO.class));
    }

    @Test
    void updateEnterpriseCustomer_badId_returnsBadRequest() {
        // Arrange
        EnterpriseCustomerInput input = new EnterpriseCustomerInput();
        input.setId("   "); // vacio
        input.setIdType("RUC");
        input.setIdNumber("20123456789");
        EnterpriseCustomerInput.CustomerTypeEnum ct = EnterpriseCustomerInput.CustomerTypeEnum.valueOf("ENTERPRISE");
        input.setCustomerType(ct);
        input.setEmail("enterprise@example.com");
        input.setPhone("988888888");

        PersonDetail pd = new PersonDetail();
        pd.setFirstName("Maria");
        pd.setLastName("Gomez");
        pd.setBirthDate(LocalDate.of(1985, 5, 5));
        pd.setNationality("Peruano");
        input.setPersonDetail(pd);

        CompanyDetail cd = new CompanyDetail();
        cd.setRuc("20123456789");
        cd.setCompanyName("ACME S.A.");
        cd.setRegistrationNumber("REG-123");
        cd.setIncorporationDate(LocalDate.of(2010, 6, 1));
        cd.setAuthorizedSigner(Boolean.TRUE);
        input.setCompanyDetail(cd);

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.updateEnterpriseCustomer(Mono.just(input), null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode()))
                .verifyComplete();

        verifyNoInteractions(customerService);
    }


    @Test
    void deleteCustomer_success_returnsOk() {
        // Arrange
        String id = "69788cac-d3a3-43c1-a36d-0a7ba62aa9de";

        when(customerService.deleteCustomer(id)).thenReturn(Completable.complete());

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.deleteCustomer(id, null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.OK, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).deleteCustomer(id);
    }

    @Test
    void deleteCustomer_notFound_returns404() {
        // Arrange
        String id = "idNotExist";
        when(customerService.deleteCustomer(id)).thenReturn(Completable.error(new NotFoundException("no")));

        // Act
        Mono<ResponseEntity<Void>> responseMono = delegate.deleteCustomer(id, null);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(resp -> assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode()))
                .verifyComplete();

        verify(customerService, times(1)).deleteCustomer(id);
    }
}
