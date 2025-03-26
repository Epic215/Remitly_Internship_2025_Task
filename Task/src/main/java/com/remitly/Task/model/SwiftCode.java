package com.remitly.Task.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "swift_codes_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCode {
    @Id
    private String swiftCode;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private String address;
    private boolean isHQ;

}
