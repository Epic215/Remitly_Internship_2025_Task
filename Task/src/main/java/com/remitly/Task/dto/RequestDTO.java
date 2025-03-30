package com.remitly.Task.dto;

import jakarta.validation.constraints.*;

public record RequestDTO(
        @NotBlank(message = "Address cannot be blank")
        String address,

        @NotBlank(message = "Bank name cannot be blank")
        String bankName,

        @NotBlank(message = "Country ISO2 code cannot be blank")
        @Size(min = 2, max = 2, message = "Country ISO2 code should be 2 letters long")
        String countryISO2,

        @NotBlank(message = "Country name cannot be blank")
        String countryName,

        @NotNull(message = "Country name cannot be null")
        boolean isHeadquarter,

        @NotNull(message = "SWIFT code cannot be null")
        @Size(min = 8, max = 11, message = "SWIFT code should be between 8 and 11 characters long")
        String swiftCode

) {
}
