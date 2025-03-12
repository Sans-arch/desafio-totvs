package com.github.sansarch.desafio_totvs.infrastructure.http.controller;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableOutputDto;
import com.github.sansarch.desafio_totvs.application.service.PayableService;
import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payables")
@RequiredArgsConstructor
public class PayableController {

    private final PayableService payableService;

    @PostMapping
    public ResponseEntity<CreatePayableOutputDto> createPayable(@RequestBody CreatePayableInputDto dto) {
        var createdPayable = payableService.createPayable(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayable);
    }
}
