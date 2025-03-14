package com.github.sansarch.desafio_totvs.domain.entity;

import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableAlreadyPaidException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableDescriptionRequiredException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableDueDateRequiredException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableValueRequiredException;
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
        assertThrows(PayableAlreadyPaidException.class, payable::markAsPaid);
    }

    @Test
    void shouldNotAllowMarkingPayableAsPaidIfCanceled() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertThrows(PayableAlreadyPaidException.class, payable::markAsPaid);
    }

    @Test
    void shouldCancelPayable() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertEquals(PayableStatus.CANCELED, payable.getStatus());
    }

    @Test
    void shouldNotAllowCancelIfAlreadyPaid() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.markAsPaid();
        assertThrows(PayableAlreadyPaidException.class, payable::cancel);
    }

    @Test
    void shouldNotAllowCancelIfAlreadyCanceled() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        payable.cancel();
        assertThrows(PayableAlreadyPaidException.class, payable::cancel);
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
    void shouldChangeValue() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        BigDecimal newValue = BigDecimal.ONE;

        assertNotEquals(newValue, payable.getValue());
        payable.changeValue(newValue);
        assertEquals(newValue, payable.getValue());
    }

    @Test
    void shouldChangeDescription() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        String newDescription = "New description";

        assertNotEquals(newDescription, payable.getDescription());
        payable.changeDescription(newDescription);
        assertEquals(newDescription, payable.getDescription());
    }

    @Test
    void shouldValidateValueChange() {
        assertThrows(PayableValueRequiredException.class, () -> PayableFactory.createNewPayable(
                LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description").changeValue(null));
    }

    @Test
    void shouldChangeDueDate() {
        Payable payable = PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, "Test description");
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(2);

        assertNotEquals(newDueDate, payable.getDueDate());
        payable.changeDueDate(newDueDate);
        assertEquals(newDueDate, payable.getDueDate());
    }

    @Test
    void shouldValidateDueDate() {
        assertThrows(PayableDueDateRequiredException.class, () -> PayableFactory.createNewPayable(
                null, BigDecimal.TEN, "Test description"));
    }

    @Test
    void shouldValidateValue() {
        assertThrows(PayableValueRequiredException.class, () -> PayableFactory.createNewPayable(
                LocalDateTime.now().plusDays(1), null, "Test description"));
    }

    @Test
    void shouldValidateDescription() {
        assertThrows(PayableDescriptionRequiredException.class, () -> PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, null));
        assertThrows(PayableDescriptionRequiredException.class, () -> PayableFactory.createNewPayable(LocalDateTime.now().plusDays(1), BigDecimal.TEN, ""));
    }
}