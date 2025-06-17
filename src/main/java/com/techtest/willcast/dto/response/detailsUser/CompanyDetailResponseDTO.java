package com.techtest.willcast.dto.response.detailsUser;

import lombok.Data;

/**
 * Autor: William Castaño ;)
 * Fecha: 17/06/2025
 * Descripción:
 */

@Data
public class CompanyDetailResponseDTO {
    private String department;
    private String name;
    private String title;
    private AddressDetailResponseDTO address;
    private String country;
}
