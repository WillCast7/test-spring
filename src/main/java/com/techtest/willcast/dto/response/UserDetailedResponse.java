package com.techtest.willcast.dto.response;

import com.techtest.willcast.dto.response.detailsUser.AddressDetailResponseDTO;
import com.techtest.willcast.dto.response.detailsUser.BankDetailsResponseDTO;
import com.techtest.willcast.dto.response.detailsUser.CompanyDetailResponseDTO;
import com.techtest.willcast.dto.response.detailsUser.hairDetailResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDetailedResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String maidenName;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String birthDate;
    private String image;
    private String bloodGroup;
    private double height;
    private double weight;
    private String eyeColor;
    private hairDetailResponseDTO hair;
    private String ip;
    private AddressDetailResponseDTO address;
    private String macAddress;
    private String university;
    private BankDetailsResponseDTO bank;
    private CompanyDetailResponseDTO company;
    private String department;
    private String name;
    private String title;
    private AddressDetailResponseDTO getAddress;
    private String ein;
    private String ssn;
    private String userAgent;
    private CompanyDetailResponseDTO crypto;
    private String role;
}
