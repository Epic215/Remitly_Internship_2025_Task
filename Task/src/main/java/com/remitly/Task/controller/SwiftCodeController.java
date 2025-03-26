package com.remitly.Task.controller;

import jakarta.validation.Valid;
import com.remitly.Task.dto.*;
import com.remitly.Task.model.SwiftCode;
import com.remitly.Task.service.SwiftCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {
    @Autowired
    private SwiftCodeService swiftCodeService;

    @GetMapping("/{swift-code}")
    public ResponseEntity<DetailsDTO> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        DetailsDTO response = swiftCodeService.getSwiftCodes(swiftCode);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDTO> getSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2code) {
        CountrySwiftCodesDTO response = swiftCodeService.getSwiftCodesCountry(countryISO2code);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> addSwiftCode(@Valid @RequestBody RequestDTO swiftCodeDTO) {
        swiftCodeService.addNewSWIFT(swiftCodeDTO);
        return ResponseEntity.ok(new ResponseDTO("Successfully added a new SWIFT code entry"));

    }
    @DeleteMapping("/{swift-code}")
    public ResponseEntity<ResponseDTO> deleteSwiftCode(@PathVariable("swift-code") String swiftCode) {
        swiftCodeService.deleteSWIFT(swiftCode);
        return ResponseEntity.ok(new ResponseDTO("Successfully deleted a SWIFT code"));
    }

}
