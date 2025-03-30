package com.remitly.Task.service;

import com.remitly.Task.dto.CountrySwiftCodesDTO;
import com.remitly.Task.dto.DetailsDTO;
import com.remitly.Task.dto.RequestDTO;
import com.remitly.Task.dto.SwiftCodeDTO;
import com.remitly.Task.model.SwiftCode;
import com.remitly.Task.repository.SwiftCodeRepository;
import dataCSVParser.DataCSVParser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class SwiftCodeService {

    private final SwiftCodeRepository swiftCodeRepository;

    @Autowired
    public SwiftCodeService(SwiftCodeRepository repository) {
        this.swiftCodeRepository = repository;
    }

    private List<SwiftCodeDTO> getBranches(List<SwiftCode> branches, boolean wantHQ) {


        return branches.stream()
                .filter(branch -> !branch.isHQ() || wantHQ)
                .map(branch -> new SwiftCodeDTO(
                        branch.getAddress(),
                        branch.getBankName(),
                        branch.getCountryISO2(),
                        branch.isHQ(),
                        branch.getSwiftCode()
                )).toList();
    }

    public DetailsDTO getSwiftCodes(String swiftCode) {
        if (swiftCodeRepository.existsById(swiftCode)) {
            SwiftCode swiftCodeData = swiftCodeRepository.getReferenceById(swiftCode);

            List<SwiftCodeDTO> branches = null;

            if (swiftCodeData.isHQ()) {
                branches = getBranches(swiftCodeRepository.findAllBySwiftCodeStartingWith(swiftCode.substring(0,8)),false);
            }
            return new DetailsDTO(swiftCodeData.getAddress(),
                    swiftCodeData.getBankName(),
                    swiftCodeData.getCountryISO2(),
                    swiftCodeData.getCountryName(),
                    swiftCodeData.isHQ(),
                    swiftCodeData.getSwiftCode(),
                    branches);
        }
        else{
            throw new IllegalArgumentException("Swift Code does not exist");
        }

    }
    public CountrySwiftCodesDTO getSwiftCodesCountry(String countryISO2code) {
        List<SwiftCode> swiftCodes = swiftCodeRepository.findAllByCountryISO2(countryISO2code);
        if (!swiftCodes.isEmpty()) {

            return new CountrySwiftCodesDTO(countryISO2code, swiftCodes.getFirst().getCountryName(),
                    getBranches(swiftCodes, true));
        }
        else{
            throw new IllegalArgumentException("Country with this code does not exist");
        }

    }

    public void addNewSWIFT(RequestDTO swiftCodeDTO) {
        SwiftCode swiftCode = new SwiftCode(swiftCodeDTO.swiftCode().toUpperCase(), swiftCodeDTO.bankName(),swiftCodeDTO.countryISO2(),
                                swiftCodeDTO.countryName(), swiftCodeDTO.address(), swiftCodeDTO.isHeadquarter());

        if (swiftCode.isHQ() != swiftCode.getSwiftCode().endsWith("XXX")){
            throw new IllegalArgumentException("Field isHeadquarter is incorrect to swiftCode field");
        }
        if (!swiftCodeRepository.existsById(swiftCode.getSwiftCode())) {
            swiftCodeRepository.save(swiftCode);
        }
        else{
            throw new IllegalArgumentException("Swift Code already exists");
        }

    }
    public void deleteSWIFT(String swiftCode) {
        if (swiftCodeRepository.existsById(swiftCode)) {
            swiftCodeRepository.deleteById(swiftCode);
        }
        else{
            throw new IllegalArgumentException("Swift Code does not exist");
        }
    }




}
