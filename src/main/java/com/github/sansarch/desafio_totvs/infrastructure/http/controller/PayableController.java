package com.github.sansarch.desafio_totvs.infrastructure.http.controller;

import com.github.sansarch.desafio_totvs.application.dto.UpdatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.service.PayableService;
import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.infrastructure.http.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payables")
@RequiredArgsConstructor
public class PayableController {

    private final PayableService payableService;

    @PostMapping
    public ResponseEntity<CreatePayableResponseDto> createPayable(@RequestBody CreatePayableRequestDto dto) {
        var input = new CreatePayableInputDto(dto.dueDate(), dto.value(), dto.description());
        var createdPayable = payableService.createPayable(input);
        var responseDto = new CreatePayableResponseDto(
                createdPayable.getId(),
                createdPayable.getDueDate(),
                createdPayable.getValue(),
                createdPayable.getDescription(),
                createdPayable.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPayableResponseDto> getPayable(@PathVariable UUID id) {
        var payable = payableService.getPayable(id);
        var responseDto = new GetPayableResponseDto(
                payable.getId(),
                payable.getDueDate(),
                payable.getValue(),
                payable.getDescription(),
                payable.getStatus()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdatePayableResponseDto> changePayableContent(@PathVariable UUID id, @RequestBody UpdatePayableRequestDto dto) {
        var input = new UpdatePayableInputDto(dto.dueDate(), dto.value(), dto.description());
        var updatedPayable = payableService.changePayableContent(id, input);
        var responseDto = new UpdatePayableResponseDto(
                updatedPayable.getId(),
                updatedPayable.getDueDate(),
                updatedPayable.getValue(),
                updatedPayable.getDescription(),
                updatedPayable.getStatus()
        );
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> payPayable(@PathVariable UUID id) {
        payableService.payPayable(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPayable(@PathVariable UUID id) {
        payableService.cancelPayable(id);
        return ResponseEntity.noContent().build();
    }
}
