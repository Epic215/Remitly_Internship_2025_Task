package com.remitly.Task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitly.Task.controller.SwiftCodeController;
import com.remitly.Task.dto.CountrySwiftCodesDTO;
import com.remitly.Task.dto.DetailsDTO;
import com.remitly.Task.dto.RequestDTO;
import com.remitly.Task.dto.SwiftCodeDTO;
import com.remitly.Task.model.SwiftCode;
import com.remitly.Task.repository.SwiftCodeRepository;
import com.remitly.Task.service.SwiftCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = SwiftCodeController.class)
public class SwiftCodeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SwiftCodeService swiftCodeService;


    @Test
    void shouldReturnSwiftCodeDetails() throws Exception {

        DetailsDTO swiftCodeH = new DetailsDTO("Adress 1234","BANK NAME", "PL", "POLAND", true, "ABCDEFGHXXX",null);

        when(swiftCodeService.getSwiftCodes("ABCDEFGHXXX")).thenReturn(swiftCodeH);
        ResultActions result = mockMvc.perform(get("/v1/swift-codes/{swift-code}","ABCDEFGHXXX" ));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("ABCDEFGHXXX"))
                .andExpect(jsonPath("$.bankName").value("BANK NAME"))
                .andExpect(jsonPath("$.countryName").value("POLAND"))
                .andExpect(jsonPath("$.countryISO2").value("PL"))
                .andExpect(jsonPath("$.isHeadquarter").value(true)) ;

    }

    @Test
    void shouldReturnCountrySwiftCodesDTO() throws Exception {

        SwiftCodeDTO swiftCodeH = new SwiftCodeDTO("Adress 1234","BANK NAME", "PL", true, "ABCDEFGHXXX");
        SwiftCodeDTO swiftCodeB = new SwiftCodeDTO("Adress 4567","NAME BANK", "PL",  false, "ABCDEFGH123");
        CountrySwiftCodesDTO countryDTO = new CountrySwiftCodesDTO("PL", "POLAND", List.of(swiftCodeH,swiftCodeB));
        when(swiftCodeService.getSwiftCodesCountry("PL")).thenReturn(countryDTO);
        ResultActions result = mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}","PL" ));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.countryName").value("POLAND"))
                .andExpect(jsonPath("$.countryISO2").value("PL"))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("ABCDEFGHXXX"))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("ABCDEFGH123")) ;

    }
    @Test
    void shouldAddNewSwiftCode() throws Exception {

        RequestDTO swiftCode = new RequestDTO("Adress 1234","BANK NAME", "PL", "POLAND",false, "ABCDEFGH345");


        ResultActions result = mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(swiftCode)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully added a new SWIFT code entry"));


    }
    @Test
    void shouldDeleteSwiftCode() throws Exception {

        ResultActions result = mockMvc.perform(delete("/v1/swift-codes/{swift-code}","ABCDEFGH345"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully deleted a SWIFT code"));




    }
    @Test
    void shouldAddNewSwiftCode_shouldThrowException_checkIfValidationWorks() throws Exception {

        RequestDTO swiftCode = new RequestDTO("Adress 1234","BANK NAME", "PLL", "POLAND",false, "ABCDEFGH345");


        ResultActions result = mockMvc.perform(post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(swiftCode)));

        result.andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("countryISO2: Country ISO2 code should be 2 letters long, "));



    }
}
