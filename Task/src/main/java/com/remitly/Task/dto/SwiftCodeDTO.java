package com.remitly.Task.dto;

public record SwiftCodeDTO(
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
) {
}
