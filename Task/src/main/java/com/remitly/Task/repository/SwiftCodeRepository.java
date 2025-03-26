package com.remitly.Task.repository;

import com.remitly.Task.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {

    List<SwiftCode> findAllByCountryISO2(String countryISO2);
    List<SwiftCode> findAllBySwiftCodeStartingWith(String swiftCodePrefix);

}
