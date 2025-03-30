package com.remitly.Task;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwiftCodeServiceTests {
    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @InjectMocks
    private SwiftCodeService swiftCodeService;

    private SwiftCode swiftCodeH;
    private SwiftCode swiftCodeB1;
    private SwiftCode swiftCodeB2;

    @BeforeEach
    void setUp() {

        swiftCodeH = new SwiftCode("ABCDEFGHXXX","BANK NAME", "PL", "POLAND", "Adress 1234", true);
        swiftCodeB1 = new SwiftCode("ABCDEFGH123","BANK NAME", "PL", "POLAND", "Adress 456", false);
        swiftCodeB2 = new SwiftCode("ABCDEFGH456","BANK NAME", "PL", "POLAND", "Adress 789", false);
    }
    @Test
    void shouldReturnSwiftCodeOfBranch() {
        when(swiftCodeRepository.existsById("ABCDEFGH456")).thenReturn(true);
        when(swiftCodeRepository.getReferenceById("ABCDEFGH456")).thenReturn(swiftCodeB2);

        DetailsDTO detailsDTO = swiftCodeService.getSwiftCodes("ABCDEFGH456");

        assertEquals("ABCDEFGH456", detailsDTO.swiftCode());
        assertEquals("BANK NAME", detailsDTO.bankName());
        assertEquals("POLAND", detailsDTO.countryName());
        assertFalse(detailsDTO.isHeadquarter());
        assertNull(detailsDTO.branches());

    }
    @Test
    void shouldReturnSwiftCodeOfHeadquarter() {
        when(swiftCodeRepository.existsById("ABCDEFGHXXX")).thenReturn(true);
        when(swiftCodeRepository.getReferenceById("ABCDEFGHXXX")).thenReturn(swiftCodeH);
        when(swiftCodeRepository.findAllBySwiftCodeStartingWith("ABCDEFGH")).thenReturn(List.of(swiftCodeB1,swiftCodeB2));

        DetailsDTO detailsDTO = swiftCodeService.getSwiftCodes("ABCDEFGHXXX");

        assertEquals("ABCDEFGHXXX", detailsDTO.swiftCode());
        assertEquals("BANK NAME", detailsDTO.bankName());
        assertEquals("POLAND", detailsDTO.countryName());
        assertTrue(detailsDTO.isHeadquarter());
        assertEquals(2, detailsDTO.branches().size());

    }
    @Test
    void shouldNotReturnSwiftCode_shouldThrowException() {
        when(swiftCodeRepository.existsById("ABCDEFGHERR")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                swiftCodeService.getSwiftCodes("ABCDEFGHERR"));

        assertEquals("Swift Code does not exist", exception.getMessage());

    }

    @Test
    void shouldReturnEverySwiftCodeFromSpecificCountry() {
        when(swiftCodeRepository.findAllByCountryISO2("PL")).thenReturn(List.of(swiftCodeH,swiftCodeB1,swiftCodeB2));

        CountrySwiftCodesDTO countrySwiftCodesDTO = swiftCodeService.getSwiftCodesCountry("PL");

        assertEquals("POLAND",countrySwiftCodesDTO.countryName());
        assertEquals("PL", countrySwiftCodesDTO.countryISO2());
        assertEquals(3, countrySwiftCodesDTO.swiftCodes().size());

    }
    @Test
    void shouldNotReturnSwiftCodeFromSpecificCountry_shouldThrowException() {
        when(swiftCodeRepository.findAllByCountryISO2("AL")).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                swiftCodeService.getSwiftCodesCountry("AL"));

        assertEquals("Country with this code does not exist", exception.getMessage());

    }
    @Test
    void testAddingNewSwiftCode() {
        RequestDTO swiftCodeDTO = new RequestDTO("Adress 1234", "Bank Name", "PL", "POLAND", true, "ABCDEFGHXXX");


        when(swiftCodeRepository.existsById("ABCDEFGHXXX")).thenReturn(false);

        swiftCodeService.addNewSWIFT(swiftCodeDTO);

        verify(swiftCodeRepository,times(1)).save(any());

    }

    @Test
    void testAddingNewSwiftCode_shouldThrowException_isHeadquarterFieldFalse() {
        RequestDTO swiftCodeDTO = new RequestDTO("Adress 1234", "Bank Name", "PL", "POLAND", false, "ABCDEFGHXXX");


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                swiftCodeService.addNewSWIFT(swiftCodeDTO));

        assertEquals("Field isHeadquarter is incorrect to swiftCode field", exception.getMessage());

    }
    @Test
    void testAddingNewSwiftCode_shouldThrowException_swiftCodeExists() {
        RequestDTO swiftCodeDTO = new RequestDTO("Adress 1234", "Bank Name", "PL", "POLAND", true, "ABCDEFGHXXX");

        when(swiftCodeRepository.existsById("ABCDEFGHXXX")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                swiftCodeService.addNewSWIFT(swiftCodeDTO));

        assertEquals("Swift Code already exists", exception.getMessage());

    }
    @Test
    void testDeletingSwiftCode() {
        when(swiftCodeRepository.existsById("ABCDEFGHXXX")).thenReturn(true);

        swiftCodeService.deleteSWIFT("ABCDEFGHXXX");

        verify(swiftCodeRepository,times(1)).deleteById(any());

    }
    @Test
    void testDeletingSwiftCode_shouldThrowException() {
        when(swiftCodeRepository.existsById("ABCDEFGHXXX")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                swiftCodeService.deleteSWIFT("ABCDEFGHXXX"));

        assertEquals("Swift Code does not exist", exception.getMessage());

    }



}
