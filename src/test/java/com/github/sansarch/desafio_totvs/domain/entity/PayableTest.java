package com.github.sansarch.desafio_totvs.domain.entity;

import com.github.sansarch.desafio_totvs.domain.exception.PayableException;
import com.github.sansarch.desafio_totvs.domain.factory.PayableFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PayableTest {

    @Test
    void shouldCreatePayableWithPendingStatusAsDefault() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        assertEquals(PayableStatus.PENDING, payable.getStatus());
    }

    @Test
    void shouldRestoreExistingPayable() {
        UUID id = UUID.randomUUID();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        LocalDateTime paymentDate = LocalDateTime.now();
        BigDecimal value = BigDecimal.TEN;
        String description = "Test description";
        PayableStatus status = PayableStatus.PENDING;

        Payable restoredPayable = new Payable(
                id,
                dueDate,
                paymentDate,
                value,
                description,
                status);

        assertEquals(restoredPayable.getId(), restoredPayable.getId());
        assertEquals(restoredPayable.getDueDate(), restoredPayable.getDueDate());
        assertEquals(restoredPayable.getPaymentDate(), restoredPayable.getPaymentDate());
        assertEquals(restoredPayable.getValue(), restoredPayable.getValue());
        assertEquals(restoredPayable.getDescription(), restoredPayable.getDescription());
        assertEquals(restoredPayable.getStatus(), restoredPayable.getStatus());
    }

    @Test
    void shouldMarkPayableAsPaid() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.markAsPaid();
        assertEquals(PayableStatus.PAID, payable.getStatus());
        assertNotNull(payable.getPaymentDate());
    }

    @Test
    void shouldNotAllowMarkingPayableAsPaidIfAlreadyPaid() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.markAsPaid();
        assertThrows(PayableException.class, payable::markAsPaid);
    }

    @Test
    void shouldNotAllowMarkingPayableAsPaidIfCanceled() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertThrows(PayableException.class, payable::markAsPaid);
    }

    @Test
    void shouldCancelPayable() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertEquals(PayableStatus.CANCELED, payable.getStatus());
    }

    @Test
    void shouldIdentifyPayableAsOverdue() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().minusDays(1), BigDecimal.TEN, "Test description");
        assertTrue(payable.isOverdue());
    }

    @Test
    void shouldNotIdentifyPayableAsOverdueIfPaid() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().minusDays(1), BigDecimal.TEN, "Test description");
        payable.markAsPaid();
        assertFalse(payable.isOverdue());
    }

    @Test
    void shouldNotIdentifyPayableAsOverdueIfCanceled() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().minusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertFalse(payable.isOverdue());
    }

    @Test
    void shouldValidateDueDate() {
        assertThrows(PayableException.class, () -> PayableFactory.createNewPayable(
                null, BigDecimal.TEN, "Test description"));
    }

    @Test
    void shouldValidateValue() {
        assertThrows(PayableException.class, () -> PayableFactory.createNewPayable(
                LocalDateTime.now().plusDays(1), null, "Test description"));
    }

    @Test
    void shouldValidateDescription() {
        assertThrows(PayableException.class, () -> PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, null));
        assertThrows(PayableException.class, () -> PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, ""));
    }
}