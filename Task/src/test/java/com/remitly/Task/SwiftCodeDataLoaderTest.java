package com.remitly.Task;

import com.remitly.Task.repository.SwiftCodeRepository;
import com.remitly.Task.service.SwiftCodeDataLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwiftCodeDataLoaderTest {


    @Mock
    private SwiftCodeRepository swiftCodeRepository;

    @InjectMocks
    private SwiftCodeDataLoader swiftCodeDataLoader;


    @Test
    public void shouldAddNewSwiftCodeFromCSVWhenRepositoryIsEmpty() {

        when(swiftCodeRepository.count()).thenReturn(0L);

        swiftCodeDataLoader.initializeDataInDatabase();

        verify(swiftCodeRepository, times(1)).saveAll(anyList());



    }
    @Test
    public void shouldNotAddNewSwiftCodeFromCSVWhenRepositoryIsNotEmpty() {

        when(swiftCodeRepository.count()).thenReturn(1061L);

        swiftCodeDataLoader.initializeDataInDatabase();

        verify(swiftCodeRepository, never()).saveAll(anyList());


    }





}
