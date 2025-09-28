package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;

public class CustomerMapper {

    public static CustomerDTO toDto(CustomerEntity e) {
        if (e == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(e.getId());
        dto.setIdType(e.getIdType());
        dto.setIdNumber(e.getIdNumber());
        dto.setCustomerType(CustomerTypeEnum.valueOf(e.getCustomerType()));
        dto.setEmail(e.getEmail());
        dto.setPhone(e.getPhone());
        if (e.getPersonDetail() != null) {
            dto.setPersonDetail(toPersonDetailDto(e.getPersonDetail()));
        }
        if (e.getCompanyDetail() != null) {
            dto.setCompanyDetail(toCompanyDetailDto(e.getCompanyDetail()));
        }
        return dto;
    }

    public static PersonDetailDTO toPersonDetailDto(PersonDetailEntity pd) {
        if (pd == null) return null;
        PersonDetailDTO dto = new PersonDetailDTO();
        dto.setFirstName(pd.getFirstName());
        dto.setLastName(pd.getLastName());
        dto.setBirthDate(pd.getBirthDate());
        dto.setNationality(pd.getNationality());
        return dto;
    }

    public static CompanyDetailDTO toCompanyDetailDto(CompanyDetailEntity cd) {
        if (cd == null) return null;
        CompanyDetailDTO dto = new CompanyDetailDTO();
        dto.setRuc(cd.getRuc());
        dto.setCompanyName(cd.getCompanyName());
        dto.setRegistrationNumber(cd.getRegistrationNumber());
        dto.setIncorporationDate(cd.getIncorporationDate());
        dto.setAuthorizedSigner(cd.getAuthorizedSigner());
        return dto;
    }

    public static CustomerEntity toEntityFromPersonalInput(PersonalCustomerInputDTO in) {
        CustomerEntity e = new CustomerEntity();
        e.setIdType(in.getIdType());
        e.setIdNumber(in.getIdNumber());
        e.setCustomerType(in.getCustomerType());
        e.setEmail(in.getEmail());
        e.setPhone(in.getPhone());
        if (in.getPersonDetail() != null) {
            PersonDetailEntity pd = new PersonDetailEntity();
            pd.setFirstName(in.getPersonDetail().getFirstName());
            pd.setLastName(in.getPersonDetail().getLastName());
            pd.setBirthDate(in.getPersonDetail().getBirthDate());
            pd.setNationality(in.getPersonDetail().getNationality());
            e.setPersonDetail(pd);
        }
        return e;
    }

    public static CustomerEntity toEntityFromEnterpriseInput(EnterpriseCustomerInputDTO in) {
        CustomerEntity e = new CustomerEntity();
        e.setIdType(in.getIdType());
        e.setIdNumber(in.getIdNumber());
        e.setCustomerType(in.getCustomerType());
        e.setEmail(in.getEmail());
        e.setPhone(in.getPhone());
        if (in.getPersonDetailDTO() != null) {
            PersonDetailEntity pd = new PersonDetailEntity();
            pd.setFirstName(in.getPersonDetailDTO().getFirstName());
            pd.setLastName(in.getPersonDetailDTO().getLastName());
            pd.setBirthDate(in.getPersonDetailDTO().getBirthDate());
            pd.setNationality(in.getPersonDetailDTO().getNationality());
            e.setPersonDetail(pd);
        }
        if (in.getCompanyDetail() != null) {
            CompanyDetailEntity cd = new CompanyDetailEntity();
            cd.setRuc(in.getCompanyDetail().getRuc());
            cd.setCompanyName(in.getCompanyDetail().getCompanyName());
            cd.setRegistrationNumber(in.getCompanyDetail().getRegistrationNumber());
            cd.setIncorporationDate(in.getCompanyDetail().getIncorporationDate());
            cd.setAuthorizedSigner(in.getCompanyDetail().getAuthorizedSigner());
            e.setCompanyDetail(cd);
        }
        return e;
    }

    public static void updateEntityFromPersonalInput(CustomerEntity e, PersonalCustomerInputDTO in) {
        e.setIdType(in.getIdType());
        e.setIdNumber(in.getIdNumber());
        e.setCustomerType(in.getCustomerType());
        e.setEmail(in.getEmail());
        e.setPhone(in.getPhone());
        if (in.getPersonDetail() != null) {
            if (e.getPersonDetail() == null) e.setPersonDetail(new PersonDetailEntity());
            e.getPersonDetail().setFirstName(in.getPersonDetail().getFirstName());
            e.getPersonDetail().setLastName(in.getPersonDetail().getLastName());
            e.getPersonDetail().setBirthDate(in.getPersonDetail().getBirthDate());
            e.getPersonDetail().setNationality(in.getPersonDetail().getNationality());
        }
        e.setCompanyDetail(null);
    }

    public static void updateEntityFromEnterpriseInput(CustomerEntity e, EnterpriseCustomerInputDTO in) {
        e.setIdType(in.getIdType());
        e.setIdNumber(in.getIdNumber());
        e.setCustomerType(in.getCustomerType());
        e.setEmail(in.getEmail());
        e.setPhone(in.getPhone());
        if (in.getPersonDetailDTO() != null) {
            if (e.getPersonDetail() == null) e.setPersonDetail(new PersonDetailEntity());
            e.getPersonDetail().setFirstName(in.getPersonDetailDTO().getFirstName());
            e.getPersonDetail().setLastName(in.getPersonDetailDTO().getLastName());
            e.getPersonDetail().setBirthDate(in.getPersonDetailDTO().getBirthDate());
            e.getPersonDetail().setNationality(in.getPersonDetailDTO().getNationality());
        }
        if (in.getCompanyDetail() != null) {
            if (e.getCompanyDetail() == null) e.setCompanyDetail(new CompanyDetailEntity());
            e.getCompanyDetail().setRuc(in.getCompanyDetail().getRuc());
            e.getCompanyDetail().setCompanyName(in.getCompanyDetail().getCompanyName());
            e.getCompanyDetail().setRegistrationNumber(in.getCompanyDetail().getRegistrationNumber());
            e.getCompanyDetail().setIncorporationDate(in.getCompanyDetail().getIncorporationDate());
            e.getCompanyDetail().setAuthorizedSigner(in.getCompanyDetail().getAuthorizedSigner());
        }
    }

    public static Customer toOpenApiCustomer(CustomerDTO dto) {
        Customer c = new Customer();
        c.setId(dto.getId());
        c.setIdType(dto.getIdType());
        c.setIdNumber(dto.getIdNumber());
        Customer.CustomerTypeEnum ct = Customer.CustomerTypeEnum.fromValue(dto.getCustomerType().getValue());
        c.setCustomerType(ct);
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        if (dto.getPersonDetail() != null) {
            PersonDetail pd = new PersonDetail();
            pd.setFirstName(dto.getPersonDetail().getFirstName());
            pd.setLastName(dto.getPersonDetail().getLastName());
            pd.setBirthDate(dto.getPersonDetail().getBirthDate());
            pd.setNationality(dto.getPersonDetail().getNationality());
            c.setPersonDetail(pd);
        }
        if (dto.getCompanyDetail() != null) {
            CompanyDetail cd = new CompanyDetail();
            cd.setRuc(dto.getCompanyDetail().getRuc());
            cd.setCompanyName(dto.getCompanyDetail().getCompanyName());
            cd.setRegistrationNumber(dto.getCompanyDetail().getRegistrationNumber());
            cd.setIncorporationDate(dto.getCompanyDetail().getIncorporationDate());
            cd.setAuthorizedSigner(dto.getCompanyDetail().getAuthorizedSigner());
            c.setCompanyDetail(cd);
        }
        return c;
    }

    // mapear OpenAPI -> DTO para cliente personal
    public static PersonalCustomerInputDTO toPersonalInputDtoFromOpenApiPersonalCustomerInput(PersonalCustomerInput in) {
        if (in == null) return null;
        PersonalCustomerInputDTO dto = new PersonalCustomerInputDTO();
        dto.setId(in.getId());
        dto.setIdType(in.getIdType());
        dto.setIdNumber(in.getIdNumber());
        dto.setCustomerType(in.getCustomerType().getValue());
        dto.setEmail(in.getEmail());
        dto.setPhone(in.getPhone());
        if (in.getPersonDetail() != null) {
            PersonDetailDTO pd = new PersonDetailDTO();
            PersonDetail inPd = in.getPersonDetail();
            pd.setFirstName(inPd.getFirstName());
            pd.setLastName(inPd.getLastName());
            pd.setBirthDate(inPd.getBirthDate());
            pd.setNationality(inPd.getNationality());
            dto.setPersonDetail(pd);
        }
        return dto;
    }
    // mapear OpenAPI -> DTO para cliente empresa
    public static EnterpriseCustomerInputDTO toEnterpriseInputDtoFromOpenApiEnterpriseInputDto(EnterpriseCustomerInput in) {
        if (in == null) return null;
        EnterpriseCustomerInputDTO dto = new EnterpriseCustomerInputDTO();
        dto.setId(in.getId());
        dto.setIdType(in.getIdType());
        dto.setIdNumber(in.getIdNumber());
        dto.setCustomerType(in.getCustomerType().getValue());
        dto.setEmail(in.getEmail());
        dto.setPhone(in.getPhone());
        if (in.getPersonDetail() != null) {
            PersonDetailDTO pd = new PersonDetailDTO();
            PersonDetail inPd = in.getPersonDetail();
            pd.setFirstName(inPd.getFirstName());
            pd.setLastName(inPd.getLastName());
            pd.setBirthDate(inPd.getBirthDate());
            pd.setNationality(inPd.getNationality());
            dto.setPersonDetailDTO(pd);
        }
        if (in.getCompanyDetail() != null) {
            CompanyDetailDTO cd = new CompanyDetailDTO();
            CompanyDetail inCd = in.getCompanyDetail();
            cd.setRuc(inCd.getRuc());
            cd.setCompanyName(inCd.getCompanyName());
            cd.setRegistrationNumber(inCd.getRegistrationNumber());
            cd.setIncorporationDate(inCd.getIncorporationDate());
            cd.setAuthorizedSigner(inCd.getAuthorizedSigner());
            dto.setCompanyDetail(cd);
        }
        return dto;
    }
}
