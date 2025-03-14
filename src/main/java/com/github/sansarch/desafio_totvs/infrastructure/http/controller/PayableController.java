package com.github.sansarch.desafio_totvs.infrastructure.http.controller;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.dto.UpdatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.service.PayableService;
import com.github.sansarch.desafio_totvs.infrastructure.http.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payables")
@RequiredArgsConstructor
@Tag(name = "Payables")
@SecurityRequirement(name = "bearerAuth")
public class PayableController {

    private final PayableService payableService;

    @Operation(
            description = "Create a payable",
            summary = "Create a payable"
    )
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

    @Operation(
            description = "Paginated list payables with optional filters",
            summary = "List payables"
    )
    @GetMapping
    public ResponseEntity<Page<GetPayableResponseDto>> listPayableWithFilter(
            @RequestParam(required = false) String description,
            @RequestParam(name = "due_date", required = false) LocalDateTime dueDate,
            @PageableDefault(size = 20) @ParameterObject Pageable pageable) {
        var payables = payableService.findPayables(description, dueDate, pageable)
                .map(
                payable -> new GetPayableResponseDto(
                        payable.getId(),
                        payable.getDueDate(),
                        payable.getValue(),
                        payable.getDescription(),
                        payable.getStatus()
                ));
        
        return ResponseEntity.ok(payables);
    }

    @Operation(
            description = "Get a payable by id",
            summary = "Get a payable"
    )
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

    @Operation(
            description = "Get total paid in a period",
            summary = "Get total paid"
    )
    @GetMapping("/total-paid")
    public ResponseEntity<TotalPaidResponseDto> getTotalPaid(
            @RequestParam(name = "start_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam (name = "end_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        var totalPaid = payableService.calculateTotalPaid(startDate, endDate);
        var responseDto = new TotalPaidResponseDto(totalPaid.startDate(), totalPaid.endDate(), totalPaid.totalPaid());
        return ResponseEntity.ok(responseDto);
    }

    @Operation(
            description = "Change payable content",
            summary = "Change payable content"
    )
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

    @Operation(
            description = "Pay a payable",
            summary = "Pay a payable"
    )
    @PatchMapping("/{id}/pay")
    public ResponseEntity<Void> payPayable(@PathVariable UUID id) {
        payableService.payPayable(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            description = "Cancel a payable",
            summary = "Cancel a payable",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Payable canceled successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Payable already paid or canceled",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelPayable(@PathVariable UUID id) {
        payableService.cancelPayable(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            description = "Import payables from a CSV file",
            summary = "Import payables"
    )
    @PostMapping(value = "/import", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportationResponseDto> importPayablesFromCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            var responseDto = new ImportationResponseDto("Invalid file. Send a  CSV file");
            return ResponseEntity.badRequest().body(responseDto);
        }

        try {
            payableService.processCsv(file);
            var responseDto = new ImportationResponseDto("Imported successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            var responseDto = new ImportationResponseDto("Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
