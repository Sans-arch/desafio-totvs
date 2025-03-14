package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.dto.TotalPaidOutputDto;
import com.github.sansarch.desafio_totvs.application.dto.UpdatePayableInputDto;
import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableAlreadyPaidException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableNotFoundException;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.PayableRepository;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayableServiceTest {

    @Mock
    private PayableRepository payableRepository;

    @InjectMocks
    private PayableService payableService;

    private final UUID payableId = UUID.randomUUID();
    private final LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
    private final BigDecimal value = BigDecimal.valueOf(100);
    private final String description = "Test payable";

    @Test
    void shouldCreatePayable() {
        CreatePayableInputDto dto = new CreatePayableInputDto(dueDate, value, description);
        PayableModel payableModel = new PayableModel(payableId, dueDate, null, value, description, PayableStatus.PENDING);
        when(payableRepository.save(any())).thenReturn(payableModel);

        Payable result = payableService.createPayable(dto);

        assertEquals(description, result.getDescription());
        assertEquals(value, result.getValue());
        assertEquals(dueDate, result.getDueDate());
        assertEquals(PayableStatus.PENDING, result.getStatus());
    }

    @Test
    void shouldPayPayable() {
        PayableModel payableModel = new PayableModel(payableId, dueDate, null, value, description, PayableStatus.PENDING);
        when(payableRepository.findById(payableId)).thenReturn(Optional.of(payableModel));

        payableService.payPayable(payableId);

        verify(payableRepository).save(argThat(saved ->
                saved.getStatus() == PayableStatus.PAID && saved.getPaymentDate() != null
        ));
    }

    @Test
    void shouldThrowExceptionWhenPayingNonExistentPayable() {
        when(payableRepository.findById(payableId)).thenReturn(Optional.empty());

        assertThrows(PayableNotFoundException.class, () ->
                payableService.payPayable(payableId)
        );
    }

    @Test
    void shouldCalculateTotalPaid() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(30);
        List<PayableModel> paidPayables = List.of(
                new PayableModel(UUID.randomUUID(), dueDate, null, BigDecimal.valueOf(100), "Test 1", PayableStatus.PAID),
                new PayableModel(UUID.randomUUID(), dueDate, null, BigDecimal.valueOf(200), "Test 2", PayableStatus.PAID)
        );
        when(payableRepository.findAll(any(Specification.class))).thenReturn(paidPayables);

        TotalPaidOutputDto result = payableService.calculateTotalPaid(startDate, endDate);

        assertEquals(BigDecimal.valueOf(300), result.totalPaid());
        assertEquals(startDate, result.startDate());
        assertEquals(endDate, result.endDate());
    }

    @Test
    void shouldProcessCsvFile() throws IOException {
        String csvContent = "description,value,dueDate\nTest,100,2024-03-20T10:00:00";
        MultipartFile file = new MockMultipartFile("file", new ByteArrayInputStream(csvContent.getBytes()));
        when(payableRepository.save(any())).thenReturn(new PayableModel());

        assertDoesNotThrow(() -> payableService.processCsv(file));

        verify(payableRepository).save(any());
    }

    @Test
    void shouldCancelPayable() {
        PayableModel payableModel = new PayableModel(payableId, dueDate, null, value, description, PayableStatus.PENDING);
        when(payableRepository.findById(payableId)).thenReturn(Optional.of(payableModel));

        payableService.cancelPayable(payableId);

        verify(payableRepository).save(argThat(saved ->
                saved.getStatus() == PayableStatus.CANCELED
        ));
    }

    @Test
    void shouldChangePayableContent() {
        PayableModel payableModel = new PayableModel(payableId, dueDate, null, value, description, PayableStatus.PENDING);
        when(payableRepository.findById(payableId)).thenReturn(Optional.of(payableModel));

        LocalDateTime newDueDate = dueDate.plusDays(5);
        BigDecimal newValue = BigDecimal.valueOf(200);
        String newDescription = "Updated description";
        UpdatePayableInputDto dto = new UpdatePayableInputDto(newDueDate, newValue, newDescription);

        Payable result = payableService.changePayableContent(payableId, dto);

        assertEquals(newDueDate, result.getDueDate());
        assertEquals(newValue, result.getValue());
        assertEquals(newDescription, result.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenChangingPaidPayable() {
        PayableModel payableModel = new PayableModel(payableId, dueDate, LocalDateTime.now(), value, description, PayableStatus.PAID);
        when(payableRepository.findById(payableId)).thenReturn(Optional.of(payableModel));

        UpdatePayableInputDto dto = new UpdatePayableInputDto(dueDate.plusDays(1), null, null);

        assertThrows(PayableAlreadyPaidException.class, () ->
                payableService.changePayableContent(payableId, dto)
        );
    }

    @Test
    void shouldFindPayablesWithFilters() {
        PayableModel payableModel = new PayableModel(payableId, dueDate, null, value, description, PayableStatus.PENDING);
        Page<PayableModel> page = new PageImpl<>(List.of(payableModel));
        when(payableRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Payable> result = payableService.findPayables("test", dueDate, pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(description, result.getContent().get(0).getDescription());
    }

    @Test
    void shouldProcessInvalidCsvContent() throws IOException {
        String invalidCsvContent = "description,value,dueDate\nTest,invalid,2024-03-20T10:00:00";
        MultipartFile file = new MockMultipartFile("file", new ByteArrayInputStream(invalidCsvContent.getBytes()));

        assertThrows(NumberFormatException.class, () ->
                payableService.processCsv(file)
        );
    }
}