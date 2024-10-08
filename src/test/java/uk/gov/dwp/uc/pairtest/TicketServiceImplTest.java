package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.mockito.Mock;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Optional;

public class TicketServiceImplTest {

    @Mock
    TicketPaymentService tps = mock(TicketPaymentService.class);
    @Mock
    SeatReservationService srs = mock(SeatReservationService.class);

    TicketService ts = new TicketServiceImpl(tps, srs);


    @Test
    public void purchaseTicketsTotalAmountTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        int expectedTotalAmount = 95;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertEquals(Optional.ofNullable(results.get("totalPrice")), Optional.of(expectedTotalAmount));

    }

    @Test
    public void purchaseTicketsTotalAmountNotEqualsTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        int expectedTotalAmount = 105;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertNotEquals(Optional.ofNullable(results.get("totalPrice")), Optional.of(expectedTotalAmount));

    }


    @Test
    public void purchaseTicketsTotalTicketsTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        int expectedTotalTickets = 5;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertEquals(Optional.ofNullable(results.get("totalTickets")), Optional.of(expectedTotalTickets));

    }


    @Test
    public void purchaseTicketsTotalTicketsNotEqualsTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3);
        long accountId = 123;
        int expectedTotalTickets = 5;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertNotEquals(Optional.ofNullable(results.get("totalTickets")), Optional.of(expectedTotalTickets));

    }


    @Test
    public void purchaseTicketsTotalSeatsTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        long accountId = 123;
        int expectedTotalSeats = 5;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertEquals(Optional.ofNullable(results.get("totalSeats")), Optional.of(expectedTotalSeats));

    }


    @Test
    public void purchaseTicketsTotalSeatsNotEqualsTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 4);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        long accountId = 123;
        int expectedTotalSeats = 5;

        Map<String, Integer> results = ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        assertNotEquals(Optional.ofNullable(results.get("totalSeats")), Optional.of(expectedTotalSeats));

    }



    @Test
    public void purchaseTicketsInvalidPurchaseTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> {
            ts.purchaseTickets(accountId,
                    ttr1,
                    ttr2,
                    ttr3
            );
        });

        assertEquals("At least one Adult ticket is required for Child or Infant tickets", exception.getMessage());
        assertEquals(3, exception.getStatusCode());
    }


    @Test
    public void purchaseTicketsInvalidPurchaseTest2() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 22);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        long accountId = 123;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> {
            ts.purchaseTickets(accountId,
                    ttr1,
                    ttr2,
                    ttr3
            );
        });

        assertEquals("Exceeded maximum number of tickets to be purchased at once", exception.getMessage());
        assertEquals(2, exception.getStatusCode());
    }


    @Test
    public void purchaseTicketsInvalidPurchaseTest3() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 22);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1);
        long accountId = 0;
        InvalidPurchaseException exception = assertThrows(InvalidPurchaseException.class, () -> {
            ts.purchaseTickets(accountId,
                    ttr1,
                    ttr2,
                    ttr3
            );
        });

        assertEquals("Invalid account ID, zero or negative", exception.getMessage());
        assertEquals(1, exception.getStatusCode());
    }


    @Test
    public void purchaseTicketsVerifyTicketPaymentServiceCallTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        int expectedTotalAmount = 95;

        ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        verify(tps).makePayment(accountId, expectedTotalAmount);
    }

    @Test
    public void purchaseTicketsVerifySeatReservationServiceCallTest() {

        TicketTypeRequest ttr1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        TicketTypeRequest ttr2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3);
        TicketTypeRequest ttr3 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0);
        long accountId = 123;
        int expectedTotalSeats = 5;

        ts.purchaseTickets(accountId,
                ttr1,
                ttr2,
                ttr3
        );

        verify(srs).reserveSeat(accountId, expectedTotalSeats);
    }
}