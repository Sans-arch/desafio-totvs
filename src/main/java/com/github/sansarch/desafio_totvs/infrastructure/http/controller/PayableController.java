package com.github.sansarch.desafio_totvs.infrastructure.http.controller;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableOutputDto;
import com.github.sansarch.desafio_totvs.application.service.PayableService;
import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
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
    public ResponseEntity<CreatePayableOutputDto> createPayable(@RequestBody CreatePayableInputDto dto) {
        var createdPayable = payableService.createPayable(dto);
        var responseDto = new CreatePayableOutputDto(
                createdPayable.getId(),
                createdPayable.getDueDate(),
                createdPayable.getValue(),
                createdPayable.getDescription(),
                createdPayable.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity getPayable(@RequestParam UUID id) {
        var payable = payableService.getPayable(id);
        return ResponseEntity.ok(payable);
    }
}
