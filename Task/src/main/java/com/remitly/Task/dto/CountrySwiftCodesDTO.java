package com.remitly.Task.dto;

import java.util.List;

public record CountrySwiftCodesDTO(
        String countryISO2,
        String countryName,
        List<SwiftCodeDTO> swiftCodes
){
}
