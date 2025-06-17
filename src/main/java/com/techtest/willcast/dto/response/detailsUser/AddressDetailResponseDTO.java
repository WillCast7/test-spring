package com.techtest.willcast.dto.response.detailsUser;

import lombok.Data;


/**
 * Autor: William Castaño ;)
 * Fecha: 17/06/2025
 * Descripción:
 */

@Data
public class AddressDetailResponseDTO {
    private String address;
    private String city;
    private String state;
    private String stateCode;
    private String postalCode;
    private CoordinatesDetailResponseDTO coordinates;
    private String country;
}
