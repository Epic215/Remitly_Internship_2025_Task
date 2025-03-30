package com.remitly.Task.dataCSVParser;

import com.remitly.Task.model.SwiftCode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;


public class DataCSVParser {
    public static ArrayList<SwiftCode> csvToSwiftCode(String csvFileName) {
        ArrayList<SwiftCode> swiftCodes = new ArrayList<>();

        try{
            ClassPathResource resource = new ClassPathResource(csvFileName);
            try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))){

                 CSVParser csvParser = CSVFormat.DEFAULT.builder()
                         .setHeader()
                         .setSkipHeaderRecord(true)
                         .build().parse(reader);

                for (CSVRecord record : csvParser) {
                    String swiftCode = record.get("SWIFT CODE");
                    String countryISO2 = record.get("COUNTRY ISO2 CODE").toUpperCase();
                    String name = record.get("NAME");
                    String address = record.get("ADDRESS");
                    String countryName = record.get("COUNTRY NAME").toUpperCase();
                    boolean isHQ = swiftCode.endsWith("XXX");


                    swiftCodes.add(new SwiftCode(swiftCode, name, countryISO2, countryName,address,   isHQ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file from resources: " + e.getMessage());
        }
        return swiftCodes;


    };

}
