package com.remitly.Task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record DetailsDTO(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<SwiftCodeDTO> branches
) {
}
