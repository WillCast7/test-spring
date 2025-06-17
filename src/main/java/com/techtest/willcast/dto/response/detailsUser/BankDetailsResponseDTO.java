package com.techtest.willcast.dto.response.detailsUser;

import lombok.Data;

/**
 * Autor: William Castaño ;)
 * Fecha: 17/06/2025
 * Descripción:
 */

@Data
public class BankDetailsResponseDTO {
    private String cardExpire;
    private String cardNumber;
    private String cardType;
    private String currency;
    private String iban;
}
