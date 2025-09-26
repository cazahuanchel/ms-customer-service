package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;

import java.util.Date;

public class CustomerMapper {

    public static CustomerDTO toDto(CustomerEntity e) {
        if (e == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(e.getId());
        dto.setIdType(e.getIdType());
        dto.setIdNumber(e.getIdNumber());
        dto.setCustomerType(e.getCustomerType());
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
        // limpiar companyDetail si existiera
        e.setCompanyDetail(null);
    }

    public static void updateEntityFromEnterpriseInput(CustomerEntity e, EnterpriseCustomerInputDTO in) {
        e.setIdType(in.getIdType());
        e.setIdNumber(in.getIdNumber());
        e.setCustomerType(in.getCustomerType());
        e.setEmail(in.getEmail());
        e.setPhone(in.getPhone());
        if (in.getCompanyDetail() != null) {
            if (e.getCompanyDetail() == null) e.setCompanyDetail(new CompanyDetailEntity());
            e.getCompanyDetail().setRuc(in.getCompanyDetail().getRuc());
            e.getCompanyDetail().setCompanyName(in.getCompanyDetail().getCompanyName());
            e.getCompanyDetail().setRegistrationNumber(in.getCompanyDetail().getRegistrationNumber());
            e.getCompanyDetail().setIncorporationDate(in.getCompanyDetail().getIncorporationDate());
            e.getCompanyDetail().setAuthorizedSigner(in.getCompanyDetail().getAuthorizedSigner());
        }
        // limpiar personDetail si existiera
        e.setPersonDetail(null);
    }
}
