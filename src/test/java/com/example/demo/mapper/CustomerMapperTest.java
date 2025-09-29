package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    @Test
    void toDto_nullInput_returnsNull() {
        assertNull(CustomerMapper.toDto(null));
    }

    @Test
    void toDto_fullEntity_mapsAllFields() {
        CustomerEntity e = new CustomerEntity();
        e.setId("51958d34-1bc0-4034-90bf-f2b80e8969e4");
        e.setIdType("DNI");
        e.setIdNumber("12345678");
        e.setCustomerType("PERSONAL");
        e.setEmail("johnsanchez@example.com");
        e.setPhone("983475683");

        PersonDetailEntity pd = new PersonDetailEntity();
        pd.setFirstName("John");
        pd.setLastName("Sanchez");
        pd.setBirthDate(LocalDate.of(1990, 1, 1));
        pd.setNationality("Peruano");
        e.setPersonDetail(pd);

        CompanyDetailEntity cd = new CompanyDetailEntity();
        cd.setRuc("1023456789");
        cd.setCompanyName("Acme S.A.");
        cd.setRegistrationNumber("100000");
        cd.setIncorporationDate(LocalDate.of(2000, 6, 15));
        cd.setAuthorizedSigner(false);
        e.setCompanyDetail(cd);

        CustomerDTO dto = CustomerMapper.toDto(e);

        assertNotNull(dto);
        assertEquals("51958d34-1bc0-4034-90bf-f2b80e8969e4", dto.getId());
        assertEquals("DNI", dto.getIdType());
        assertEquals("12345678", dto.getIdNumber());
        assertEquals("PERSONAL", dto.getCustomerType().getValue());
        assertEquals("johnsanchez@example.com", dto.getEmail());
        assertEquals("983475683", dto.getPhone());

        assertNotNull(dto.getPersonDetail());
        assertEquals("John", dto.getPersonDetail().getFirstName());
        assertEquals("Sanchez", dto.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getPersonDetail().getBirthDate());
        assertEquals("Peruano", dto.getPersonDetail().getNationality());

        assertNotNull(dto.getCompanyDetail());
        assertEquals("1023456789", dto.getCompanyDetail().getRuc());
        assertEquals("Acme S.A.", dto.getCompanyDetail().getCompanyName());
        assertEquals("100000", dto.getCompanyDetail().getRegistrationNumber());
        assertEquals(LocalDate.of(2000, 6, 15), dto.getCompanyDetail().getIncorporationDate());
        assertEquals(false, dto.getCompanyDetail().getAuthorizedSigner());
    }

    @Test
    void toPersonDetailDto_null_returnsNull() {
        assertNull(CustomerMapper.toPersonDetailDto(null));
    }

    @Test
    void toPersonDetailDto_mapsFields() {
        PersonDetailEntity pd = new PersonDetailEntity();
        pd.setFirstName("Ana");
        pd.setLastName("Perez");
        pd.setBirthDate(LocalDate.of(1985, 5, 20));
        pd.setNationality("Argentino");

        PersonDetailDTO dto = CustomerMapper.toPersonDetailDto(pd);

        assertNotNull(dto);
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Perez", dto.getLastName());
        assertEquals(LocalDate.of(1985, 5, 20), dto.getBirthDate());
        assertEquals("Argentino", dto.getNationality());
    }

    @Test
    void toCompanyDetailDto_null_returnsNull() {
        assertNull(CustomerMapper.toCompanyDetailDto(null));
    }

    @Test
    void toCompanyDetailDto_mapsFields() {
        CompanyDetailEntity cd = new CompanyDetailEntity();
        cd.setRuc("2045634782");
        cd.setCompanyName("AliCorp");
        cd.setRegistrationNumber("1000001");
        cd.setIncorporationDate(LocalDate.of(2010, 2, 2));
        cd.setAuthorizedSigner(false);

        CompanyDetailDTO dto = CustomerMapper.toCompanyDetailDto(cd);

        assertNotNull(dto);
        assertEquals("2045634782", dto.getRuc());
        assertEquals("AliCorp", dto.getCompanyName());
        assertEquals("1000001", dto.getRegistrationNumber());
        assertEquals(LocalDate.of(2010, 2, 2), dto.getIncorporationDate());
        assertEquals(false, dto.getAuthorizedSigner());
    }

    @Test
    void toEntityFromtoEntityFromPersonalCustomerInputDTO_mapsFields() {
        PersonalCustomerInputDTO in = new PersonalCustomerInputDTO();
        in.setIdType("DNI");
        in.setIdNumber("79345678");
        in.setCustomerType("PERSONAL");
        in.setEmail("paulagomez@example.com");
        in.setPhone("983475683");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Paula");
        pd.setLastName("Gomez");
        pd.setBirthDate(LocalDate.of(1992, 3, 3));
        pd.setNationality("Peruano");
        in.setPersonDetail(pd);

        CustomerEntity e = CustomerMapper.toEntityFromPersonalCustomerInputDTO(in);

        assertNotNull(e);
        assertEquals("DNI", e.getIdType());
        assertEquals("79345678", e.getIdNumber());
        assertEquals("PERSONAL", e.getCustomerType());
        assertEquals("paulagomez@example.com", e.getEmail());
        assertEquals("983475683", e.getPhone());

        assertNotNull(e.getPersonDetail());
        assertEquals("Paula", e.getPersonDetail().getFirstName());
        assertEquals("Gomez", e.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1992, 3, 3), e.getPersonDetail().getBirthDate());
        assertEquals("Peruano", e.getPersonDetail().getNationality());
    }

    @Test
    void toEntityFromEnterpriseInput_mapsPersonAndCompany() {
        EnterpriseCustomerInputDTO in = new EnterpriseCustomerInputDTO();
        in.setIdType("DNI");
        in.setIdNumber("79345678");
        in.setCustomerType("ENTERPRISE");
        in.setEmail("paolacampos@company.com");
        in.setPhone("983475683");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Paola");
        pd.setLastName("Campos");
        pd.setBirthDate(LocalDate.of(1980, 4, 4));
        pd.setNationality("Peruano");
        in.setPersonDetailDTO(pd);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("2077777777");
        cd.setCompanyName("Pangea Co");
        cd.setRegistrationNumber("100002");
        cd.setIncorporationDate(LocalDate.of(2005, 7, 7));
        cd.setAuthorizedSigner(false);
        in.setCompanyDetail(cd);

        CustomerEntity e = CustomerMapper.toEntityFromEnterpriseInput(in);

        assertNotNull(e);
        assertEquals("DNI", e.getIdType());
        assertEquals("79345678", e.getIdNumber());
        assertEquals("ENTERPRISE", e.getCustomerType());
        assertEquals("paolacampos@company.com", e.getEmail());
        assertEquals("983475683", e.getPhone());

        assertNotNull(e.getPersonDetail());
        assertEquals("Paola", e.getPersonDetail().getFirstName());
        assertEquals("Campos", e.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1980, 4, 4), e.getPersonDetail().getBirthDate());
        assertEquals("Peruano", e.getPersonDetail().getNationality());

        assertNotNull(e.getCompanyDetail());
        assertEquals("2077777777", e.getCompanyDetail().getRuc());
        assertEquals("Pangea Co", e.getCompanyDetail().getCompanyName());
        assertEquals(LocalDate.of(2005, 7, 7), e.getCompanyDetail().getIncorporationDate());
        assertEquals(false, e.getCompanyDetail().getAuthorizedSigner());
    }

    @Test
    void updateEntityFromPersonalInput_updatesAndNullifiesCompany() {
        CustomerEntity e = new CustomerEntity();
        CompanyDetailEntity cd = new CompanyDetailEntity();
        cd.setCompanyName("ABC"); // solo para validar companyDetail sea nulo en los assert
        e.setCompanyDetail(cd);

        PersonalCustomerInputDTO in = new PersonalCustomerInputDTO();
        in.setIdType("DNI");
        in.setIdNumber("79345679");
        in.setCustomerType("PERSONAL");
        in.setEmail("gersoncampos@example.com");
        in.setPhone("983475683");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Gerson");
        pd.setLastName("Campos");
        pd.setBirthDate(LocalDate.of(1999, 9, 9));
        pd.setNationality("Peruano");
        in.setPersonDetail(pd);

        CustomerMapper.updateEntityFromPersonalInput(e, in);

        assertEquals("DNI", e.getIdType());
        assertEquals("79345679", e.getIdNumber());
        assertEquals("PERSONAL", e.getCustomerType());
        assertEquals("gersoncampos@example.com", e.getEmail());
        assertEquals("983475683", e.getPhone());

        assertNotNull(e.getPersonDetail());
        assertEquals("Gerson", e.getPersonDetail().getFirstName());
        assertEquals("Campos", e.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1999, 9, 9), e.getPersonDetail().getBirthDate());
        assertEquals("Peruano", e.getPersonDetail().getNationality());

        assertNull(e.getCompanyDetail()); // valida que sea null
    }

    @Test
    void updateEntityFromEnterpriseInput_createsAndUpdatesDetails() {
        CustomerEntity e = new CustomerEntity(); // initially empty

        EnterpriseCustomerInputDTO in = new EnterpriseCustomerInputDTO();
        in.setIdType("DNI");
        in.setIdNumber("79345678");
        in.setCustomerType("ENTERPRISE");
        in.setEmail("paolacampos@company.com");
        in.setPhone("983475683");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Paola");
        pd.setLastName("Campos");
        pd.setBirthDate(LocalDate.of(1980, 4, 4));
        pd.setNationality("Peruano");
        in.setPersonDetailDTO(pd);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("2045634782");
        cd.setCompanyName("AliCorp");
        cd.setRegistrationNumber("1000001");
        cd.setIncorporationDate(LocalDate.of(2010, 2, 2));
        cd.setAuthorizedSigner(false);
        in.setCompanyDetail(cd);

        CustomerMapper.updateEntityFromEnterpriseInput(e, in);

        assertEquals("DNI", e.getIdType());
        assertEquals("79345678", e.getIdNumber());
        assertEquals("ENTERPRISE", e.getCustomerType());
        assertEquals("paolacampos@company.com", e.getEmail());
        assertEquals("983475683", e.getPhone());

        assertNotNull(e.getPersonDetail());
        assertEquals("Paola", e.getPersonDetail().getFirstName());
        assertEquals("Campos", e.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1980, 4, 4), e.getPersonDetail().getBirthDate());
        assertEquals("Peruano", e.getPersonDetail().getNationality());

        assertNotNull(e.getCompanyDetail());
        assertEquals("2045634782", e.getCompanyDetail().getRuc());
        assertEquals("AliCorp", e.getCompanyDetail().getCompanyName());
        assertEquals("1000001", e.getCompanyDetail().getRegistrationNumber());
        assertEquals(LocalDate.of(2010, 2, 2), e.getCompanyDetail().getIncorporationDate());
        assertEquals(false, e.getCompanyDetail().getAuthorizedSigner());
    }

    @Test
    void toOpenApiCustomer_mapsDtoToOpenApiModel() {
        CustomerDTO dto = new CustomerDTO();

        dto.setIdType("DNI");
        dto.setIdNumber("79345678");
        CustomerTypeEnum typeEnum = CustomerTypeEnum.valueOf("ENTERPRISE");
        dto.setCustomerType(typeEnum);
        dto.setEmail("paolacampos@company.com");
        dto.setPhone("983475683");

        PersonDetailDTO pd = new PersonDetailDTO();
        pd.setFirstName("Paola");
        pd.setLastName("Campos");
        pd.setBirthDate(LocalDate.of(1980, 4, 4));
        pd.setNationality("Peruano");
        dto.setPersonDetail(pd);

        CompanyDetailDTO cd = new CompanyDetailDTO();
        cd.setRuc("2045634782");
        cd.setCompanyName("AliCorp");
        cd.setRegistrationNumber("1000001");
        cd.setIncorporationDate(LocalDate.of(2010, 2, 2));
        cd.setAuthorizedSigner(false);
        dto.setCompanyDetail(cd);

        Customer c = CustomerMapper.toOpenApiCustomer(dto);

        assertNotNull(c);
        assertEquals("DNI", c.getIdType());
        assertEquals("79345678", c.getIdNumber());
        assertEquals("ENTERPRISE", c.getCustomerType().getValue());
        assertEquals("paolacampos@company.com", c.getEmail());
        assertEquals("983475683", c.getPhone());

        assertNotNull(c.getPersonDetail());
        assertEquals("Paola", c.getPersonDetail().getFirstName());
        assertEquals("Campos", c.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1980, 4, 4), c.getPersonDetail().getBirthDate());
        assertEquals("Peruano", c.getPersonDetail().getNationality());

        assertNotNull(c.getCompanyDetail());
        assertEquals("2045634782", c.getCompanyDetail().getRuc());
        assertEquals("AliCorp", c.getCompanyDetail().getCompanyName());
        assertEquals("1000001", c.getCompanyDetail().getRegistrationNumber());
        assertEquals(LocalDate.of(2010, 2, 2), c.getCompanyDetail().getIncorporationDate());
        assertEquals(false, c.getCompanyDetail().getAuthorizedSigner());
    }

    @Test
    void toPersonalInputDtoFromOpenApiPersonalCustomerInput_mapsAndHandlesNull() {

        PersonalCustomerInput in = new PersonalCustomerInput();
        in.setId("39aafcec-68bc-42c3-b84b-50330a20f189");
        in.setIdType("DNI");
        in.setIdNumber("79345679");
        PersonalCustomerInput.CustomerTypeEnum typeEnum = PersonalCustomerInput.CustomerTypeEnum.valueOf("PERSONAL");
        in.setCustomerType(typeEnum);
        in.setEmail("gersoncampos@example.com");
        in.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Gerson");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1999, 9, 9));
        inPd.setNationality("Peruano");
        in.setPersonDetail(inPd);

        PersonalCustomerInputDTO dto = CustomerMapper.toPersonalInputDtoFromOpenApiPersonalCustomerInput(in);

        assertNull(CustomerMapper.toPersonalInputDtoFromOpenApiPersonalCustomerInput(null));

        assertNotNull(dto);
        assertEquals("39aafcec-68bc-42c3-b84b-50330a20f189", dto.getId());
        assertEquals("DNI", dto.getIdType());
        assertEquals("79345679", dto.getIdNumber());
        assertEquals("gersoncampos@example.com", dto.getEmail());
        assertEquals("PERSONAL", dto.getCustomerType());
        assertEquals("983475683", dto.getPhone());

        assertNotNull(dto.getPersonDetail());
        assertEquals("Gerson", dto.getPersonDetail().getFirstName());
        assertEquals("Campos", dto.getPersonDetail().getLastName());
        assertEquals(LocalDate.of(1999, 9, 9), dto.getPersonDetail().getBirthDate());
        assertEquals("Peruano", dto.getPersonDetail().getNationality());
    }

    @Test
    void toEnterpriseInputDtoFromOpenApiEnterpriseInputDto_mapsAndHandlesNull() {
        assertNull(CustomerMapper.toEnterpriseInputDtoFromOpenApiEnterpriseInputDto(null));

        EnterpriseCustomerInput in = new EnterpriseCustomerInput();
        in.setId("eba4a107-a0c6-43ca-be33-744cb2351a2e");
        in.setIdType("DNI");
        in.setIdNumber("79345678");
        EnterpriseCustomerInput.CustomerTypeEnum typeEnum = EnterpriseCustomerInput.CustomerTypeEnum.valueOf("ENTERPRISE");
        in.setCustomerType(typeEnum);
        in.setEmail("paolacampos@example.com");
        in.setPhone("983475683");

        PersonDetail inPd = new PersonDetail();
        inPd.setFirstName("Paola");
        inPd.setLastName("Campos");
        inPd.setBirthDate(LocalDate.of(1980, 4, 4));
        inPd.setNationality("Peruano");
        in.setPersonDetail(inPd);

        CompanyDetail inCd = new CompanyDetail();
        inCd.setRuc("2045634782");
        inCd.setCompanyName("AliCorp");
        inCd.setRegistrationNumber("1000001");
        inCd.setIncorporationDate(LocalDate.of(2010, 2, 2));
        inCd.setAuthorizedSigner(false);
        in.setCompanyDetail(inCd);

        EnterpriseCustomerInputDTO dto = CustomerMapper.toEnterpriseInputDtoFromOpenApiEnterpriseInputDto(in);

        assertNotNull(dto);
        assertEquals("eba4a107-a0c6-43ca-be33-744cb2351a2e", dto.getId());
        assertEquals("DNI", dto.getIdType());
        assertEquals("79345678", dto.getIdNumber());
        assertEquals("ENTERPRISE", dto.getCustomerType());
        assertEquals("paolacampos@example.com", dto.getEmail());
        assertEquals("983475683", dto.getPhone());

        assertNotNull(dto.getPersonDetailDTO());
        assertEquals("Paola", dto.getPersonDetailDTO().getFirstName());
        assertEquals("Campos", dto.getPersonDetailDTO().getLastName());
        assertEquals(LocalDate.of(1980, 4, 4), dto.getPersonDetailDTO().getBirthDate());
        assertEquals("Peruano", dto.getPersonDetailDTO().getNationality());

        assertNotNull(dto.getCompanyDetail());
        assertEquals("2045634782", dto.getCompanyDetail().getRuc());
        assertEquals("AliCorp", dto.getCompanyDetail().getCompanyName());
        assertEquals("1000001", dto.getCompanyDetail().getRegistrationNumber());
        assertEquals(LocalDate.of(2010, 2, 2), dto.getCompanyDetail().getIncorporationDate());
        assertEquals(false, dto.getCompanyDetail().getAuthorizedSigner());
    }
}
