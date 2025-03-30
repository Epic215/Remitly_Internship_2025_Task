package com.remitly.Task.service;

import com.remitly.Task.model.SwiftCode;
import com.remitly.Task.repository.SwiftCodeRepository;
import com.remitly.Task.dataCSVParser.DataCSVParser;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;


import java.util.ArrayList;

@Configuration
public class SwiftCodeDataLoader  {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftCodeDataLoader(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }
    @PostConstruct
    public void initializeDataInDatabase() {
        if (swiftCodeRepository.count() == 0) {
            String csvData = "Interns_2025_SWIFT_CODES.csv";
            ArrayList<SwiftCode> swiftCodes = DataCSVParser.csvToSwiftCode(csvData);
            if (!swiftCodes.isEmpty()) {
                swiftCodeRepository.saveAll(swiftCodes);
                System.out.println("Successfully loaded " + swiftCodes.size() + " csv data");
            }
            else{
                System.out.println("No swift codes data found");
            }

        }

    }



}
